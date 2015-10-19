package com.android.daka.fragments;

import com.android.daka.Config;
import com.android.daka.R;
import com.android.daka.adapters.MyAdapters;
import com.android.daka.database.MyDbHelper;
import com.android.daka.database.Tables;
import com.android.daka.net.NetUtils;
import com.android.daka.net.NetworkReceiver;
import com.android.daka.utils.ActivityUtils;
import com.android.daka.utils.FileUtils;
import com.android.daka.utils.ReflectionUtils;
import com.android.daka.utils.ShellUtils;
import com.android.daka.utils.SystemUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {
    static final String TAG = Config.TAG_APP+"MainFragment";
	Button btnGoWork;
	Button btnOffWork;
	ListView listDakaInfo;
	MyDbHelper mMyDbHelper;
	ActivityUtils mActivityUtils;
	NetUtils mNetUtils;
	public MainFragment(){

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_daka, container,
				false);
	    mMyDbHelper = MyDbHelper.getInstance(getActivity());
	    mActivityUtils = new ActivityUtils(getActivity());
	    mNetUtils = new NetUtils(getActivity());
		initView(rootView);
		updateListView();
		return rootView;
	}
	void initView(View rootView){
	    //for test begin
	    Log.i(TAG, "initView registe broadcast");
	    NetworkReceiver mNetworkReceiver = new NetworkReceiver();
	    IntentFilter filter = new IntentFilter();
	    filter.addAction("com.launcher.action");
	    getActivity().registerReceiver(mNetworkReceiver, filter);
	    //for test end
		btnGoWork = (Button)rootView.findViewById(R.id.btnGoWork);
		if(btnGoWork == null){
			Log.i(TAG,"btnGoWork == null");
			return;
		}
		btnGoWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	             //ShellUtils.test();
			    SystemUtils.getDisplayMetrics(getActivity());
			    //FileUtils.test(getActivity());
			    if(true)
			        return;
			    if(mMyDbHelper.addOnWork()){
			        updateListView();
			    }else{
	                 String str =getResources().getString(R.string.alert_punched_in);
	                 mActivityUtils.alert(str);
			    }
			}
		});
		btnOffWork = (Button)rootView.findViewById(R.id.btnOffWork);
		btnOffWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			    if(mMyDbHelper.addOffWork()){
				    updateListView();
			    }else{
			        String str =getResources().getString(R.string.alert_not_punch_in);
				    mActivityUtils.alert(str);
			    }
			}
		});
		listDakaInfo = (ListView)rootView.findViewById(R.id.listDakaInfo);
		
	}
	void updateListView(){
		MyAdapters mMyAdapters = MyAdapters.getInstance(this.getActivity());
		//listDakaInfo.setAdapter(mMyAdapters.getSimpleCursorAdapter()); //getDakaCursorAdapter
		listDakaInfo.setAdapter(mMyAdapters.getDakaCursorAdapter());
	}
	//add by lilei for 悬浮框 test begin
	
	//add by lilei for 悬浮框 test end
	
}
