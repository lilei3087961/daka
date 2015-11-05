package com.android.daka.fragments;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.android.daka.Config;
import com.android.daka.R;
import com.android.daka.launcher.LauncherApplication;
import com.android.daka.views.DragGridView;
import com.android.daka.views.DragGridView.OnChanageListener;
import com.android.daka.views.DragGridView.OnDragListener;

import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class DragGridFragment extends Fragment {
    static final String TAG = Config.TAG_APP+"DragGridFragment";
    private List<Map<String, Object>> mAllAppList = new ArrayList<Map<String, Object>>();
    List<List<Map<String, Object>>> mPages = new ArrayList<List<Map<String, Object>>>();
    ViewGroup mRootView;
    LauncherApplication mLauncherApplication = null;
    private static final boolean SCROLL_VERTICAL = DragGridView.SCROLL_VERTICAL; //是否纵向滚动
    public static int pageCount = 2;
    private int mCurrentPage = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView;
        if(SCROLL_VERTICAL){
            rootView = inflater.inflate(R.layout.draggrid_fragment_vertical,container,false);
        }else{
            rootView = inflater.inflate(R.layout.draggrid_fragment_horizontal,container,false);
        }
        mRootView = (ViewGroup) rootView;
        getAllAppListForTestPages();
        initView(0);
        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void getAllAppList(){
        //if(mLauncherApplication == null)
            //mLauncherApplication = (LauncherApplication)getApplication();
    }
    public void getAllAppListForTest(){
        mAllAppList.clear();
        int COUNT;
        if(SCROLL_VERTICAL)
            COUNT = 60;
        else
            COUNT = 3;
        for (int i = 0; i < COUNT; i++) {
            Map<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("item_image",R.drawable.ic_launcher);
            itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
            mAllAppList.add(itemHashMap);
        }
    }
    public void getAllAppListForTestPages(){
        pageCount = 2;
        int lineCount = 3;
        mPages.clear();
//        List<Map<String, Object>> mpageAppList;
        //Map<String, Object> itemHashMap;
        for(int i=0;i<pageCount;i++){
            List<Map<String, Object>> mpageAppList = new ArrayList<Map<String, Object>>();
            for(int j=0;j<lineCount;j++){
                Map<String, Object> itemHashMap = new HashMap<String, Object>();
                itemHashMap.put("item_image",R.drawable.ic_launcher);
                itemHashMap.put("item_text", "拖拽 " + i+""+j);
                mpageAppList.add(itemHashMap);
            }
            mPages.add(mpageAppList);
        }
        
    }
    
    void initView(int page){
        mCurrentPage = page;
        Log.i(TAG, "~~~~ initView 11 mPages.size():"+mPages.size()+" page:"+page);
        DragGridView mDragGridView = (DragGridView) mRootView.findViewById(R.id.dragGridView);
        
        final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this.getActivity(), mPages.get(page),
                R.layout.draggrid_item, new String[] { "item_image", "item_text" },
                new int[] { R.id.item_image, R.id.item_text });
        mSimpleAdapter.setViewBinder(new ViewBinder(){
            @Override
            public boolean setViewValue(View arg0, Object arg1, String arg2) {
                // TODO Auto-generated method stub
                Log.i(TAG,"** ViewBinder arg0:"+arg0+" arg1:"+arg1);
                if( (arg0 instanceof ImageView) && (arg1 instanceof Bitmap)) {
                    ImageView iv = (ImageView) arg0;  
                    Bitmap bm = (Bitmap) arg1;  
                    iv.setImageBitmap(bm);  
                    return true;
                }
                return false;
            }
        });
       if(!SCROLL_VERTICAL){  //if scroll horizontal,
            int size = mPages.get(page).size();  //mAllAppList.size();
            int length = 100;
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 4) * density);
            int itemWidth = (int) (length * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
            mDragGridView.setLayoutParams(params); // �ص�
//            mDragGridView.setColumnWidth(itemWidth); // �ص�
//            mDragGridView.setHorizontalSpacing(5); // ���
//            mDragGridView.setStretchMode(GridView.NO_STRETCH);
            Log.i(TAG, "~~~~ initView size:"+size);
            mDragGridView.setNumColumns(size); // �ص�
        }
        mDragGridView.setAdapter(mSimpleAdapter);
        
        mDragGridView.setOnChangeListener(new OnChanageListener() {
            
            @Override
            public void onChange(int from, int to) {
                List<Map<String, Object>> mPageAppList = mPages.get(mCurrentPage);
                Map<String, Object> temp = mPageAppList.get(from);
                //直接交互item
//              dataSourceList.set(from, dataSourceList.get(to));
//              dataSourceList.set(to, temp);
                
                //这里的处理需要注意下
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(mPageAppList, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(mPageAppList, i, i-1);
                    }
                }
                
                mPageAppList.set(to, temp);
                
                mSimpleAdapter.notifyDataSetChanged();
                
                
            }
        });
        mDragGridView.setOnDragListener(new OnDragListener(){
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

            @Override
            public void onChangePage(int from, int to) {
                // TODO Auto-generated method stub
                initView(to);
                Log.i(TAG,"onChangePage from:"+from+" to:"+to);
            }
            
        });

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
    //for test begin
    //将draggrid_fragment_horizontal.xml 中的com.android.daka.views.DragGridView改成GridView测试
    void initViewTestGridView(View rootView){
        getAllAppListForTest();
        GridView mDragGridView = (GridView) rootView.findViewById(R.id.dragGridView);
        
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
        int size = mAllAppList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        mDragGridView.setLayoutParams(params); // �ص�
        mDragGridView.setColumnWidth(itemWidth); // �ص�
        mDragGridView.setHorizontalSpacing(5); // ���
        mDragGridView.setStretchMode(GridView.NO_STRETCH);
        mDragGridView.setNumColumns(size); // �ص�
        
        mDragGridView.setAdapter(mSimpleAdapter);
        
        

        //add by lilei end
    }
    //for test end
    
}
