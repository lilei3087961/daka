package com.android.daka.utils;

import com.android.daka.Config;

import android.util.Log;

public class SortUtils {
    final String TAG = Config.TAG_APP+"SortUtils";
    String temp="";
    private static SortUtils mSortUtils;
    public static SortUtils getInstance(){
        if(mSortUtils == null){
            mSortUtils = new SortUtils();
        }
        return mSortUtils;
    }
    public void test(){
        int a[] = {9,3,1,7,9,10};
        Log.i(TAG, "quiSort ~~~");
        quiSort(a,0,a.length-1);
        
        printArray(a);
    }

    public void bobSort(int a[]){
        int len =  a.length;
        for(int i=len-1;i>0;i--){ //设置每次冒泡的位置
            for(int j=0;j<i;j++){ //从0到每次冒泡前的位置
                if(a[j]>a[j+1]){
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
        }
    }
    
    public void selSort(int a[]){
        int len =  a.length;
        for(int i=0;i<len;i++){   //每次开始选择的位置
            int minIndex = i;
            for(int j=i+1;j<len;j++){  //设置开始选择之后的范围
                if(a[minIndex]>a[j]){
                    minIndex = j;
                }
            }
            if(minIndex != i){
                int temp = a[minIndex];
                a[minIndex] = a[i];
                a[i] = temp;
            }
        }
    }
    
    public void insSort(int a[]){
        int len =  a.length;
        for(int i=1;i<len;i++){  //设置每次开始插入的位置
            for(int j=i;j>0;j--){ //设置开始插入之前的范围
                if(a[j]<a[j-1]){
                    int temp = a[j];
                    a[j] = a[j-1];
                    a[j-1] = temp;
                }
            }
        }
    }

    public void quiSort(int a[],int low,int high){
        int l = low;
        int h = high;
        int pivot = a[low];
        if(l == h)
            return;
        while(l<h){
            while(a[h]>=pivot && l<h){
                h--;
            }
            if(l<h){  //找到右边小于中间值的的记录交互
                int temp = a[h];
                a[h] = a[l];
                a[l] = temp;
                l++;
            }
            while(a[l]<=pivot && l<h){
                l++;
            }
            if(l<h){ //找到左边找到大于中间值的的记录交互
                int temp = a[h];
                a[h] = a[l];
                a[l] = temp;
                h--;
            }
        }
        Log.i(TAG, "quiSort,l:"+l+" h:"+h);
        printArray(a);
        if(l>low)
            quiSort(a,low,l-1);
        if(h<high)
            quiSort(a,l+1,high);
    }
    void printArray(int a[]){
        temp="";
        for(int i=0;i<a.length;i++){
            temp += a[i]+",";
        }
        Log.i(TAG, "result:"+temp);
    }
}
