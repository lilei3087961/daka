package com.android.daka.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.android.daka.R;
import com.android.daka.utils.MyDateUtil;

import android.app.Activity;
import android.text.format.Time;
import android.widget.SimpleAdapter;

public class MyAdapters {
	private Activity mActivity;
	public MyAdapters(Activity activity){
		mActivity = activity;
	}
	public SimpleAdapter getSimpleAdapter(){
		String strDateOn = "2014-1-1 9:05:05";
		String strDateOff = "2014-1-1 17:05:05";
		String strDateOn2 = "2014-1-2 9:00:00";
		String strDateOff2 = "2014-1-2 17:10:05";
		
		ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("onWorkTime", strDateOn);
		map.put("offWorkTime", strDateOff);
		map.put("WorkedTime", MyDateUtil.getDiffTime(strDateOn, strDateOff));
		list.add(map);
		//map.clear();
		map=new HashMap<String, String>();
		map.put("onWorkTime", strDateOn2);
		map.put("offWorkTime", strDateOff2);
		map.put("WorkedTime", MyDateUtil.getDiffTime(strDateOn2, strDateOff2));
		list.add(map);
		
		SimpleAdapter adapter=new SimpleAdapter(mActivity, list, 
			R.layout.items_daka_info,new String[]{"onWorkTime","offWorkTime","WorkedTime"}, 
			new int[]{R.id.txtOnWorkTime,R.id.txtOffWorkTime,R.id.txtWorkedTime});
		return adapter;
	}
}
