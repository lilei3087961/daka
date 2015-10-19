package com.android.daka.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.android.daka.Config;
import com.android.daka.R;
import com.android.daka.database.MyDbHelper;
import com.android.daka.database.Tables;
import com.android.daka.utils.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MyAdapters {
	private Context mContext;
	private MyDbHelper mMyDbHelper;
	static final String TAG = Config.TAG_APP+"MyAdapters";
	private static MyAdapters mInstance;
	public MyAdapters(Context context){
		mContext = context;
		mMyDbHelper =  MyDbHelper.getInstance(context);
	}
	public static synchronized MyAdapters getInstance(Context context){
		if(mInstance == null){
			mInstance = new MyAdapters(context);
		}
		return mInstance;
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
		map.put("WorkedTime", DateUtils.getDiffTime(strDateOn, strDateOff));
		list.add(map);
		//map.clear();
		map=new HashMap<String, String>();
		map.put("onWorkTime", strDateOn2);
		map.put("offWorkTime", strDateOff2);
		map.put("WorkedTime", DateUtils.getDiffTime(strDateOn2, strDateOff2));
		list.add(map);
		
		SimpleAdapter adapter=new SimpleAdapter(mContext, list, 
			R.layout.items_daka_info,new String[]{"onWorkTime","offWorkTime","WorkedTime"}, 
			new int[]{R.id.txtOnWorkTime,R.id.txtOffWorkTime,R.id.txtWorkedTime});
		return adapter;
	}
	/***
	 *  only for simple db data show,
	 * @return 
	 */
	public SimpleCursorAdapter getSimpleCursorAdapter(){
		String strQuery = "select * from "+Tables.DakaInfo.TABLE_NAME
				+" ORDER BY "+Tables.DakaInfo.COL_ID+" desc";
		String onWorkTime = Tables.DakaInfo.COL_ON_WORK_TIME;
		String offWorkTime = Tables.DakaInfo.COL_OFF_WORK_TIME;
		String WorkedTime = DateUtils.getDiffTime(onWorkTime,offWorkTime);
		Cursor cursor = mMyDbHelper.queryRaw(strQuery);
		Log.i(TAG, "getSimpleCursorAdapter() getCount:"+cursor.getCount());
		SimpleCursorAdapter adapter =  null;
		if(cursor != null && cursor.getCount()>0){
			adapter = new SimpleCursorAdapter(mContext,
        		R.layout.items_daka_info, cursor,
        		new String[]{onWorkTime,offWorkTime},
        		new int[]{R.id.txtOnWorkTime,R.id.txtOffWorkTime},0);
		}
		return adapter;
	}
	/***
	 * for multi db data show,
	 * @return
	 */
	public DakaCursorAdapter getDakaCursorAdapter(){
		//Cursor cursor = mMyDbHelper.getDakaInfo();
		Cursor cursor = mMyDbHelper.getDakaInfoRecentMonth();
		DakaCursorAdapter adapter = new DakaCursorAdapter(mContext,cursor);
		return adapter;
	}
}
