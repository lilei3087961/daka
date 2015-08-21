package com.android.daka.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.android.daka.Config;
import com.android.daka.net.ShellUtils2;

import android.util.Log;

public class ShellUtils{
    static final String TAG = Config.TAG_APP+"ShellUtils";
    public static final String COMMAND_SU       = "su";  //if root use this
    public static final String COMMAND_SH       = "sh";  //if no root use this
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    public static void test(){
        String cmd = "/system/bin/ping -c 1 baidu.com";
        String cmd1 = "rm -rf \\data\\";  //可以执行
        String cmd2 = "am start -n com.android.dialer/com.android.dialer.DialtactsActivity"; //
        String cmd3 = "echo -n \"\\x1B\\x40\"";  //> /dev/lp0
        String cmd4 = "/data/usb_printer_selftest.sh";  ///mnt/sdcard
        String[] cmds = new String[]{cmd};//,"echo $0"
        long startTime = System.nanoTime(); 
        ShellUtils2.execCommand(cmds, false);
        long consumingTime = System.nanoTime() - startTime; //消耗時間
        Log.i(TAG,(consumingTime/1000)+" 微秒");
        //execCommand(cmd1,false);
    }
    public static void execCommand(String command,boolean isFile){
        Process process = null;
        DataOutputStream os = null;
        List<String> lines = null;
        try {
            Log.i(TAG, "execCommand() command is>>"+command);
            if(isFile && !FileUtils.fileExist(command)){
                Log.i(TAG, "execCommand() file not exists");
                return;
            }

            process = Runtime.getRuntime().exec(COMMAND_SH);
            InputStream inputstream = process.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(
                    inputstream);
            BufferedReader bufferedreader = new BufferedReader(
                    inputstreamreader);
 
            os = new DataOutputStream(process.getOutputStream());
            if(isFile){
                lines = FileUtils.readFileToList(command, "utf-8");
                for(int i=0;i<lines.size();i++){
                    String line = lines.get(i);
                    boolean isComment = line.startsWith("#");
                    Log.i(TAG, "is Comment:"+isComment+" write file line>>"+line);
                    if(!isComment){
                        os.writeBytes(line);
                        os.writeBytes(COMMAND_LINE_END);
                    }
                }
            }else{
                os.writeBytes(command);
                os.writeBytes(COMMAND_LINE_END);
            }
//            os.writeBytes("ls \n"); //for ping command
            os.writeBytes(COMMAND_EXIT);
            os.flush();
            process.waitFor();
            String line = "";
 
            while ((line = bufferedreader.readLine()) != null) {
                Log.i(TAG, "read shell line>>"+line);
            }
            Log.i(TAG, " "+process.exitValue());

        } catch (Exception e) {
            Log.i(TAG,"execCommand() error:"+e.toString());
        }
    }
}
