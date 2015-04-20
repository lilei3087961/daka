package com.android.daka.fragments;

import com.android.daka.R;
import com.android.daka.adapters.MyAdapters;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {
	Button btnGoWork;
	Button btnOffWork;
	ListView listDakaInfo;
	public MainFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_daka, container,
				false);
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
				
			}
		});
		btnOffWork = (Button)rootView.findViewById(R.id.btnOffWork);
		btnOffWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		listDakaInfo = (ListView)rootView.findViewById(R.id.listDakaInfo);
		
	}
	void updateListView(){
		MyAdapters mMyAdapters = new MyAdapters(this.getActivity());
		listDakaInfo.setAdapter(mMyAdapters.getSimpleAdapter());
	}
}
