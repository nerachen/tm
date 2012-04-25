package com.yfvesh.test.tts;

import com.yfvesh.test.tts.R;

import com.yfvesh.tm.ttsservice.TextToSpeech;
import com.yfvesh.tm.ttsservice.TextToSpeech.OnInitListener;
import com.yfvesh.tm.ttsservice.TextToSpeech.OnUtteranceCompletedListener;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YfveTtsClientActivity extends Activity {

    TextToSpeech tts;

    EditText editText;
    Button speech;
    Button record;
    Button pause;
    Button resume;
    Button stop;



    private HashMap<String,String> mParamMap = new HashMap<String,String>();
    private String tts_text_id = "TTS_TEST";
    private String app_abs_path;

    protected static final String TEST_TTS_TAG = "TEST_TTS_CLIENT";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean connected=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editText = (EditText) findViewById(R.id.txt);
        speech = (Button) findViewById(R.id.speech);
        pause = (Button) findViewById(R.id.pause);
        resume = (Button) findViewById(R.id.resume);
        record = (Button) findViewById(R.id.record);
        stop = (Button) findViewById(R.id.stop);

        app_abs_path = this.getFilesDir().toString();

        final Handler myHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what == 0x5555)
                {
                    speech.setEnabled(true);
                }
            }

        };

        tts = new TextToSpeech(this, new OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                // 如果装载TTS引擎成功
                if (status == TextToSpeech.SUCCESS)
                {
                    // 设置使用美式英语朗读
                    int result = tts.setLanguage(Locale.US);
                    // 如果不支持所设置的语言
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                        && result != TextToSpeech.LANG_AVAILABLE)
                    {
                        Toast.makeText(YfveTtsClientActivity.this, "TTS暂时不支持这种语言的朗读。", 50000)
                            .show();
                    }
                }
                 tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener()
                {

                    @Override
                    public void onUtteranceCompleted(String utteranceId) {
                        // TODO Auto-generated method stub
                        if(utteranceId.equals(tts_text_id)){
                            //speech.setEnabled(true);
                            Message msg = new Message();
                            msg.what = 0x5555;
                            myHandler.sendMessage(msg);
                            Log.v(TEST_TTS_TAG, "TTS朗读完毕");
                        }
                    }

                });
         }

        });


        speech.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // 执行朗读
                //tts.speak(editText.getText().toString(),
                //        TextToSpeech.QUEUE_ADD,null);
                mParamMap.clear();
                mParamMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, tts_text_id);
                tts.speak(editText.getText().toString(),
                        TextToSpeech.QUEUE_ADD,mParamMap);
                speech.setEnabled(false);


             }
        });

        pause.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // 执行暂停
                tts.pause();
                Toast.makeText(YfveTtsClientActivity.this, "工作目录:" + app_abs_path , 50000)
                .show();


            }


        });

        resume.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // 执行恢复
                tts.resume();
            }
        });

        stop.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // 执行恢复
                tts.stop();
            }
        });
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }


}