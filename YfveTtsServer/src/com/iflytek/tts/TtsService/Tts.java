package com.iflytek.tts.TtsService;

public final class Tts{
	static {
		System.loadLibrary("Aisound");
	}

	public static native int JniGetVersion();
	public static native int JniCreate(String resFilename);
	public static native int JniDestory();
	public static native int JniStop();
	public static native int JniSpeak(String text);
	public static native int JniSetParam(int paramId,int value);
	public static native int JniGetParam(int paramId);
	public static native int JniIsPlaying();
	public static native boolean JniIsCreated();
}
