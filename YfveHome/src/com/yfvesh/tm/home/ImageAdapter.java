package com.yfvesh.tm.home;

import com.yfvesh.tm.home.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	// 定义整型数组 即图片源
	private Integer[] mImageIds = { R.drawable.img1, R.drawable.img2,
			R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
			R.drawable.img7, R.drawable.img8, R.drawable.img1, R.drawable.img2, };

	public ImageAdapter(Context c) {
		mContext = c;
	}

	// 获取图片的个数
	public int getCount() {
		//return mImageIds.length;
		return YfveHomeActivity.INDEX_APPS_COUNT;
	}

	// 获取图片在库中的位置
	public Object getItem(int position) {
		return position;
	}

	// 获取图片ID
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v;
		if (convertView == null) {
			LayoutInflater factory = LayoutInflater.from(mContext);
			v = factory.inflate(R.layout.icon, null);
			TextView tv = (TextView) v.findViewById(R.id.icon_text);
			tv.setTextColor(Color.WHITE);
			Resources r = mContext.getResources();
			tv.setText(getIndexedString(position));
			//tv.setText(r.getString(R.string.mainmenu_choice0 + position));
			ImageView iv = (ImageView) v.findViewById(R.id.icon_image);
			//iv.setImageResource(mImageIds[position]);
			iv.setImageResource(getIndexedIcons(position));
			

		} else {
			v = convertView;
		}
		return v;
	}

	public static int getIndexedString(int index) {
		int id = R.string.mainmenu_choice_unknown;
		switch (index) {
		case YfveHomeActivity.INDEX_USRLOGIN: {
			id = R.string.mainmenu_choice0;
			break;
		}
		case YfveHomeActivity.INDEX_ENTP_MSG: {
			id = R.string.mainmenu_choice1;
			break;
		}
		case YfveHomeActivity.INDEX_ECALL: {
			id = R.string.mainmenu_choice2;
			break;
		}
		case YfveHomeActivity.INDEX_ORDER_MSG: {
			id = R.string.mainmenu_choice3;
			break;
		}
		case YfveHomeActivity.INDEX_ONLINE_POI: {
			id = R.string.mainmenu_choice4;
			break;
		}
		case YfveHomeActivity.INDEX_TEAM_COMMU: {
			id = R.string.mainmenu_choice5;
			break;
		}
		case YfveHomeActivity.INDEX_VEHICLE_STATUS: {
			id = R.string.mainmenu_choice6;
			break;
		}
		case YfveHomeActivity.INDEX_WEATHER: {
			id = R.string.mainmenu_choice7;
			break;
		}
		case YfveHomeActivity.INDEX_VEHICLE_CONDITION: {
			id = R.string.mainmenu_choice8;
			break;
		}
		case YfveHomeActivity.INDEX_NAVI: {
			id = R.string.mainmenu_choice9;
			break;
		}
		default:
			break;
		}
		return id;
	}
	public static int getIndexedIcons(int index) {
		int id = R.drawable.icon;
		switch (index) {
		case YfveHomeActivity.INDEX_USRLOGIN: {
			id = R.drawable.icon_usrlogin_states;
			break;
		}
		case YfveHomeActivity.INDEX_ENTP_MSG: {

			id = R.drawable.icon_entpmsg_states;
			break;
		}
		case YfveHomeActivity.INDEX_ECALL: {
			id = R.drawable.icon_ecall_states;
			break;
		}
		case YfveHomeActivity.INDEX_ORDER_MSG: {
			id = R.drawable.icon_ordermsg_states;
			break;
		}
		case YfveHomeActivity.INDEX_ONLINE_POI: {
			id = R.drawable.icon_onlinepoi_states;
			break;
		}
		case YfveHomeActivity.INDEX_TEAM_COMMU: {
			id = R.drawable.icon_teamcommu_states;
			break;
		}
		case YfveHomeActivity.INDEX_VEHICLE_STATUS: {
			id = R.drawable.icon_vehiclestatus_states;
			break;
		}
		case YfveHomeActivity.INDEX_WEATHER: {
			id = R.drawable.icon_weather_states;
			break;
		}
		case YfveHomeActivity.INDEX_VEHICLE_CONDITION: {
			id = R.drawable.icon_vehiclecondition_states;
			break;
		}
		case YfveHomeActivity.INDEX_NAVI: {
			id = R.drawable.icon_navi_states;
			break;
		}
		default:
			break;
		}
		return id;
	}

}
