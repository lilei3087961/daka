package com.android.daka.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.android.daka.Config;

import android.os.Environment;
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
}
