package com.android.daka.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class DragHorizontalScrollView extends HorizontalScrollView {
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            isMove = true;
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
            isMove = false;
        }
        Log.i(TAG,"***#### onTouchEvent return false action:"+parseAction(ev)+" isMove:"+isMove);
        return false;
    }
    @Override
    public void fling(int velocityX) {
        // TODO Auto-generated method stub
        //Log.i(TAG,"**** fling() velocityX:"+velocityX);
        super.fling(velocityX);
    }
    /**
     * 是否滚动
     */
    public static boolean isMove = false;
    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        //Log.i(TAG,"**** computeScroll()");
        super.computeScroll();
    }
    public DragHorizontalScrollView(Context context){
        this(context,null);
    }
    public DragHorizontalScrollView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public DragHorizontalScrollView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    final String TAG = ">>lilei>>DragHorizontalScrollView";
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
        }
        return action;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

//        Log.i(TAG,"**** onInterceptTouchEvent return false action:"+parseAction(ev)+
//                " DragGridView.isDrag:"+DragGridView.isDrag);
//        return false;
        
        //return super.onInterceptTouchEvent(ev);
//        Log.i(TAG,"**** onInterceptTouchEvent isDrag:"+DragGridView.isDrag+" action:"+ev.getAction()
//                +" isMove:"+isMove);
        if(DragGridView.HORIZONTAL_LONG){
            Log.i(TAG,"**** onInterceptTouchEvent 111");
            if(DragGridView.isDrag){  //返回false 事件只分发给子布局
               return false;
            }else{
                return super.onInterceptTouchEvent(ev); ////返回super 前面子布局和父布局都获取down事件后return true,只有父布局获取相关事件
            }
        }else {
            Log.i(TAG,"**** onInterceptTouchEvent 222");
            return false;
        }  
    }



}
