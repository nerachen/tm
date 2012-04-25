package com.yfvesh.tm.vclcondition;

import com.yfvesh.tm.vclcondition.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

public class VclConditionActivity extends Activity {
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

        
        ListView listview = (ListView)findViewById(R.id.itemlist);
        listview.setAdapter(new VclConAdapter(this));

    }
    
    private void goBack() {
        this.finish();
    }    
}