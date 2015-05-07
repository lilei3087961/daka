package com.android.daka.adapters;

import com.android.daka.R;
import com.android.daka.database.Tables;
import com.android.daka.utils.DateUtil;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DakaCursorAdapter extends CursorAdapter {
	Context  mContext=null;
	TextView txtOnWorkTime;
	TextView txtOffWorkTime;
	TextView txtWorkedTime;
	public DakaCursorAdapter(Context context, Cursor c) {
		super(context, c,false);
		// TODO Auto-generated constructor stub
		mContext = context;  
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub
		View root =  arg0;
		txtOnWorkTime = (TextView)root.findViewById(R.id.txtOnWorkTime);
		txtOffWorkTime = (TextView)root.findViewById(R.id.txtOffWorkTime);
		txtWorkedTime = (TextView)root.findViewById(R.id.txtWorkedTime);
		String strOnWorkTime =arg2.getString(
				arg2.getColumnIndex(Tables.DakaInfo.COL_ON_WORK_TIME));
		String strOffWorkTime = arg2.getString(
				arg2.getColumnIndex(Tables.DakaInfo.COL_OFF_WORK_TIME));
		
		txtOnWorkTime.setText(strOnWorkTime);
		txtOffWorkTime.setText(strOffWorkTime);
		if(strOnWorkTime != null && strOffWorkTime != null)
			txtWorkedTime.setText(DateUtil.getDiffTime(strOnWorkTime, strOffWorkTime));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewGroup view;
		LayoutInflater vi = null;  
	    vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    view =(ViewGroup)vi.inflate(R.layout.items_daka_info, arg2, false);  
		return view;
	}

}
