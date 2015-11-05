package com.android.daka.views;

import com.android.daka.Config;
import com.android.daka.fragments.DragGridFragment;
import com.android.daka.launcher.HolographicOutlineHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnLongClickListener;

/**
 * 参考 blog  http://blog.csdn.net/xiaanming 
 * 
 *
 *
 */
@SuppressLint("NewApi")
public class DragGridView extends GridView implements OnLongClickListener{
    final boolean SHOW_DROP_LOCATION = false; //是否显示占位图标轮廓
    public static final boolean SCROLL_VERTICAL = false; //是否纵向滚动
    public static final boolean HORIZONTAL_LONG = false; //首先 SCROLL_VERTICAL为false，其次 水平图标个数要很多才能设置为true(未实现)
    /**
     * DragGridView的item长按响应的时间， 默认是1000毫秒，也可以自行设置
     */
    private long dragResponseMS = 1000;
    static final String TAG = Config.TAG_APP+"DragGridView";
    /**
     * 是否可以拖拽，默认不可以
     */
    public static boolean isDrag = false;
    
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    /**
     * 正在拖拽的position
     */
    private int mDragPosition;
    
    /**
     * 刚开始拖拽的item对应的View
     */
    private View mStartDragItemView = null;
    
    /**
     * 用于拖拽的镜像，这里直接用一个ImageView
     */
    private ImageView mDragImageView;
    
    private ImageView mDropLocationImageView;
    
    /**
     * 震动器
     */
    private Vibrator mVibrator;
    
    private WindowManager mWindowManager;
    /**
     * item镜像的布局参数
     */
    private WindowManager.LayoutParams mWindowLayoutParams;
    private LinearLayout.LayoutParams mViewGroupLayoutParams;
    /**
     * 我们拖拽的item对应的Bitmap
     */
    private Bitmap mDragBitmap;
    Context mContext;
    /**
     * 按下的点到所在item的上边缘的距离
     */
    private int mPoint2ItemTop ; 
    
    /**
     * 按下的点到所在item的左边缘的距离
     */
    private int mPoint2ItemLeft;
    
    /**
     * DragGridView距离屏幕顶部的偏移量
     */
    private int mOffset2Top;
    
    /**
     * DragGridView距离屏幕左边的偏移量
     */
    private int mOffset2Left;
    
    /**
     * 状态栏的高度
     */
    private int mStatusHeight; 
    /**
     *  横向滚动模式
     * DragGridView自动向左边滚动的边界值
     */
    private int mLeftScrollBorder;
    
    /**
     *  横向滚动模式
     * DragGridView自动向又滚动的边界值
     */
    private int mRightScrollBorder;
    
    /**
     * 纵向滚动模式
     * DragGridView自动向下滚动的边界值
     */
    private int mDownScrollBorder;
    
    /**
     * 纵向滚动模式
     * DragGridView自动向上滚动的边界值
     */
    private int mUpScrollBorder;
    
    /**
     * DragGridView自动滚动的速度
     */
    private static final int speed = 80;
    
    /**
     * item发生变化回调的接口
     */
    private OnChanageListener onChanageListener;
    
    int mScreenWidth;
    int mScreenHeight;
    float MOVE_THRESHOLD = 100;
    int mCurrentPage = 0;
    
    public DragGridView(Context context) {
        this(context, null);
    }
    
    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mGestureDetector = new GestureDetector(context, new DrapGestureListener());
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context); //获取状态栏的高度
    }
    
    private Handler mHandler = new Handler();
    
    //用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable() {
        
        @Override
        public void run() {
            isDrag = true; //设置可以拖拽
            Log.i(TAG, "mLongClickRunnable.run() isDrag = true");
            mVibrator.vibrate(50); //震动一下
            mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item
            //绘制outline add by lilei begin
            if(SHOW_DROP_LOCATION){
                createDropLocationView();//init mDropLocationImageView
                showDropLocationView();
            }
            //add by lilei end
            //根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };
    //add by lilei begin
    private final HolographicOutlineHelper mOutlineHelper = new HolographicOutlineHelper();
    private final Rect mTempRect = new Rect();
    private Bitmap mDragOutline = null;
    //显示外框线图形
    private void createDropLocationView(){
        final Canvas canvas = new Canvas();
        mDragOutline = createDragOutline(mStartDragItemView,canvas,2);
        if(mDropLocationImageView == null)
            mDropLocationImageView = new ImageView(getContext());
        mDropLocationImageView.setImageBitmap(mDragOutline);
    }
    private void showDropLocationView(){
        if(mDropLocationImageView == null)
            createDropLocationView();
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        float x = view.getX();
        float y = view.getY();
        Log.i(TAG, "showDropLocationView view.getX():"+view.getX()+" view.getY():"+view.getY());
        mOnDragListener.onShowDropLocationView(mDropLocationImageView, (int)x, (int)y);
    }
    private void hideDropLocationView(){
        mOnDragListener.onHideDropLocationView(mDropLocationImageView);
    }
    private void swapDropLocationView(){
        hideDropLocationView();
        showDropLocationView();
    }
    /**
     * Returns a new bitmap to be used as the object outline, e.g. to visualize the drop location.
     * Responsibility for the bitmap is transferred to the caller.
     */
    private Bitmap createDragOutline(View v, Canvas canvas, int padding) {
        final int outlineColor = getResources().getColor(android.R.color.white);
        final Bitmap b = Bitmap.createBitmap(
                v.getWidth() + padding, v.getHeight() + padding, Bitmap.Config.ARGB_8888);

        canvas.setBitmap(b);
        drawDragView(v, canvas, padding, true);
        mOutlineHelper.applyMediumExpensiveOutlineWithBlur(b, canvas, outlineColor, outlineColor);
        canvas.setBitmap(null);
        return b;
    }
    private void drawDragView(View v, Canvas destCanvas, int padding, boolean pruneToDrawable) {
        final Rect clipRect = mTempRect;
        v.getDrawingRect(clipRect);

        boolean textVisible = false;

        destCanvas.save();
        destCanvas.translate(-v.getScrollX() + padding / 2, -v.getScrollY() + padding / 2);
        destCanvas.clipRect(clipRect, Op.REPLACE);
        v.draw(destCanvas);
        destCanvas.restore();
    }
    //add by lilei end
    /**
     * 设置回调接口
     * @param onChanageListener
     */
    public void setOnChangeListener(OnChanageListener onChanageListener){
        this.onChanageListener = onChanageListener;
    }
    
    /**
     * 设置响应拖拽的毫秒数，默认是1000毫秒
     * @param dragResponseMS
     */
    public void setDragResponseMS(long dragResponseMS) {
        this.dragResponseMS = dragResponseMS;
    }
    public void alert(String txt){
        Toast.makeText(getContext(), txt, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
        case MotionEvent.ACTION_DOWN:
            //使用Handler延迟dragResponseMS执行mLongClickRunnable
            Log.i(TAG, "dispatchTouchEvent>> ACTION_DOWN");
            //if(!DragHorizontalScrollView.isMove)
            //mHandler.postDelayed(mLongClickRunnable, dragResponseMS);
            
            mDownX = (int) ev.getX();
            mDownY = (int) ev.getY();
            
            //根据按下的X,Y坐标获取所点击item的position
            mDragPosition = pointToPosition(mDownX, mDownY);
            
            if(mDragPosition == AdapterView.INVALID_POSITION){
                return super.dispatchTouchEvent(ev);
            }
            
            //根据position获取该item所对应的View
            mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
            boolean is_BUTTON_SECONDARY = (ev.getButtonState() & MotionEvent.BUTTON_SECONDARY) != 0;
            Log.i(TAG, "dispatchTouchEvent>> mDragPosition:"+mDragPosition+" getFirstVisiblePosition():"
                    +getFirstVisiblePosition()+" mDownX:"+mDownX+" mDownY:"+mDownY
                    +" ev.getRawX():"+ev.getRawX()+" ev.getRawY():"+ev.getRawY()
                    +" is_BUTTON_SECONDARY:"+is_BUTTON_SECONDARY
                    +" ev.getButtonState():"+ev.getButtonState());
            /*alert("dispatchTouchEvent>> mDragPosition:"+mDragPosition+" getFirstVisiblePosition():"
                    +getFirstVisiblePosition()+" mDownX:"+mDownX+" mDownY:"+mDownY
                    +" ev.getRawX():"+ev.getRawX()+" ev.getRawY():"+ev.getRawY()
                    +" is_BUTTON_SECONDARY:"+is_BUTTON_SECONDARY
                    +" ev.getButtonState():"+ev.getButtonState()); */
            //下面这几个距离大家可以参考我的博客上面的图来理解下
            mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
            mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
            
            mOffset2Top = (int) (ev.getRawY() - mDownY);
            mOffset2Left = (int) (ev.getRawX() - mDownX);
            //add by lilei begin
            //获取DragGridView自动向左边滚动的偏移量，小于这个值，DragGridView向左滚动
            mLeftScrollBorder = mScreenWidth/8;
            //获取DragGridView自动向右边滚动的偏移量，小于这个值，DragGridView向右滚动
            mRightScrollBorder = mScreenWidth * 7/8;
            
            //add by lilei end
            //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
            mDownScrollBorder = getHeight() /4;
            //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
            mUpScrollBorder = getHeight() * 3/4;
            Log.i(TAG, "dispatchTouchEvent>>mDownScrollBorder:"+mDownScrollBorder+" mUpScrollBorder:"+mUpScrollBorder
                    +" mLeftScrollBorder:"+mLeftScrollBorder+" mRightScrollBorder:"+mRightScrollBorder
                    +" getWidth():"+getWidth()+" getHeight():"+getHeight()+" mScreenWidth:"+mScreenWidth
                    +" mScreenHeight:"+mScreenHeight);
            
            
            //开启mDragItemView绘图缓存
            mStartDragItemView.setDrawingCacheEnabled(true);
            //获取mDragItemView在缓存中的Bitmap对象
            mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
            //这一步很关键，释放绘图缓存，避免出现重复的镜像
            mStartDragItemView.destroyDrawingCache();
            
            
            break;
        case MotionEvent.ACTION_MOVE:
            int moveX = (int)ev.getX();
            int moveY = (int) ev.getY();
            
            //如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable
            if(!isTouchInItem(mStartDragItemView, moveX, moveY)){
                mHandler.removeCallbacks(mLongClickRunnable);
            }
            break;
        case MotionEvent.ACTION_UP:
            Log.i(TAG, "dispatchTouchEvent>> ACTION_UP");
            mHandler.removeCallbacks(mLongClickRunnable);
            mHandler.removeCallbacks(mScrollRunnable);
            //for test pop begin
            int upX = (int)ev.getX();
            int upY = (int) ev.getY();
            if(isTouchInItem(mStartDragItemView, upX, upY)){
                Log.i(TAG, "up this position***** isDrag:"+isDrag);
            }
            int diff = upX - mDownX;
            Log.i(TAG, "dispatchTouchEvent>> ACTION_UP upX:"+upX+" mDownX:"+mDownX
                    +" diff:"+diff+" isDrag:"+isDrag);
            if(!isDrag){
                if(diff> MOVE_THRESHOLD && mCurrentPage > 0){
                    mOnDragListener.onChangePage(mCurrentPage, mCurrentPage-1);
                    mCurrentPage--;
                }else if(diff < -MOVE_THRESHOLD && mCurrentPage< DragGridFragment.pageCount-1){
                    mOnDragListener.onChangePage(mCurrentPage, mCurrentPage+1);
                    mCurrentPage++;
                }
            }
            if(SHOW_DROP_LOCATION)
                hideDropLocationView();
            //for test pop end
            break;
        }
//        Log.i(TAG,"dispatchTouchEvent ev.getAction():"+parseAction(ev));
        boolean flag = super.dispatchTouchEvent(ev); 
        //Log.i(TAG," flag:"+flag);
        return flag;
    }

    
    /**
     * 是否点击在GridView的item上面
     * @param itemView
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(View dragView, int x, int y){
        if(dragView == null)
            return false;
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if(x < leftOffset || x > leftOffset + dragView.getWidth()){
            return false;
        }
        
        if(y < topOffset || y > topOffset + dragView.getHeight()){
            return false;
        }
        
        return true;
    }
    //for test begin
    public String parseAction(MotionEvent ev){
        String action = null;
        switch(ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
            default:
                action = ""+ev.getAction();
                break;
        }
        return action;
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //for test begin
        mGestureDetector.onTouchEvent(ev);//
        String action = parseAction(ev);
//        Log.i(TAG, "onTouchEvent 111 ev.getAction():"+action);
        //for test end 
        if(isDrag && mDragImageView != null){
            Log.i(TAG, "onTouchEvent 222");
            switch(ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getX();
                moveY = (int) ev.getY();
                //拖动item
                onDragItem(moveX, moveY);
                break;
            case MotionEvent.ACTION_UP:
                onStopDrag();
                isDrag = false;
                break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }
    
    
    /**
     * 创建拖动的镜像
     * @param bitmap 
     * @param downX
     *          按下的点相对父控件的X坐标
     * @param downY
     *          按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX , int downY){
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowLayoutParams.alpha = 0.55f; //透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
          
        mDragImageView = new ImageView(getContext());  
        mDragImageView.setImageBitmap(bitmap);  
        //mWindowManager.addView(mDragImageView, mWindowLayoutParams);
        //add by lilei begin
        //LinearLayout.LayoutParams
        Log.i(TAG, "downX:"+downX+" downY:"+downY+" mPoint2ItemLeft:"+mPoint2ItemLeft+
                " mOffset2Left:"+mOffset2Left+" mPoint2ItemTop:"+mPoint2ItemTop+" mOffset2Top:"+mOffset2Top+
                " mStatusHeight:"+mStatusHeight);
        mViewGroupLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mViewGroupLayoutParams.gravity = Gravity.TOP | Gravity.LEFT; 
        //mDragImageView.setTranslationX(downX - mPoint2ItemLeft + mOffset2Left);
        //mDragImageView.setTranslationY(downY - mPoint2ItemTop + mOffset2Top - mStatusHeight);
        int x = downX - mPoint2ItemLeft + mOffset2Left;
        int y = downY - mPoint2ItemTop;
        //((ViewGroup)this).addView(mDragImageView);
        mOnDragListener.onDragStart(mDragImageView, x, y);
        //((LinearLayout)this.getParent()).addView(mDragImageView);
        //add by lilei end
    }
    
    /**
     * 从界面上面移动拖动镜像
     */
    private void removeDragImage(){
        if(mDragImageView != null){
            //mWindowManager.removeView(mDragImageView);
            mOnDragListener.onDragEnd(mDragImageView, 0, 0);
            mDragImageView = null;
        }
    }
    
    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     * @param x
     * @param y
     */
    private void onDragItem(int moveX, int moveY){
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        //modify by lilei begin
        //mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //更新镜像的位置
        int x = moveX - mPoint2ItemLeft + mOffset2Left;
        int y = moveY - mPoint2ItemTop;
        mOnDragListener.onDragMove(mDragImageView, x, y);
        //modify by lilei end
        onSwapItem(moveX, moveY);
        
        //GridView自动滚动
        mHandler.post(mScrollRunnable);
    }
    
    
    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
     * 当moveY的值小于向下滚动的边界值，触犯GridView自动向下滚动
     * 否则不进行滚动
     */
    private Runnable mScrollRunnable = new Runnable() {
        
        @Override
        public void run() {
            if(SCROLL_VERTICAL){
                int scrollY;
                if(moveY > mUpScrollBorder){
                     scrollY = -speed;
                     mHandler.postDelayed(mScrollRunnable, 25);
                }else if(moveY < mDownScrollBorder){
                    scrollY = speed;
                     mHandler.postDelayed(mScrollRunnable, 25);
                }else{
                    scrollY = 0;
                    mHandler.removeCallbacks(mScrollRunnable);
                }
                
                //当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
                //所以我们在这里调用下onSwapItem()方法来交换item
                onSwapItem(moveX, moveY);
                
                View view = getChildAt(mDragPosition - getFirstVisiblePosition());
                //实现GridView的自动滚动
//                Log.i(TAG, "mScrollRunnable~~~~~~~ mDragPosition："+mDragPosition+" view.getTop():"+view.getTop()
//                        +" scrollY:"+scrollY+" moveY:"+moveY);
                smoothScrollToPositionFromTop(mDragPosition, view.getTop() + scrollY);
            }else{
                int scrollX;
                if(moveX > mRightScrollBorder){
                    scrollX = -speed;
                }else if(moveX < mLeftScrollBorder){
                    scrollX = speed;
                }else{
                    scrollX = 0;
                    mHandler.removeCallbacks(mScrollRunnable);
                }
                //当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
                //所以我们在这里调用下onSwapItem()方法来交换item
                onSwapItem(moveX, moveY);
                View view = getChildAt(mDragPosition - getFirstVisiblePosition());
//                Log.i(TAG, "mScrollRunnable~~~~~~~ mDragPosition："+mDragPosition+" view.getLeft():"+view.getLeft()
//                        +" scrollX:"+scrollX+" moveX:"+moveX);
                //if(scrollX < 0)
                    //smoothScrollToPosition(mDragPosition,2);
            }
        }
    };
    
    
    /**
     * 交换item,并且控制item之间的显示与隐藏效果
     * @param moveX
     * @param moveY
     */
    private void onSwapItem(int moveX, int moveY){
        //获取我们手指移动到的那个item的position
        int tempPosition = pointToPosition(moveX, moveY);
        
        //假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
        if(tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION){
            getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//拖动到了新的item,新的item隐藏掉
            getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);//之前的item显示出来
            
            if(onChanageListener != null){
                onChanageListener.onChange(mDragPosition, tempPosition);
            }
            
            mDragPosition = tempPosition;
            if(SHOW_DROP_LOCATION)
                swapDropLocationView(); //add by lilei
        }
    }
    
    
    /**
     * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag(){
        getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
        removeDragImage();
    }
    
    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        return statusHeight;
    }

    
    /**
     * 
     * @author 声明回调接口
     *
     */
    public interface OnChanageListener{
        
        /**
         * 当item交换位置的时候回调的方法，我们只需要在该方法中实现数据的交换即可
         * @param form
         *          开始的position
         * @param to 
         *          拖拽到的position
         */
        public void onChange(int form, int to);
    }
    //add by lilei begin
    OnDragListener mOnDragListener;
    public void setOnDragListener(OnDragListener onDragListener){
        mOnDragListener = onDragListener;
    }
    
    public interface OnDragListener{
        /***
         * 开始拖拽View
         * @param view
         * @param x  view 的x坐标
         * @param y  view 的y坐标
         */
        public void onDragStart(View view,int x,int y);
        public void onDragMove(View view,int x,int y);
        public void onDragEnd(View view,int x,int y);
        public void onChangePage(int from,int to);
        public void onShowDropLocationView(View locationView,int x,int y);
        public void onHideDropLocationView(View locationView);
    }
    
    //test longclick begin
    private GestureDetector mGestureDetector;
    private class DrapGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
//            Log.i(TAG,"#######DrapGestureListener.onScroll e1.getX():"+e1.getX()
//                    +" e2.getX():"+e2.getX()+" distanceX:"+distanceX);
            // TODO Auto-generated method stub
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            // TODO Auto-generated method stub
            Log.i(TAG,"#######DrapGestureListener.onFling e1.getX():"+e1.getX()
                    +" e2.getX():"+e2.getX()+" velocityX:"+velocityX);
            return true;
        }
        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(TAG,"#######DrapGestureListener.onLongPress ");
            mHandler.postDelayed(mLongClickRunnable, 0);
            super.onLongPress(e);
        }
        @Override
        public boolean onDown(MotionEvent e) {
//            Log.i(TAG,"#######DrapGestureListener.onDown ~~~");
            return true;
        }
    }

    //add by lilei end

    @Override
    public boolean onLongClick(View arg0) {
        // TODO Auto-generated method stub
        Log.i(TAG, "######### onLongClick view:"+arg0);
        return false;
    }

}
