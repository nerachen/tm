
package com.yfvesh.tm.infoondemand;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends Activity {
//    private static String[] testarr = {
//            "A", "B", "c"
//    };

    private MusicItemAdapter mMusicItems = null;
    private int mSelPos = -1;
    private static final String TAG = "MusicListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);

        String music_dir_path = getResources().getString(R.string.music_dir_path);
        ListView listview = (ListView) findViewById(R.id.itemlist);
        //listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, testarr));
        //listview.setAdapter(new MusicItemAdapter(this));
        mMusicItems = new MusicItemAdapter(this,music_dir_path);
        listview.setAdapter(mMusicItems);

        Button backButton = (Button) this.findViewById(R.id.btnback);
        backButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View arg0)
            {
                goBack();
            }
        });
        
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            public boolean onItemLongClick (AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                //对长按的项进行处理
                //DisplayToast("长按了第"+Integer.toString(arg2+1)+"项");
                LaunchMusicPlay(arg2);
                return true;
            }

        });

    }

    private void goBack() {
        this.finish();
    }
    
    public void onResume()
    {
        super.onResume();
        //Log.v(TAG, "onResume");
        ListView listview = (ListView) findViewById(R.id.itemlist);
        listview.setSelection(mSelPos+1);        
    }
    
    /* 显示Toast  */
    public void DisplayToast(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void LaunchMusicPlay(int idx)
    {
        Intent it = new Intent(Intent.ACTION_VIEW);
        String uri_val = "file://" + mMusicItems.getItemPath(idx);
        Uri uri = Uri.parse(uri_val);
        it.setDataAndType(uri, "audio/MP3");
        startActivityForResult(it,idx);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        //DisplayToast("播放了"+ Integer.toString(requestCode)+"项");
        mSelPos = requestCode;
       
    }
}
