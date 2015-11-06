package com.android.daka.launcher;

import android.app.Application;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Handler;


import java.lang.ref.WeakReference;

import android.util.Log;


/**  Hypervisor application */
public class LauncherApplication extends Application {

    public ApplicationManager mModel;
    public IconCache mIconCache;
    private long  currentPlaytime;
    static final String TAG = ">>lilei>>LauncherApplication";
    @Override
    public void onCreate() {
        super.onCreate();
   
        Log.v(TAG, ">>>>>onCreate()>>>>");
   
        mIconCache = new IconCache(this);
        mModel = new ApplicationManager(this, mIconCache);
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        registerReceiver(mModel, filter);
/*        filter = new IntentFilter();
        filter.addAction(SearchManager.INTENT_GLOBAL_SEARCH_ACTIVITY_CHANGED);
        registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(SearchManager.INTENT_ACTION_SEARCHABLES_CHANGED);
        registerReceiver(mModel, filter);

*/
        mModel.startLoader(); 



   }
    /**
     * There's no guarantee that this function is ever called.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        
        Log.v(TAG, ">>>>>onTerminate()>>>>");
        if(mModel != null)
           unregisterReceiver(mModel);

    }


    public IconCache getIconCache() {
        return mIconCache;
    }

    ApplicationManager getModel() {
    	return mModel;
    }

    public void setPlaytime(long time){
        currentPlaytime = time; 
    }
    
    public long getPlaytime(){
        return currentPlaytime;
    }
    public void cleartime(){
        currentPlaytime = -1 ; 
    }
}
