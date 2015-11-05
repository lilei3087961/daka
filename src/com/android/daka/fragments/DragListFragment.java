package com.android.daka.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.daka.Config;
import com.android.daka.R;
import com.android.daka.launcher.LauncherApplication;
import com.android.daka.views.DragListView;
import com.android.daka.views.DragListView.OnChanageListener;
import com.android.daka.views.DragListView.OnDragListener;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class DragListFragment extends Fragment {
    static final String TAG = Config.TAG_APP+"DragListFragment";
    private List<Map<String, Object>> mAllAppList = new ArrayList<Map<String, Object>>();
    ViewGroup mRootView;
    LauncherApplication mLauncherApplication = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.draglist_fragment,container,
                false);
        mRootView = (ViewGroup) rootView;
        initView(rootView);
        return rootView;
    }

    public void getAllAppListForTest(){
        mAllAppList.clear();
        for (int i = 0; i < 30; i++) {
            Map<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("item_image",R.drawable.ic_launcher);
            itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
            mAllAppList.add(itemHashMap);
        }
    }
    void initView(View rootView){
        getAllAppListForTest();
        DragListView mDragListView = (DragListView) rootView.findViewById(R.id.dragListView);
        
        final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this.getActivity(), mAllAppList,
                R.layout.draggrid_item, new String[] { "item_image", "item_text" },
                new int[] { R.id.item_image, R.id.item_text });
        mSimpleAdapter.setViewBinder(new ViewBinder(){
            @Override
            public boolean setViewValue(View arg0, Object arg1, String arg2) {
                // TODO Auto-generated method stub
                Log.i(TAG,"ViewBinder arg0:"+arg0+" arg1:"+arg1);
                if( (arg0 instanceof ImageView) && (arg1 instanceof Bitmap)) {
                    ImageView iv = (ImageView) arg0;  
                    Bitmap bm = (Bitmap) arg1;  
                    iv.setImageBitmap(bm);  
                    return true;
                }
                return false;
            }
        });
        mDragListView.setAdapter(mSimpleAdapter);
        
        mDragListView.setOnChangeListener(new OnChanageListener() {
            
            @Override
            public void onChange(int from, int to) {
                Map<String, Object> temp = mAllAppList.get(from);
                //直接交互item
//              dataSourceList.set(from, dataSourceList.get(to));
//              dataSourceList.set(to, temp);
                
                
                //这里的处理需要注意下
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(mAllAppList, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(mAllAppList, i, i-1);
                    }
                }
                
                mAllAppList.set(to, temp);
                
                mSimpleAdapter.notifyDataSetChanged();
                
                
            }
        });
        //add by lilei begin
        mDragListView.setOnDragListener(new OnDragListener(){
            @Override
            public void onDragMove(View view,int x,int y) {
                // TODO Auto-generated method stub
                Log.i(TAG,"onDragMove x:"+x+" y:"+y+" view:"+view);
                moveDragImage(view,x,y);
            }
            
            @Override
            public void onDragStart(View view,int x,int y) {
                // TODO Auto-generated method stub
                Log.i(TAG,"onDragStart x:"+x+" y:"+y+" view:"+view);
                view.setAlpha(0.55f);
                mDragView = view;
                //mRootView.addView(view);
                showViewInPosition(getActivity(),view,x,y,-1);
            }

            @Override
            public void onDragEnd(View view, int x, int y) {
                // TODO Auto-generated method stub
                Log.i(TAG,"onDragEnd x:"+x+" y:"+y+" view:"+view);
                removeAddedView(view);
                view = null;
            }

            @Override
            public void onShowDropLocationView(View locationView, int x, int y) {
                // TODO Auto-generated method stub
                Log.i(TAG,"onShowDropLocation x:"+x+" y:"+y+" locationView:"+locationView);
                showViewInPosition(getActivity(),locationView,x,y,0);
            }

            @Override
            public void onHideDropLocationView(View locationView) {
                // TODO Auto-generated method stub
                Log.i(TAG,"onHideDropLocation locationView:"+locationView);
                removeAddedView(locationView);
            }
            
        });

        //add by lilei end
    }
    //add by lilei begin
    ImageView mImageView;
    View mDragView;
    FrameLayout.LayoutParams mViewGroupLayoutParams;
    public void showViewInPosition(Context context,View view,int x,int y,int index){
        if(mViewGroupLayoutParams == null){
            mViewGroupLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            //mViewGroupLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        }
        moveDragImage(view,x,y);
        //mRootView.addView(view,mViewGroupLayoutParams);
        mRootView.addView(view, index, mViewGroupLayoutParams);
    }
    public void moveDragImage(View view,int x,int y){
        view.setTranslationX(x);
        view.setTranslationY(y);
    }
    public void removeAddedView(View view){
        mRootView.removeView(view);
    }
    public void showImage(Context context){
        mImageView = new ImageView(context);
        mImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher));
        Log.i(TAG, "showImage mRootView.getLayoutParams():"+mRootView.getLayoutParams());
        if(mViewGroupLayoutParams == null){
            mViewGroupLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            mViewGroupLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        }
        mRootView.addView(mImageView,mViewGroupLayoutParams);
    }
    //add by lilei end
}
