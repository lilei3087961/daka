package com.android.daka.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PackageUtils {
    public static void install(Context context,String path) {    
        // 核心是下面几句代码  
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),  
                "application/vnd.android.package-archive");  
        context.startActivity(intent);  
    }
}
