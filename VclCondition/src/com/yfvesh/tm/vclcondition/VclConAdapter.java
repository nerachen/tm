package com.yfvesh.tm.vclcondition;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VclConAdapter extends BaseAdapter
{
	// 定义Context
	private Context		mContext;

	// 定义整型数组 即图片源
	private Integer[]	mItemIcon	=
	{
			R.drawable.state_ok,
			R.drawable.state_err,

	};

	private Integer[] mItemState =
	{
	     0,0,1,0,0,
	};

	private String[] mItemName =
	{
	    "剩余油量",
	    "发动机/变速器系统",
	    "排放系统",
	    "安全气囊系统",
	    "车身稳定控制系统",
	};

    private String[] mItemDetail =
    {
        "剩余油量:20L",
        "目前状况良好",
        "目前状况异常,请到4S店详细检测",
        "目前状况良好",
        "目前状况良好",
    };

	public VclConAdapter(Context c)
	{
		mContext = c;
	}

	// 获取图片的个数
	public int getCount()
	{
		return mItemName.length;
	}

	// 获取图片在库中的位置
	public Object getItem(int position)
	{
		return position;
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{

        View v;
        if(convertView==null){
        	LayoutInflater factory = LayoutInflater.from(mContext);
        	v = factory.inflate(R.layout.item_layout, null);
            ImageView iv = (ImageView)v.findViewById(R.id.state_image);
            iv.setImageResource(mItemIcon[mItemState[position]]);
            TextView tv0 = (TextView)v.findViewById(R.id.con_name);
            tv0.setText(mItemName[position]);
            TextView tv1 = (TextView)v.findViewById(R.id.con_detail);
            tv1.setText(mItemDetail[position]);
        }
        else
        {
            v = convertView;
        }
        return v;


	}

}

