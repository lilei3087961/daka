package com.android.daka.net;

import com.android.daka.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {
    static final String TAG = Config.TAG_APP +"NetworkReceiver";
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        if(arg1.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            //Settings.Global.putInt(arg0.getContentResolver(), Settings.Global.AUTO_TIME,1);
            int auto_time = 0;
            try {
                auto_time = Settings.Global.getInt(arg0.getContentResolver(), Settings.Global.AUTO_TIME);
            } catch (SettingNotFoundException e) {
                // TODO Auto-generated catch block
                Log.i(TAG, "error: "+e.toString());
                e.printStackTrace();
            }
            NetUtils mNetUtils = new NetUtils(arg0);
            if(isNetworkAvailable(arg0)){
                //Settings.Global.putInt(arg0.getContentResolver(), Settings.Global.AUTO_TIME,1);
                Log.i(TAG, "00000  ");
            }
            Log.i(TAG, "11111111  auto_time："+auto_time);

        }else if(arg1.getAction().equals("com.launcher.action")){
            Log.i(TAG,"222 noManifest onReceive action:"+arg1.getAction());
        }
    }
    public boolean isNetworkAvailable(Context context)
    {
        //Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    Log.i(TAG, i + "===类型===" + networkInfo[i].getTypeName()+"===状态===" + networkInfo[i].getState());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        Log.i(TAG,"network available");
                        return true;
                    }
                }
            }
        }
        Log.i(TAG,"network not available");
        return false;
    }

}
