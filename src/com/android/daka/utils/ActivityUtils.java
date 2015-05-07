package com.android.daka.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class ActivityUtils {
	Context mContext;
	static final String TAG = "adam";
	public ActivityUtils(Context context){
		mContext = context;
	}
	public boolean isInLauncher(String activityName){
	   final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
       mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
       final PackageManager packageManager = mContext.getPackageManager();
       List<ResolveInfo> mApps = packageManager.queryIntentActivities(mainIntent, 0);
       if (mApps == null) {
           return false;
       }
       for(int i=0;i<mApps.size();i++){
       		String strActivity = mApps.get(i).activityInfo.name;
       		Log.i(TAG, "isInLauncher strActivity_"+i+":"+strActivity);
       		if(activityName.equals(strActivity)){
       			Log.i(TAG, "isInLauncher !!==== strActivity:"+strActivity);
       		}
       }
       return false;
   }
	public boolean isValidActivity(String packageName,String className){
	    Intent intent = new Intent();
	    intent.setClassName(packageName, className);

	    ResolveInfo component =mContext.getPackageManager().resolveActivity(intent, 0);
	    if(component != null) {  
	        Log.i(TAG,"valid Activity() 1111 component:"+component);
	        return true;
	    }else{
	        Log.i(TAG,"invalid Activity 222");
	        return false;
	    }
	}
	public boolean isPackageNameInLauncher(String packageName){
		   final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
	       mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	       final PackageManager packageManager = mContext.getPackageManager();
	       List<ResolveInfo> mApps = packageManager.queryIntentActivities(mainIntent, 0);
	       if (mApps == null) {
	           return false;
	       }
	       for(int i=0;i<mApps.size();i++){
	       		String strPackageName = mApps.get(i).activityInfo.packageName;
	       		//Log.i(TAG, "isInLauncher strPackageName_"+i+":"+strPackageName);
	       		if(packageName.equals(strPackageName)){
	       			//Log.i(TAG, "isInLauncher !!==== strActivity:"+strPackageName);
	       			return true;
	       		}
	       }
	       return false;
	   }
	public boolean isInLauncher2(String packageName,String className){
		   final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
	       mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	       mainIntent.setClassName(packageName, className);
	       final PackageManager packageManager = mContext.getPackageManager();
	       List<ResolveInfo> mApps = packageManager.queryIntentActivities(mainIntent, 0);
	       if (mApps == null) {
	           return false;
	       }
	       Log.i(TAG, "<<isInLauncher isInLauncher:"+(mApps.size()>0));
	       for(int i=0;i<mApps.size();i++){
	       		String strActivity = mApps.get(i).activityInfo.name;
	       		Log.i(TAG, "isInLauncher strActivity_"+i+":"+strActivity);
	       		if(className.equals(strActivity)){
	       			Log.i(TAG, "isInLauncher !!==== strActivity:"+strActivity);
	       		}
	       }
	       return mApps.size() > 0;
	}
	public void getRunningAppProcesses(){
	    ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
	    for(RunningAppProcessInfo rapi : infos){
	    	if(isPackageNameInLauncher(rapi.processName))
	    		Log.d(TAG, ">>lilei>>in packageName:"+rapi.processName);
	    	else{
	    		Log.d(TAG, ">>lilei>>not in packageName:"+rapi.processName);
	    	}
	    }

    }
}
