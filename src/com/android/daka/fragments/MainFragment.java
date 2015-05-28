package com.android.daka.fragments;

import com.android.daka.R;
import com.android.daka.adapters.MyAdapters;
import com.android.daka.database.MyDbHelper;
import com.android.daka.database.Tables;
import com.android.daka.utils.ActivityUtils;
import com.android.daka.utils.ReflectionUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {
	Button btnGoWork;
	Button btnOffWork;
	ListView listDakaInfo;
	MyDbHelper mMyDbHelper;
	ActivityUtils mActivityUtils;
	public MainFragment(){

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_daka, container,
				false);
	    mMyDbHelper = MyDbHelper.getInstance(getActivity());
	    mActivityUtils = new ActivityUtils(getActivity());
		initView(rootView);
		updateListView();
		return rootView;
	}
	void initView(View rootView){
		btnGoWork = (Button)rootView.findViewById(R.id.btnGoWork);
		if(btnGoWork == null){
			Log.i("lilei","btnGoWork == null");
			return;
		}
		btnGoWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			    if(mMyDbHelper.addOnWork()){
			        updateListView();
			    }else{
	                 String str ="you had punched in today";
	                 //mActivityUtils.alert(str);
			    }
			}
		});
		btnOffWork = (Button)rootView.findViewById(R.id.btnOffWork);
		btnOffWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			    ReflectionUtils.test();
			 /*   if(mMyDbHelper.addOffWork()){
				    updateListView();
			    }else{
			        String str ="please punch in first";
				    mActivityUtils.alert(str);
			    } //*/
			}
		});
		listDakaInfo = (ListView)rootView.findViewById(R.id.listDakaInfo);
		
	}
	void updateListView(){
		MyAdapters mMyAdapters = MyAdapters.getInstance(this.getActivity());
		//listDakaInfo.setAdapter(mMyAdapters.getSimpleCursorAdapter()); //getDakaCursorAdapter
		listDakaInfo.setAdapter(mMyAdapters.getDakaCursorAdapter());
	}
}
