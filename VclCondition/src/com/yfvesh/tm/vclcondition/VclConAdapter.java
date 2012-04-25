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
	// ����Context
	private Context		mContext;

	// ������������ ��ͼƬԴ
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
	    "ʣ������",
	    "������/������ϵͳ",
	    "�ŷ�ϵͳ",
	    "��ȫ����ϵͳ",
	    "�����ȶ�����ϵͳ",
	};

    private String[] mItemDetail =
    {
        "ʣ������:20L",
        "Ŀǰ״������",
        "Ŀǰ״���쳣,�뵽4S����ϸ���",
        "Ŀǰ״������",
        "Ŀǰ״������",
    };

	public VclConAdapter(Context c)
	{
		mContext = c;
	}

	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return mItemName.length;
	}

	// ��ȡͼƬ�ڿ��е�λ��
	public Object getItem(int position)
	{
		return position;
	}


	// ��ȡͼƬID
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

