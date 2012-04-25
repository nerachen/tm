package com.yfvesh.tm.infoondemand;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicItemAdapter extends BaseAdapter
{
	// 定义Context
	private Context		mContext;

    private Integer[] mItemState =
    {
         0,0,1,
    };

    private String[] mItemName =
    {
        "陈百强-偏偏喜欢你",
        "陈慧娴-飘雪",
        "郑源-一万个理由",
    };

    class ItemObj {
        public String Name;     //文件名称
        public Integer Type;    //文件类型
        public String Path;     //文件路径
        public View ItemView;   //是否创建
    }

    private List<ItemObj> mItemObjList = new ArrayList<ItemObj>();


	// 定义整型数组 即图片源
	private Integer[]	mItemIcon	=
	{
			R.drawable.icon_song,
			R.drawable.icon_video,

	};

    public MusicItemAdapter(Context c)
    {
        mContext = c;
        setTestData();
    }

	public MusicItemAdapter(Context c,String path)
	{
		mContext = c;
		setDirPath(path);
	}

	// 获取图片的个数
	public int getCount()
	{
		//return mItemName.length;
	    return mItemObjList.size();
	}

	// 获取图片在库中的位置
 	public Object getItem(int position)
 	{
 		return mItemObjList.get(position);
 	}

 	public String getItemPath(int position)
 	{
 	    return mItemObjList.get(position).Path;
 	}

	// 获取图片ID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
	    ItemObj item = mItemObjList.get(position);
	    View v = item.ItemView;
        if( v == null ){
            LayoutInflater factory = LayoutInflater.from(mContext);
            v = factory.inflate(R.layout.item_layout, null);
            if(position % 2 == 0)
                v.setBackgroundResource(R.drawable.item_odd_selected);
            else
                v.setBackgroundResource(R.drawable.item_even_selected);

            ImageView iv = (ImageView)v.findViewById(R.id.state_image);
            iv.setImageResource(mItemIcon[item.Type]);

            TextView tv0 = (TextView)v.findViewById(R.id.con_name);
            tv0.setText(item.Name);
            
            item.ItemView = v;
	    }
        
	    return v;


	}

    private void setDirPath(String path)
    {
        String mDirPath = path;
        String file_path;
        File home = new File(mDirPath);

        mItemObjList.clear();

        File[] files = home.listFiles(new SpecFileFilter());
        if(files.length > 0)
        {
            for(File file: files)
            {
                ItemObj item= new ItemObj();

                item.Name = file.getName();
                try {
                    file_path= file.getCanonicalPath();
                }
                catch(IOException e){

                    file_path = "";
                }
                item.Path = file_path;
                if(file_path.endsWith(".mp3"))
                {
                    item.Type = 0;
                }
                else
                {
                    item.Type = 1;
                }
                mItemObjList.add(item);

            }
        }

    }

    final class SpecFileFilter implements FilenameFilter
    {

        public boolean accept(File dir,String name)
        {
            return (name.endsWith(".mp3") || name.endsWith(".mp4"));
        }
    }

    void setTestData()
    {
       int size = mItemName.length;
       for(int pos = 0; pos < size ; pos ++)
       {
           ItemObj item= new ItemObj();

           item.Name = mItemName[pos];
           item.Type = mItemState[pos];
           mItemObjList.add(item);

       }
    }


}

