package com.yfvesh.tm.ttsservice;

import com.iflytek.tts.TtsService.AudioData;
import com.iflytek.tts.TtsService.Tts;
import com.yfvesh.tm.ttsservice.ITts.Stub;
import com.yfvesh.tm.ttsservice.ITtsCallback;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class TtsService extends Service {

    private static class SpeechItem {
        public static final int TEXT = 0;
        public static final int EARCON = 1;
        public static final int SILENCE = 2;
        public static final int TEXT_TO_FILE = 3;
        public String mText = "";
        public ArrayList<String> mParams = null;
        public int mType = TEXT;
        public long mDuration = 0;
        public String mFilename = null;
        public String mCallingApp = "";

        public SpeechItem(String source, String text, ArrayList<String> params, int itemType) {
            mText = text;
            mParams = params;
            mType = itemType;
            mCallingApp = source;
        }

        public SpeechItem(String source, long silenceTime, ArrayList<String> params) {
            mDuration = silenceTime;
            mParams = params;
            mType = SILENCE;
            mCallingApp = source;
        }

        public SpeechItem(String source, String text, ArrayList<String> params,
                int itemType, String filename) {
            mText = text;
            mParams = params;
            mType = itemType;
            mFilename = filename;
            mCallingApp = source;
        }

    }

    // If the speech queue is locked for more than 5 seconds, something has gone
    // very wrong with processSpeechQueue.
    private static final int SPEECHQUEUELOCK_TIMEOUT = 5000;
    private static final int MAX_SPEECH_ITEM_CHAR_LENGTH = 2048;
    // TODO use the TTS stream type when available
    private static final int DEFAULT_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    protected static final String SERVICE_TAG = "TtsService";

    private final RemoteCallbackList<ITtsCallback> mCallbacks
            = new RemoteCallbackList<ITtsCallback>();

    private HashMap<String, ITtsCallback> mCallbacksMap;

    private Boolean mIsSpeaking;
    private Boolean mSynthBusy;
    private ArrayList<SpeechItem> mSpeechQueue;

    private TtsServerBinder mBinder;
    private SpeechItem mCurrentSpeechItem;
    private HashMap<SpeechItem, Boolean> mKillList; // Used to ensure that in-flight synth calls
                                                    // are killed when stop is used.
    private TtsService mSelf;

    private boolean mExitThread = false;
    // lock for the speech queue (mSpeechQueue) and the current speech item (mCurrentSpeechItem)
    private final ReentrantLock speechQueueLock = new ReentrantLock();
    private final ReentrantLock synthesizerLock = new ReentrantLock();
    @Override
    public void onCreate()
    {
        super.onCreate();
        mBinder = new TtsServerBinder();

        mSelf = this;
        mIsSpeaking = false;
        mSynthBusy = false;
        //启动并初始化TTS
        Tts.JniCreate("/sdcard/TTS/Resource.irf");
        //String resource_path = getResources().getString(R.string.tts_resource_path);
        //Tts.JniCreate(resource_path);
        mCallbacksMap = new HashMap<String, ITtsCallback>();

        mSpeechQueue = new ArrayList<SpeechItem>();
        Tts.JniSetParam(256, 1);
        Tts.JniSetParam(1280, 3);

        mKillList = new HashMap<SpeechItem, Boolean>();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        killAllUtterances();


        mBinder = null;
        mExitThread = true;

        //停止并退出TTS
        Tts.JniStop();
        Tts.JniDestory();

        // Unregister all callbacks.
        mCallbacks.kill();

        Log.v(SERVICE_TAG, "onDestroy() completed");
    }


    /**
     * Speaks the given text using the specified queueing mode and parameters.
     *
     * @param text
     *            The text that should be spoken
     * @param queueMode
     *            TextToSpeech.TTS_QUEUE_FLUSH for no queue (interrupts all previous utterances),
     *            TextToSpeech.TTS_QUEUE_ADD for queued
     * @param params
     *            An ArrayList of parameters. This is not implemented for all
     *            engines.
     */
    private int speak(String callingApp, String text, int queueMode, ArrayList<String> params) {
        Log.v(SERVICE_TAG, "TTS service received " + text);
        if (queueMode == TextToSpeech.QUEUE_FLUSH) {
            stop(callingApp);
        } else if (queueMode == 2) {
            stopAll(callingApp);
        }
        mSpeechQueue.add(new SpeechItem(callingApp, text, params, SpeechItem.TEXT));
        if (!mIsSpeaking) {
            processSpeechQueue();
        }
        return TextToSpeech.SUCCESS;
    }

    /**
     * Stops all speech output and removes any utterances still in the queue for the calling app.
     */
    private int stop(String callingApp) {
        int result = TextToSpeech.ERROR;
        boolean speechQueueAvailable = false;
        try{
            speechQueueAvailable =
                    speechQueueLock.tryLock(SPEECHQUEUELOCK_TIMEOUT, TimeUnit.MILLISECONDS);
            if (speechQueueAvailable) {
                Log.i(SERVICE_TAG, "Stopping");
                for (int i = mSpeechQueue.size() - 1; i > -1; i--){
                    if (mSpeechQueue.get(i).mCallingApp.equals(callingApp)){
                        mSpeechQueue.remove(i);
                    }
                }
                if ((mCurrentSpeechItem != null) &&
                     mCurrentSpeechItem.mCallingApp.equals(callingApp)) {
                    try {
                        //result = sNativeSynth.stop();
                        result = Tts.JniStop();

                    } catch (NullPointerException e1) {
                        // synth will become null during onDestroy()
                        result = TextToSpeech.ERROR;
                    }
                    mKillList.put(mCurrentSpeechItem, true);
                    //if (mPlayer != null) {
                    //    try {
                    //        mPlayer.stop();
                    //    } catch (IllegalStateException e) {
                    //        // Do nothing, the player is already stopped.
                    //    }
                    //}
                    mIsSpeaking = false;
                    mCurrentSpeechItem = null;
                } else {
                    result = TextToSpeech.SUCCESS;
                }
                Log.i(SERVICE_TAG, "Stopped");
            } else {
                Log.e(SERVICE_TAG, "TTS stop(): queue locked longer than expected");
                result = TextToSpeech.ERROR;
            }
        } catch (InterruptedException e) {
          Log.e(SERVICE_TAG, "TTS stop: tryLock interrupted");
          e.printStackTrace();
        } finally {
            // This check is needed because finally will always run; even if the
            // method returns somewhere in the try block.
            if (speechQueueAvailable) {
                speechQueueLock.unlock();
            }
            return result;
        }
    }


    /**
     * Stops all speech output, both rendered to a file and directly spoken, and removes any
     * utterances still in the queue globally. Files that were being written are deleted.
     */
    @SuppressWarnings("finally")
    private int killAllUtterances() {
        int result = TextToSpeech.ERROR;
        boolean speechQueueAvailable = false;

        try {
            speechQueueAvailable = speechQueueLock.tryLock(SPEECHQUEUELOCK_TIMEOUT,
                    TimeUnit.MILLISECONDS);
            if (speechQueueAvailable) {
                // remove every single entry in the speech queue
                mSpeechQueue.clear();

                // clear the current speech item
                if (mCurrentSpeechItem != null) {
                    //result = sNativeSynth.stopSync();
                    result = Tts.JniStop();
                    mKillList.put(mCurrentSpeechItem, true);
                    mIsSpeaking = false;

                    // was the engine writing to a file?
                    if (mCurrentSpeechItem.mType == SpeechItem.TEXT_TO_FILE) {
                        // delete the file that was being written
                        if (mCurrentSpeechItem.mFilename != null) {
                            File tempFile = new File(mCurrentSpeechItem.mFilename);
                            Log.v(SERVICE_TAG, "Leaving behind " + mCurrentSpeechItem.mFilename);
                            if (tempFile.exists()) {
                                Log.v(SERVICE_TAG, "About to delete "
                                        + mCurrentSpeechItem.mFilename);
                                if (tempFile.delete()) {
                                    Log.v(SERVICE_TAG, "file successfully deleted");
                                }
                            }
                        }
                    }

                    mCurrentSpeechItem = null;
                }
            } else {
                Log.e(SERVICE_TAG, "TTS killAllUtterances(): queue locked longer than expected");
                result = TextToSpeech.ERROR;
            }
        } catch (InterruptedException e) {
            Log.e(SERVICE_TAG, "TTS killAllUtterances(): tryLock interrupted");
            result = TextToSpeech.ERROR;
        } finally {
            // This check is needed because finally will always run, even if the
            // method returns somewhere in the try block.
            if (speechQueueAvailable) {
                speechQueueLock.unlock();
            }
            return result;
        }
    }


    /**
     * Stops all speech output and removes any utterances still in the queue globally, except
     * those intended to be synthesized to file.
     */
    private int stopAll(String callingApp) {
        int result = TextToSpeech.ERROR;
        boolean speechQueueAvailable = false;
        try{
            speechQueueAvailable =
                    speechQueueLock.tryLock(SPEECHQUEUELOCK_TIMEOUT, TimeUnit.MILLISECONDS);
            if (speechQueueAvailable) {
                for (int i = mSpeechQueue.size() - 1; i > -1; i--){
                    if (mSpeechQueue.get(i).mType != SpeechItem.TEXT_TO_FILE){
                        mSpeechQueue.remove(i);
                    }
                }
                if ((mCurrentSpeechItem != null) &&
                    ((mCurrentSpeechItem.mType != SpeechItem.TEXT_TO_FILE) ||
                      mCurrentSpeechItem.mCallingApp.equals(callingApp))) {
                    try {
                        //result = sNativeSynth.stop();
                        result = Tts.JniStop();
                    } catch (NullPointerException e1) {
                        // synth will become null during onDestroy()
                        result = TextToSpeech.ERROR;
                    }
                    mKillList.put(mCurrentSpeechItem, true);
                    //if (mPlayer != null) {
                    //    try {
                    //        mPlayer.stop();
                    //    } catch (IllegalStateException e) {
                    //        // Do nothing, the player is already stopped.
                    //    }
                    //}
                    mIsSpeaking = false;
                    mCurrentSpeechItem = null;
                } else {
                    result = TextToSpeech.SUCCESS;
                }
                Log.i(SERVICE_TAG, "Stopped all");
            } else {
                Log.e(SERVICE_TAG, "TTS stopAll(): queue locked longer than expected");
                result = TextToSpeech.ERROR;
            }
        } catch (InterruptedException e) {
          Log.e(SERVICE_TAG, "TTS stopAll: tryLock interrupted");
          e.printStackTrace();
        } finally {
            // This check is needed because finally will always run; even if the
            // method returns somewhere in the try block.
            if (speechQueueAvailable) {
                speechQueueLock.unlock();
            }
            return result;
        }
    }

    public void onCompletion(/*MediaPlayer arg0*/) {
        // mCurrentSpeechItem may become null if it is stopped at the same
        // time it completes.
        SpeechItem currentSpeechItemCopy = mCurrentSpeechItem;
        if (currentSpeechItemCopy != null) {
            String callingApp = currentSpeechItemCopy.mCallingApp;
            ArrayList<String> params = currentSpeechItemCopy.mParams;
            String utteranceId = "";
            if (params != null) {
                for (int i = 0; i < params.size() - 1; i = i + 2) {
                    String param = params.get(i);
                    if (param.equals(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID)) {
                        utteranceId = params.get(i + 1);
                    }
                }
            }
            if (utteranceId.length() > 0) {
                dispatchUtteranceCompletedCallback(utteranceId, callingApp);
            }
        }
        processSpeechQueue();
    }


    private void silence(final SpeechItem speechItem) {
        class SilenceThread implements Runnable {
            public void run() {
                String utteranceId = "";
                if (speechItem.mParams != null){
                    for (int i = 0; i < speechItem.mParams.size() - 1; i = i + 2){
                        String param = speechItem.mParams.get(i);
                        if (param.equals(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID)){
                            utteranceId = speechItem.mParams.get(i+1);
                        }
                    }
                }
                try {
                    Thread.sleep(speechItem.mDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (utteranceId.length() > 0){
                        dispatchUtteranceCompletedCallback(utteranceId, speechItem.mCallingApp);
                    }
                    processSpeechQueue();
                }
            }
        }
        Thread slnc = (new Thread(new SilenceThread()));
        slnc.setPriority(Thread.MIN_PRIORITY);
        slnc.start();
    }

    private void speakInternalOnly(final SpeechItem speechItem) {
        class SynthThread implements Runnable {
            public void run() {
                boolean synthAvailable = false;
                String utteranceId = "";
                try {
                    synthAvailable = synthesizerLock.tryLock();
                    if (!synthAvailable) {
                        mSynthBusy = true;
                        Thread.sleep(100);
                        Thread synth = (new Thread(new SynthThread()));
                        synth.start();
                        mSynthBusy = false;
                        return;
                    }
                    int streamType = DEFAULT_STREAM_TYPE;
                    String language = "";
                    String country = "";
                    String variant = "";
                    String speechRate = "";
                    String engine = "";
                    String pitch = "";
                    if (speechItem.mParams != null){
                        for (int i = 0; i < speechItem.mParams.size() - 1; i = i + 2){
                            String param = speechItem.mParams.get(i);
                            if (param != null) {
                                if (param.equals(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID)){
                                    utteranceId = speechItem.mParams.get(i+1);
                                } else if (param.equals(TextToSpeech.Engine.KEY_PARAM_STREAM)) {
                                    try {
                                        streamType
                                                = Integer.parseInt(speechItem.mParams.get(i + 1));
                                    } catch (NumberFormatException e) {
                                        streamType = DEFAULT_STREAM_TYPE;
                                    }
                                }
                            }
                        }
                    }
                    // Only do the synthesis if it has not been killed by a subsequent utterance.
                    if (mKillList.get(speechItem) == null) {
                        try {
                            //sNativeSynth.speak(speechItem.mText, streamType);
                            Tts.JniSpeak(speechItem.mText);
                        } catch (NullPointerException e) {
                            // synth will become null during onDestroy()
                            Log.v(SERVICE_TAG, " null synth, can't speak");
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(SERVICE_TAG, "TTS speakInternalOnly(): tryLock interrupted");
                    e.printStackTrace();
                } finally {
                    // This check is needed because finally will always run;
                    // even if the
                    // method returns somewhere in the try block.
                    if (utteranceId.length() > 0){
                        dispatchUtteranceCompletedCallback(utteranceId, speechItem.mCallingApp);
                    }
                    if (synthAvailable) {
                        synthesizerLock.unlock();
                        processSpeechQueue();
                    }
                }
            }
        }
        Thread synth = (new Thread(new SynthThread()));
        synth.setPriority(Thread.MAX_PRIORITY);
        synth.start();
    }

    private void synthToFileInternalOnly(final SpeechItem speechItem) {
    }
    private void broadcastTtsQueueProcessingCompleted(){
        Intent i = new Intent(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        sendBroadcast(i);
    }


    private void dispatchUtteranceCompletedCallback(String utteranceId, String packageName) {
        ITtsCallback cb = mCallbacksMap.get(packageName);
        if (cb == null){
            return;
        }
        Log.v(SERVICE_TAG, "TTS callback: dispatch started");
        // Broadcast to all clients the new value.
        final int N = mCallbacks.beginBroadcast();
        try {
            cb.utteranceCompleted(utteranceId);
        } catch (RemoteException e) {
            // The RemoteCallbackList will take care of removing
            // the dead object for us.
        }
        mCallbacks.finishBroadcast();
        Log.v(SERVICE_TAG, "TTS callback: dispatch completed to " + N);
    }

    private SpeechItem splitCurrentTextIfNeeded(SpeechItem currentSpeechItem){
        if (currentSpeechItem.mText.length() < MAX_SPEECH_ITEM_CHAR_LENGTH){
            return currentSpeechItem;
        } else {
            String callingApp = currentSpeechItem.mCallingApp;
            ArrayList<SpeechItem> splitItems = new ArrayList<SpeechItem>();
            int start = 0;
            int end = start + MAX_SPEECH_ITEM_CHAR_LENGTH - 1;
            String splitText;
            SpeechItem splitItem;
            while (end < currentSpeechItem.mText.length()){
                splitText = currentSpeechItem.mText.substring(start, end);
                splitItem = new SpeechItem(callingApp, splitText, null, SpeechItem.TEXT);
                splitItems.add(splitItem);
                start = end;
                end = start + MAX_SPEECH_ITEM_CHAR_LENGTH - 1;
            }
            splitText = currentSpeechItem.mText.substring(start);
            splitItem = new SpeechItem(callingApp, splitText, null, SpeechItem.TEXT);
            splitItems.add(splitItem);
            mSpeechQueue.remove(0);
            for (int i = splitItems.size() - 1; i >= 0; i--){
                mSpeechQueue.add(0, splitItems.get(i));
            }
            return mSpeechQueue.get(0);
        }
    }

    private void processSpeechQueue() {
        boolean speechQueueAvailable = false;
        synchronized (this) {
            if (mSynthBusy){
                // There is already a synth thread waiting to run.
                return;
            }
        }
        try {
            speechQueueAvailable =
                    speechQueueLock.tryLock(SPEECHQUEUELOCK_TIMEOUT, TimeUnit.MILLISECONDS);
            if (!speechQueueAvailable) {
                Log.e(SERVICE_TAG, "processSpeechQueue - Speech queue is unavailable.");
                return;
            }
            if (mSpeechQueue.size() < 1) {
                mIsSpeaking = false;
                mKillList.clear();
                broadcastTtsQueueProcessingCompleted();
                return;
            }

            mCurrentSpeechItem = mSpeechQueue.get(0);
            mIsSpeaking = true;
                if (mCurrentSpeechItem.mType == SpeechItem.TEXT) {
                    mCurrentSpeechItem = splitCurrentTextIfNeeded(mCurrentSpeechItem);
                    speakInternalOnly(mCurrentSpeechItem);
                } else if (mCurrentSpeechItem.mType == SpeechItem.TEXT_TO_FILE) {
                    synthToFileInternalOnly(mCurrentSpeechItem);
                } else {
                    // This is either silence or an earcon that was missing
                    silence(mCurrentSpeechItem);
                }
            if (mSpeechQueue.size() > 0) {
                mSpeechQueue.remove(0);
            }
        } catch (InterruptedException e) {
          Log.e(SERVICE_TAG, "TTS processSpeechQueue: tryLock interrupted");
          e.printStackTrace();
        } finally {
            // This check is needed because finally will always run; even if the
            // method returns somewhere in the try block.
            if (speechQueueAvailable) {
                speechQueueLock.unlock();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }


    public class TtsServerBinder extends Stub
    {
        public int registerCallback(String packageName, ITtsCallback cb) {
            if (cb != null) {
                mCallbacks.register(cb);
                mCallbacksMap.put(packageName, cb);
                return TextToSpeech.SUCCESS;
            }
            return TextToSpeech.ERROR;
        }

        public int unregisterCallback(String packageName, ITtsCallback cb) {
            if (cb != null) {
                mCallbacksMap.remove(packageName);
                mCallbacks.unregister(cb);
                return TextToSpeech.SUCCESS;
            }
            return TextToSpeech.ERROR;
        }

        /**
         * Speaks the given text using the specified queueing mode and
         * parameters.
         *
         * @param text
         *            The text that should be spoken
         * @param queueMode
         *            TextToSpeech.TTS_QUEUE_FLUSH for no queue (interrupts all previous utterances)
         *            TextToSpeech.TTS_QUEUE_ADD for queued
         * @param params
         *            An ArrayList of parameters. The first element of this
         *            array controls the type of voice to use.
         */
        public int speak(String callingApp, String text, int queueMode, String[] params) {
            //Log.d(SERVICE_TAG,"speak:--->("+text + ") $ mode=" + queueMode );
            //add_text(text);
           ArrayList<String> speakingParams = new ArrayList<String>();
            if (params != null) {
                speakingParams = new ArrayList<String>(Arrays.asList(params));
            }
            return mSelf.speak(callingApp, text, queueMode, speakingParams);
        }
        
        @Override
        public int playEarcon(String callingApp, String earcon, int queueMode, String[] params)
                throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        } 
        
        @Override
        public int playSilence(String callingApp, long duration, int queueMode, String[] params)
                throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int stop(String callingApp) throws RemoteException {
            // TODO Auto-generated method stub
            return mSelf.stop(callingApp);
        }
        
        @Override
        public boolean isSpeaking() throws RemoteException {
            // TODO Auto-generated method stub
            return (mSelf.mIsSpeaking && (mSpeechQueue.size() < 1));
        }

        @Override
        public void addSpeech(String callingApp, String text, String packageName, int resId)
                throws RemoteException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void addSpeechFile(String callingApp, String text, String filename)
                throws RemoteException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void addEarcon(String callingApp, String earcon, String packageName, int resId)
                throws RemoteException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void addEarconFile(String callingApp, String earcon, String filename)
                throws RemoteException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public int setSpeechRate(String callingApp, int speechRate) throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }        
                


        @Override
        public int setPitch(String callingApp, int pitch) throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int isLanguageAvailable(String language, String country, String variant,
                String[] params) throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }
        
        @Override
        public String[] getLanguage() throws RemoteException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public int setLanguage(String callingApp, String language, String country, String variant)
                throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean synthesizeToFile(String callingApp, String text, String[] params,
                String outputDirectory) throws RemoteException {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public int setEngineByPackageName(String enginePackageName) throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }
        
        @Override
        public String getDefaultEngine() throws RemoteException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean areDefaultsEnforced() throws RemoteException {
            // TODO Auto-generated method stub
            return false;
        }
        
 				///////////////////////////////////////////////////////////////////////////////////////////               
      
        @Override
        public int pause() throws RemoteException {
            // TODO Auto-generated method stub
            Log.d(SERVICE_TAG,"pause");
            AudioData.pause();
            return 0;
        }

        @Override
        public int resume() throws RemoteException {
            // TODO Auto-generated method stub
            Log.d(SERVICE_TAG,"resume");
            AudioData.resume();
            return 0;
        }        

    }


}
