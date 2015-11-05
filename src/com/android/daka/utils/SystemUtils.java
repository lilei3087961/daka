package com.android.daka.utils;

import com.android.daka.Config;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class SystemUtils {
    static final String TAG = Config.TAG_APP+"SystemUtils";
    static Context mContext;
    static float sScreenDensity = -1;
    public static void initContext(Context context){
        mContext = context;
    }
    public static String getDisplayMetrics(Context cx) {
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        str += "The absolute width:" + String.valueOf(screenWidth) + "pixels\n";
        str += "The absolute heightin:" + String.valueOf(screenHeight)
                        + "pixels\n";
        str += "The logical density of the display.:" + String.valueOf(density)
                        + "\n";
        str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
        Log.i(TAG, "str is:"+str);
        return str;
}
    public static float getScreenDensity() {
        if(sScreenDensity == -1)
            sScreenDensity = mContext.getResources().getDisplayMetrics().density;
        return sScreenDensity;
    }

    
    
}
