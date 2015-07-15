package com.android.daka.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ActivityUtils {
	Context mContext;
	static final String TAG = "ActivityUtils";

	public ActivityUtils(){
	    
	}

	public ActivityUtils(Context context){
		mContext = context;
	}
	public void test(){ //for test
		final Handler mHandler = new Handler();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 try {
						final InetAddress address=InetAddress.getByName("www.baidu.com");
						Log.i(TAG, ">>lilei>>test() address:"+address.getHostAddress()+
								" currentThread:"+Thread.currentThread().getName());
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								alert(address.getHostAddress()+" currentThread:"+Thread.currentThread().getName());
							}
						});

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}).start();
		
//	    String packageName = "com.android.contacts";
//	    String className = "com.android.contacts.activities.PeopleActivity";
//	    startActivity(packageName,className);
	}
	
	public void alert(String str){
	    Log.d(TAG, ">>lilei>>alert() str:"+str);
	    Toast toast = Toast.makeText(mContext,str,Toast.LENGTH_LONG);
	    toast.show();
	}
	public void alert(){
	    Log.d(TAG, ">>lilei>>alert() ");
	    String str = "my test Reflect call ActivityUtils.alert()";
        Toast toast = Toast.makeText(mContext,str,Toast.LENGTH_LONG);
        toast.show();
    }
	void startActivity(String packageName,String className){
	        Intent[] mIntents;
	        Intent mIntent;
	        ComponentName mComponentName;
	        mComponentName = new ComponentName(packageName,className);
	        mIntent = new Intent(Intent.ACTION_MAIN);
	        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        mIntent.setComponent(mComponentName);
//	        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//	                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	        mIntents = new Intent[]{mIntent};

	        try {
	            Log.d(TAG, ">>lilei>>startActivity className:"+className);
	            mContext.startActivities(mIntents);
	        }catch (ActivityNotFoundException e) {
	            Log.d(TAG, ">>lilei>>startActivity fail !!!");
	            return;
	        }
	        Log.d(TAG, ">>lilei>>startActivity success!");
	}
	
    /***
     * judge wthether app is in system
     * @param packageName
     * @param className
     * @return
     */
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
	/***
	 * is packageName in Launcher
	 * @param packageName
	 * @return
	 */
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
	/***
	 * is activityName in Launcher
	 * @param activityName
	 * @return
	 */
	public boolean isActivityInLauncher(String activityName){
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
	/***
	 * is packageName and className in Launcher
	 * @param packageName
	 * @param className
	 * @return
	 */
	public boolean isInLauncher(String packageName,String className){
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
	/***
	 * get Running App Processes
	 */
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
