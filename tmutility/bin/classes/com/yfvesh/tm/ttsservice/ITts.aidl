package com.yfvesh.tm.ttsservice;

import com.yfvesh.tm.ttsservice.ITtsCallback;

import android.content.Intent;

interface ITts
{
    int pause();
	
    int resume();
	
    int setSpeechRate(in String callingApp, in int speechRate);

    int setPitch(in String callingApp, in int pitch);


    int speak(in String callingApp, in String text, in int queueMode, in String[] params);

    boolean isSpeaking();

    int stop(in String callingApp);

    void addSpeech(in String callingApp, in String text, in String packageName, in int resId);

    void addSpeechFile(in String callingApp, in String text, in String filename);

    String[] getLanguage();

    int isLanguageAvailable(in String language, in String country, in String variant, in String[] params);

    int setLanguage(in String callingApp, in String language, in String country, in String variant);

    boolean synthesizeToFile(in String callingApp, in String text, in String[] params, in String outputDirectory);

    int playEarcon(in String callingApp, in String earcon, in int queueMode, in String[] params);

    void addEarcon(in String callingApp, in String earcon, in String packageName, in int resId);

    void addEarconFile(in String callingApp, in String earcon, in String filename);

    int registerCallback(in String callingApp, ITtsCallback cb);

    int unregisterCallback(in String callingApp, ITtsCallback cb);

    int playSilence(in String callingApp, in long duration, in int queueMode, in String[] params);

    int setEngineByPackageName(in String enginePackageName);

    String getDefaultEngine();

    boolean areDefaultsEnforced();
}