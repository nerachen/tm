package com.yfvesh.tm.weatherrep;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherTipsCtrl extends LinearLayout {
	
	private TextView mTxtViewTipsContent;
	private TextView mTxtViewTipsTitle;
	
	private ImageView  mImageTipsIcon;
	
	private LinearLayout  mLayout;

	public static final int MODE_DAY = 0;
	public static final int MODE_NGT = 1;
	
	public static final int STYLE_CARWASHING = 0;
	public static final int STYLE_TRAVALING = 1;
	public static final int STYLE_DRESSING = 2;
	public static final int STYLE_FEELING = 3;
	
	private List<TextView> mTxtviewLst = new LinkedList<TextView>();
	
    public WeatherTipsCtrl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    public WeatherTipsCtrl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.weather_tips_ctrl, this,true);
		mTxtViewTipsContent = (TextView) findViewById(R.id.txtviewTips);
		mTxtViewTipsTitle = (TextView) findViewById(R.id.txtviewTipsTitle);
		
		
		mImageTipsIcon = (ImageView) findViewById(R.id.imgviewTipsIcon);
		
		mLayout = (LinearLayout) findViewById(R.id.layoutTips);
		//setLayoutBackground(R.drawable.weather_frame_buttom_dark);
		//Add txt View to lst
		addtxtviewToLst();
	}
    
    private void addtxtviewToLst()
    {
    	mTxtviewLst.add(mTxtViewTipsContent);
    	mTxtviewLst.add(mTxtViewTipsTitle);

    }

    public void setTipsContent(String TipsContent)
    {
    	mTxtViewTipsContent.setText(TipsContent);
    }
    
    public void setLayoutBackground(int resId)
    {
    	mLayout.setBackgroundResource(resId);
    }
    
    public void setTipsTitle(String TipsTitle)
    {
    	mTxtViewTipsTitle.setText(TipsTitle);
    }
    
    
    public void setTipsIcon(int resId)
    {
    	mImageTipsIcon.setImageResource(resId);
    }
    
    public void setModeTextColor(int mode)
    {
    	int ViewNum  = mTxtviewLst.size();
    	int dayColor = 0xff0f4687;
    	int ngtColor = 0xffffffff;
    	if(MODE_DAY == mode)
    	{
    		for(int i = 0;i<ViewNum;i++)
    		{
    			mTxtviewLst.get(i).setTextColor(dayColor);
    		}
    	}
    	else if(MODE_NGT == mode)
    	{
    		for(int i = 0;i<ViewNum;i++)
    		{
    			mTxtviewLst.get(i).setTextColor(ngtColor);
    		}
    	}
    	else
    	{
    		
    	}
    }
    
    public void setModeBg(int mode)
    {
    	
    	if(MODE_DAY == mode)
    	{
    		setLayoutBackground(R.drawable.weather_frame_buttom_blue);
    	}
    	else if(MODE_NGT == mode)
    	{
    		setLayoutBackground(R.drawable.weather_frame_buttom_dark);
    	}
    	else
    	{
    		
    	}
    }
    
    public void setMode(int mode)
    {
    	setModeTextColor(mode);
    	setModeBg(mode);
    }
    
    public void setTipsStyle(int style)
    {
    	switch(style)
    	{
    	case STYLE_CARWASHING:
    		setTipsTitle("Ï´³µ");
    		setTipsIcon(R.drawable.icon_car_washing);
    		break;
    	case STYLE_TRAVALING:
    		setTipsTitle("ÂÃÓÎ");
    		setTipsIcon(R.drawable.icon_travel);
    		break;
    	case STYLE_DRESSING:
    		setTipsTitle("´©ÒÂ");
    		setTipsIcon(R.drawable.icon_ware);
    		break;
    	case STYLE_FEELING:
    		setTipsTitle("ÊæÊÊ¶È");
    		setTipsIcon(R.drawable.icon_comfort);
    		break;
    	default:
    		break;
    	}
    }
    
}