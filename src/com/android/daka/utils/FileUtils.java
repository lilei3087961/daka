package com.android.daka.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.android.daka.Config;
import com.android.daka.net.ShellUtils2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;

public class FileUtils {
    static final String TAG = Config.TAG_APP+"FileUtils";
  public static String sdPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
  public static boolean fileExist(String path){
      File dir =new File(sdPath);
      Log.i(TAG, "fileExist sdPath:"+sdPath+" >>sdPath exists:"+dir.exists());
      File file =new File(path);
      return file.exists();
  }
  /**
   * read file to string list, a element of list is a line
   * 
   * @param filePath
   * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
   * @return if file not exist, return null, else return content of file
   * @throws RuntimeException if an error occurs while operator BufferedReader
   */
  public static List<String> readFileToList(String filePath, String charsetName) {
      File file = new File(filePath);
      List<String> fileContent = new ArrayList<String>();
      if (file == null || !file.isFile()) {
          return null;
      }

      BufferedReader reader = null;
      try {
          InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
          reader = new BufferedReader(is);
          String line = null;
          while ((line = reader.readLine()) != null) {
              fileContent.add(line);
          }
          reader.close();
          return fileContent;
      } catch (IOException e) {
          throw new RuntimeException("IOException occurred. ", e);
      } finally {
          if (reader != null) {
              try {
                  reader.close();
              } catch (IOException e) {
                  throw new RuntimeException("IOException occurred. ", e);
              }
          }
      }
  }

  public static ArrayList<File> getFilesToList(String path) {
      ArrayList<File> fileList = new ArrayList<File>();
      File[] allFiles = new File(path).listFiles();

      for (int i = 0; i < allFiles.length; i++) {  
          File file = allFiles[i];  
          if (file.isFile()) {  
              fileList.add(file);  
          }
      }
      return fileList;
  }  
  public static int copyDirFiles(String fromPath, String toPath){
          File[] currentFiles;
          File root = new File(fromPath);
          currentFiles = root.listFiles();
          int count = 0;
          File targetDir = new File(toPath);
          //check whether targetDir exists
          if(!targetDir.exists()){
              Log.e(TAG, ">>lileip>>"+toPath+" not exists!!");
          }
          for(int i= 0;i<currentFiles.length;i++){
              if(currentFiles[i].isFile()){
                  Log.e(TAG, ">>lileip>>copy file:"+currentFiles[i].getPath());
                  copyFile(currentFiles[i].getPath(), toPath + currentFiles[i].getName());
                  count++;
              }
          }
          return count;
  }
  /***
   *   复制文件到指定路径
   *   //要复制的目录下的所有非子目录(文件夹)文件拷贝
   * @param fromFile
   * @param toFile
   * @return
   */
  public static int copyFile(String fromFile, String toFile)
  {
      try{
          InputStream fosfrom = new FileInputStream(fromFile);
          OutputStream fosto = new FileOutputStream(toFile);
          byte bt[] = new byte[1024];
          int c;
          while ((c = fosfrom.read(bt)) > 0){
              fosto.write(bt, 0, c);
          }
          fosfrom.close();
          fosto.close();
          return 0;
           
      } catch (Exception ex){
          Log.e(TAG,"CopyFile error:"+ex.toString());
          return -1;
      }
  }
  /**
   * 静默安装功能实现
   * @param context
   */
  public static void backInstall(){
      ArrayList<File> fileList = getFilesToList("system/vendor/preinstall/");
      Log.i(TAG, ">>lileip>>testBackInstall() fileList.size():"+fileList.size());
      String cmd = "";
      for(int i=0;i<fileList.size();i++){
          File file = fileList.get(i);
          Log.i(TAG, ">>lileip>>test() i:"+i+" file:"+file.getPath());
          cmd = "pm install "+file.getPath();
          String[] cmds = new String[]{cmd};
          ShellUtils2.execCommand(cmds, false);
          //PackageUtils.install(context,file.getPath());
      }
      Log.i(TAG, "test() end copy");
  }
}
