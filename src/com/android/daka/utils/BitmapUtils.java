package com.android.daka.utils;

import com.android.daka.Config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class BitmapUtils {
    final static String TAG = Config.TAG_APP+"BitmapUtils";
    public static Bitmap resizeBitmap(Bitmap bitmap,int newWidth,int newHeight){
        Bitmap BitmapOrg = bitmap;    
        int width = BitmapOrg.getWidth();    
        int height = BitmapOrg.getHeight();     
        Log.i(TAG, " orginwidth:"+width+" originheight:"+height);
        Log.i(TAG, " newWidth:"+newWidth+" newHeight:"+newHeight);
        float scaleWidth = ((float) newWidth) / width;    
        float scaleHeight = ((float) newHeight) / height;    
  
        Matrix matrix = new Matrix();    
        matrix.postScale(scaleWidth, scaleHeight);    
        // if you want to rotate the Bitmap     
        // matrix.postRotate(45);     
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,    
                        height, matrix, true);
        return resizedBitmap;
    }
}
