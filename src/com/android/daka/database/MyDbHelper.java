package com.android.daka.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/***
 * 创建ｄｂ的一些动作
 * @author lilei
 *
 */
public class MyDbHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "daka_db";
	private final static int DATABASE_VERSION = 1;

	public MyDbHelper(Context context){
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		  String sql="CREAT TABLE IF NOT EXISTS "+Tables.DakaInfo.TABLE_NAME+"("
		  		+ Tables.DakaInfo.COL_ID+" integer primary key autoincrement,"
		  		+ Tables.DakaInfo.COL_NAME+" text,"
		  		+ Tables.DakaInfo.COL_ON_WORK_TIME+" text,"
		  		+ Tables.DakaInfo.COL_OFF_WORK_TIME+" text";  
		  arg0.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String sql=" DROP TABLE IF EXISTS daka_info";  
		arg0.execSQL(sql);
        onCreate(arg0);  
	}
	
	public long insert(String tableName,ContentValues cv){
		if(tableName.equals(Tables.DakaInfo.TABLE_NAME)){
			SQLiteDatabase db=this.getWritableDatabase();
			long row=db.insert(Tables.DakaInfo.TABLE_NAME, null, cv);  
	        return row; 
		}
		return -1;
	}
	
}
