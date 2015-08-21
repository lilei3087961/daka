package com.android.daka.adapters;

import com.android.daka.Config;
import com.android.daka.R;
import com.android.daka.database.Tables;
import com.android.daka.utils.DateUtil;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DakaCursorAdapter extends CursorAdapter {
    static final String TAG = Config.TAG_APP+"DakaCursorAdapter";
	Context  mContext=null;
	TextView txtOnWorkTime;
	TextView txtOffWorkTime;
	TextView txtWorkedTime;
	private static class ViewHolder{
	    ViewGroup rootLayout;
	    TextView txtOnWorkTime;
	    TextView txtOffWorkTime;
	    TextView txtWorkedTime;
	}
	public DakaCursorAdapter(Context context, Cursor c) {
		super(context, c,false);
		// TODO Auto-generated constructor stub
		mContext = context;  
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub
		View root =  arg0;
		ViewHolder holder = (ViewHolder)root.getTag();
        Log.i(TAG, "bindView() (holder == null)?"+(holder == null));
		if(holder == null){
		    holder = new ViewHolder();
		    holder.txtOnWorkTime = (TextView)root.findViewById(R.id.txtOnWorkTime);
		    holder.txtOffWorkTime = (TextView)root.findViewById(R.id.txtOffWorkTime);
		    holder.txtWorkedTime = (TextView)root.findViewById(R.id.txtWorkedTime);
		    root.setTag(holder);
		}
		String strOnWorkTime =arg2.getString(
				arg2.getColumnIndex(Tables.DakaInfo.COL_ON_WORK_TIME));
		String strOffWorkTime = arg2.getString(
				arg2.getColumnIndex(Tables.DakaInfo.COL_OFF_WORK_TIME));
		
		holder.txtOnWorkTime.setText(strOnWorkTime);
		holder.txtOffWorkTime.setText(strOffWorkTime);
		holder.txtWorkedTime.setText("");
		Log.i(TAG, ">>strOffWorkTime:"+strOffWorkTime);
		if(strOnWorkTime != null && strOffWorkTime != null
		        && !"".equals(strOffWorkTime.trim())
		        && !"".equals(strOnWorkTime.trim()))
		    holder.txtWorkedTime.setText(DateUtil.getDiffTime(strOnWorkTime, strOffWorkTime));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
	    Log.i(TAG, "newView()");
		ViewGroup view;
		LayoutInflater vi = null;  
	    vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    view =(ViewGroup)vi.inflate(R.layout.items_daka_info, arg2, false);  
		return view;
	}

}
