package com.yfvesh.tm.enterprisemsg.ttsmgr;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

public final class TtsMgr implements TextToSpeech.OnInitListener,
		OnUtteranceCompletedListener,
		com.yfvesh.tm.ttsservice.TextToSpeech.OnInitListener,
		com.yfvesh.tm.ttsservice.TextToSpeech.OnUtteranceCompletedListener {
	/* the system default TTS */
	private TextToSpeech mTts;
	/* the if TTS for zh_cn */
	private com.yfvesh.tm.ttsservice.TextToSpeech mIfTts;
	
	private static TtsMgr mTTSInstance;

	private static boolean USING_DEFAULT_TTS = false;
	private boolean mbTtsInited = false;
	private boolean mbTtsLocaled = false;

	private Map<String, String> mParamMap = new HashMap<String, String>();
	private Locale mLocale = Locale.US;
	private TtsStatusListener mListner;

	private TtsMgr() {
	}

	public static TtsMgr getInstance() {
		if (mTTSInstance == null) {
			mTTSInstance = new TtsMgr();
		}
		return mTTSInstance;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			// create tts done
			Log.d("tag","Time:"+System.currentTimeMillis());
			mbTtsInited = true;
			setLocale(mLocale);
		}
	}

	public void init(Context ctx, Locale locale, TtsStatusListener listner,
			String id) {
		mLocale = locale;
		mListner = listner;
		mParamMap.clear();
		mParamMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id);
		if (USING_DEFAULT_TTS) {
			// using sytem default tts
			if (mTts == null) {
				mTts = new TextToSpeech(ctx, this);
			} 
		} else {
			// using if tts
			if (mIfTts == null) {
				mIfTts = new com.yfvesh.tm.ttsservice.TextToSpeech(ctx, this);
				Log.d("tag","Time:"+System.currentTimeMillis());
			} 
		}

	}

	public void deInit() {
		if (USING_DEFAULT_TTS) {
			if (mTts != null) {
				mTts.stop();
				mTts.shutdown();
			}
			mbTtsInited = false;
			mbTtsLocaled = false;
			mTts = null;
		} else {
			if (mIfTts != null) {
				mIfTts.stop();
				mIfTts.shutdown();
			}
			mbTtsInited = false;
			mbTtsLocaled = false;
			mIfTts = null;
		}
		mListner = null;
	}

	public void releaseTts() {
		if (USING_DEFAULT_TTS) {
			if (mTts != null) {
				mTts.shutdown();
			}
		} else {
			if (mIfTts != null) {
				mIfTts.shutdown();
			}
		}
	}

	public boolean ttsSpeak(String str) {
		boolean bdone = false;
		if (mbTtsLocaled) {
			int result;
			if (USING_DEFAULT_TTS) {
				mTts.setOnUtteranceCompletedListener(this);
				result = mTts.speak(str, TextToSpeech.QUEUE_FLUSH,
						(HashMap<String, String>) mParamMap);
			} else {
				//mIfTts.resume();
				mIfTts.setOnUtteranceCompletedListener(this);
				result = mIfTts.speak(str, com.yfvesh.tm.ttsservice.TextToSpeech.QUEUE_FLUSH,
						(HashMap<String, String>) mParamMap);
			}
			if (TextToSpeech.SUCCESS == result) {
				bdone = true;
			}
		}
		return bdone;
	}

	public void ttsStop() {
		if (mbTtsLocaled) {
			if (USING_DEFAULT_TTS) {
				if(mTts!=null) {
					mTts.stop();
				}
			} else {
				if(mIfTts!=null) {
					//mIfTts.pause();
					mIfTts.stop();
				}
			}
		}
	}

	public boolean isInited() {
		return mbTtsInited;
	}

	public boolean isLocaled() {
		return mbTtsLocaled;
	}

	public boolean setLocale(Locale locale) {
		boolean bdone = false;
		if (mbTtsInited) {
			int result = 0;
			
			if (USING_DEFAULT_TTS) {
				result = mTts.setLanguage(locale);
			} else {
				result = mIfTts.setLanguage(locale);
			}
			
			if (TextToSpeech.LANG_MISSING_DATA == result
					|| TextToSpeech.LANG_NOT_SUPPORTED == result) {
				bdone = false;
			} else {
				bdone = true;
			}
		}
		mbTtsLocaled = bdone;
		return bdone;
	}

	public void setTtsListener(TtsStatusListener listner) {
		//Log.d("tag","setTtsListener lsnr id ="+listner.toString());
		mListner = listner;
	}

	@Override
	public void onUtteranceCompleted(String arg0) {
		//Log.d("tag","onUtteranceCompleted + arg =" +arg0);
		if (mListner != null) {
			mListner.onTtsDone(arg0);
		}
	}
}
