package com.yfvesh.test.tts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ActivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("com.yfve.action.TEST_BOARDCAST")) {
            Intent newIntent = new Intent(context, YfveTtsClientActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(newIntent);
        }

       Toast.makeText(context, "通过广播消息启动", 5000) .show();

    }
}
