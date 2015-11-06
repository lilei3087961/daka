package com.android.daka.fragments;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.android.daka.Config;
import com.android.daka.R;
import com.android.daka.launcher.AllAppsList;
import com.android.daka.launcher.ApplicationInfo;
import com.android.daka.launcher.ApplicationManager;
import com.android.daka.launcher.LauncherApplication;
import com.android.daka.utils.BitmapUtils;
import com.android.daka.views.DragGridView;
import com.android.daka.views.DragGridView.OnChanageListener;
import com.android.daka.views.DragGridView.OnDragListener;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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
    private static final HandlerThread sWorkerThread = new HandlerThread("daka-loader");
    static {
        sWorkerThread.start();
    }
    public static final Handler sWorker = new Handler(sWorkerThread.getLooper());
    public static final Handler mHandler = new Handler();

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
        
        if(mLauncherApplication == null)
            mLauncherApplication = (LauncherApplication)getActivity().getApplication();
        Log.i(TAG,"onCreateView 111");
        sWorker.post(new RunnableWaitLoad());
        Log.i(TAG,"onCreateView 222");
//        getAllAndroidAppToList();
//        getAllAppListForTestPages();
//        initView(0);
        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
    public class RunnableWaitLoad implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.i(TAG,"RunnableWaitLoad 111");
            while(ApplicationManager.getLoadStatus() != true){
                try {
                    Thread.sleep(500);  //500ms
                } catch (InterruptedException unused) {
                }
            }
            Log.i(TAG,"RunnableWaitLoad 222");
            getAllAppPages();
            //getAllAppPagesTest();
            mHandler.post(new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    initView(0);
                }
            });

        }
        
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

    /***
     * 初始化指定page的图标信息到界面
     * init page views
     * @param page
     */
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

    ImageView mImageView;
    View mDragView;
    FrameLayout.LayoutParams mViewGroupLayoutParams;
    /***
     * 显示悬浮view到指定位置，
     * @param context
     * @param view
     * @param x
     * @param y
     * @param index 层级 -1层级最大
     */
    public void showViewInPosition(Context context,View view,int x,int y,int index){
        if(mViewGroupLayoutParams == null){
            mViewGroupLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            //mViewGroupLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        }
        moveDragImage(view,x,y);
        //mRootView.addView(view,mViewGroupLayoutParams);
        mRootView.addView(view, index, mViewGroupLayoutParams);
    }
    /***
     * 移动 view 到指定的位置
     * @param view
     * @param x
     * @param y
     */
    public void moveDragImage(View view,int x,int y){
        view.setTranslationX(x);
        view.setTranslationY(y);
    }
    /***
     * 移出弹出的view
     * @param view
     */
    public void removeAddedView(View view){
        mRootView.removeView(view);
    }

    //for test begin
    /***
     * 单页面测试，未使用
     * 将draggrid_fragment_horizontal.xml 中的com.android.daka.views.DragGridView改成GridView测试
     * @param rootView
     */
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
        
        


    }
    //add by lilei end
    final String KEY_PACKAGE = "packageName";
    final String KEY_CLASS = "className";
    /***
     * 通过Bitmap图片资源(android应用图标),获取图标，标题信息到mPages
     * 
     */
    public void getAllAppPages(){
        final ArrayList<ApplicationInfo> list = (ArrayList<ApplicationInfo>)mLauncherApplication.mModel.getAllAppInfo().data.clone();
        HashMap<String,String> mapName = null;
        List<HashMap<String,String>> listName = new ArrayList<HashMap<String,String>>();
        Log.i(TAG,"getAllAppPages 111 app size:"+list.size());
        for(int i=0;i< list.size();i++){
            if(list.get(i)!=null){
                String packageName = list.get(i).componentName.getPackageName();
                String className = list.get(i).componentName.getClassName();
                Log.i(TAG,">>getAllAndroidAppToList>>packageName:"+packageName+" className:"+className);
                mapName = new HashMap<String,String>();
                mapName.put(KEY_PACKAGE, packageName);
                mapName.put(KEY_CLASS, className);
                listName.add(mapName);
            }
        }
        Log.i(TAG,"getAllAppPages 222 listName.size():"+listName.size());
        int index=0;
        pageCount = 2;
        int lineCount = 3;
        mPages.clear();
        Bitmap appIcon = null;
        String title = "error";
        ComponentName componentName;
        String packageName;
        String className;
        for(int i=0;i<pageCount;i++){
            List<Map<String, Object>> mpageAppList = new ArrayList<Map<String, Object>>();
            for(int j=0;j<lineCount;j++){
                Map<String, Object> itemHashMap = new HashMap<String, Object>();
                if(mLauncherApplication.getIconCache() != null){
                    packageName = listName.get(index).get(KEY_PACKAGE);
                    className = listName.get(index).get(KEY_CLASS); 
                    index++;
                    componentName =  new ComponentName(packageName,className);
                    appIcon = mLauncherApplication.getIconCache().getComponentIcon(componentName);
                    appIcon = BitmapUtils.resizeBitmap(appIcon, 72, 72);
                    title = mLauncherApplication.getIconCache().getApplicationTitle(componentName);
                }
                Log.i(TAG,"getAllAppPages title:"+title+" appIcon:"+appIcon);
                itemHashMap.put("item_image",appIcon);
                itemHashMap.put("item_text", title);
                mpageAppList.add(itemHashMap);
            }
            mPages.add(mpageAppList);
        }
        
    }
    /***
     * 通过drawable图片资源(机器人图标),获取图标，标题信息到mPages
     */
    public void getAllAppPagesTest(){
        pageCount = 2;
        int lineCount = 3;
        mPages.clear();
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

    
}
