package com.android.daka.net;

import com.android.daka.Config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtils {
    Context mContext;
    static final String TAG = Config.TAG_APP +"NetUtils";
    public NetUtils(Context context){
        mContext = context;
    }
    /**
     * 检查当前网络是否可用
     * 
     * @param context
     * @return
     */
    
    public boolean isNetworkAvailable()
    {
        //Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        
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
                    Log.i(TAG, i + "===状态===" + networkInfo[i].getState());
                    Log.i(TAG, i + "===类型===" + networkInfo[i].getTypeName());
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
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
