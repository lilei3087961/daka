package com.android.daka.fragments;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.android.daka.R;
import com.android.daka.views.DragGridView;
import com.android.daka.views.DragGridView.OnChanageListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class DragGridFragment extends Fragment {
    private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.draggrid_fragment,container,
                false);
        initView(rootView);
        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
    void initView(View rootView){
        DragGridView mDragGridView = (DragGridView) rootView.findViewById(R.id.dragGridView);
        for (int i = 0; i < 30; i++) {
            HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("item_image",R.drawable.ic_launcher);
            itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
            dataSourceList.add(itemHashMap);
        }
        

        final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this.getActivity(), dataSourceList,
                R.layout.draggrid_item, new String[] { "item_image", "item_text" },
                new int[] { R.id.item_image, R.id.item_text });
        
        mDragGridView.setAdapter(mSimpleAdapter);
        
        mDragGridView.setOnChangeListener(new OnChanageListener() {
            
            @Override
            public void onChange(int from, int to) {
                HashMap<String, Object> temp = dataSourceList.get(from);
                //直接交互item
//              dataSourceList.set(from, dataSourceList.get(to));
//              dataSourceList.set(to, temp);
                
                
                //这里的处理需要注意下
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(dataSourceList, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(dataSourceList, i, i-1);
                    }
                }
                
                dataSourceList.set(to, temp);
                
                mSimpleAdapter.notifyDataSetChanged();
                
                
            }
        });
    }
    
}
