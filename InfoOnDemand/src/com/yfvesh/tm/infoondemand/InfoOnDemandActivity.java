package com.yfvesh.tm.infoondemand;

import com.android.internal.telephony.ITelephony;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Method;

public class InfoOnDemandActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button backButton = (Button) this.findViewById(R.id.btnback);
        backButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
                goBack();
            }
        });

        Button callButton = (Button) this.findViewById(R.id.btntelephonist);
        callButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
                CallPhone();
            }
        });

        Button showListButton = (Button) this.findViewById(R.id.btnshowlist);

        /* 装载按钮事件 */
        showListButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
                Intent intent = new Intent();
                intent.setClass(InfoOnDemandActivity.this,MusicListActivity.class);
                startActivityForResult(intent,1);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
       if(resultCode == 1)
        {
            Toast.makeText(InfoOnDemandActivity.this, "己下载列表功能成功执行", Toast.LENGTH_SHORT).show();
        }
    }

    private void goBack() {
        this.finish();
    }

    private void CallPhone()
    {
        Method method;
        try {
            method = Class.forName(
                    "android.os.ServiceManager").getMethod(
                    "getService", String.class);
            // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
            IBinder binder = (IBinder) method.invoke(null,
                new Object[] { TELEPHONY_SERVICE });
            // 将IBinder对象的代理转换为ITelephony对象
            ITelephony telephony = ITelephony.Stub
                .asInterface(binder);

         // 挂断电话
            if(! telephony.isIdle()){
                telephony.endCall();
                Thread.sleep(500);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        Intent intent = new Intent();

        //为Intent设置Action属性（动作为：拨号）
        //intent.setAction(Intent.ACTION_DIAL);
        intent.setAction(Intent.ACTION_CALL);
        //String data = "tel:10086";
        String data = getResources().getString(R.string.call_number);
        //根据指定字符串解析出Uri对象
        Uri uri = Uri.parse(data);
        //设置Data属性
        intent.setData(uri);
        startActivity(intent);

    }
}