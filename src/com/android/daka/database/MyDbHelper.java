package com.android.daka.database;

import com.android.daka.utils.DateUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/***
 * 创建ｄｂ的一些动作
 * @author lilei
 *
 */
public class MyDbHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "daka_db";
	private static final int DATABASE_VERSION = 3;
	private static final String TAG = "MyDbHelper";
	private static MyDbHelper mInstance;
	
	public MyDbHelper(Context context){
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}
	
	public static synchronized MyDbHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new MyDbHelper(context);
		}
		return mInstance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		  Log.i(TAG, "<<lilei<<onCreate<<");
		  String sql="CREATE TABLE IF NOT EXISTS "+Tables.DakaInfo.TABLE_NAME+"("
		  		+ Tables.DakaInfo.COL_ID+" integer primary key autoincrement,"
		  		+ Tables.DakaInfo.COL_NAME+" text,"
		  		+ Tables.DakaInfo.COL_ON_WORK_TIME+" text,"
		  		+ Tables.DakaInfo.COL_OFF_WORK_TIME+" text,"
		  		+ Tables.DakaInfo.COL_OPERATE_DATE+" text)";
		  arg0.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.i(TAG, "<<lilei<<onUpgrade<<");
		String sql=" DROP TABLE IF EXISTS daka_info";  
		arg0.execSQL(sql);
        onCreate(arg0);  
	}
	public Cursor queryRaw(String strQuery){
			Log.i(TAG, "<<lilei<<queryRaw<<strQuery");
			SQLiteDatabase db=this.getWritableDatabase();
			Cursor cursor = db.rawQuery(strQuery, null);
			return cursor;
	}
	public long insert(String tableName,ContentValues cv){
		Log.i(TAG, "<<lilei<<insert table:"+tableName+" valeus:"+cv.toString());
		if(tableName.equals(Tables.DakaInfo.TABLE_NAME)){
			SQLiteDatabase db=this.getWritableDatabase();
			long row=db.insert(Tables.DakaInfo.TABLE_NAME, null, cv);  
	        return row; 
		}
		return -1;
	}
	public long delete(String tableName,String whereClause,String[] whereArgs){
		Log.i(TAG, "<<lilei<<delete table:"+tableName+" where:"+whereClause
				+" whereArgs:"+whereArgs);
		if(tableName.equals(Tables.DakaInfo.TABLE_NAME)){
			SQLiteDatabase db=this.getWritableDatabase();
			long row=db.delete(Tables.DakaInfo.TABLE_NAME, whereClause, whereArgs);
	        return row;
		}
		return -1;
	}
	public long update(String tableName,ContentValues cv,
			String whereClause,String[] whereArgs){
		Log.i(TAG, "<<lilei<<update table:"+tableName+" valeus:"+cv.toString()+
				" where:"+whereClause+" whereArgs:"+whereArgs);
		if(tableName.equals(Tables.DakaInfo.TABLE_NAME)){
			SQLiteDatabase db=this.getWritableDatabase();
			long row=db.update(Tables.DakaInfo.TABLE_NAME, cv, 
					whereClause, whereArgs); 
	        return row; 
		}
		return -1;
	}
	// #######################################
	//add one on work info to db
	public void addOnWork(){
		String onWorkTime = DateUtil.getCurrentDateTime("yyyy-mm-dd HH:mm:ss");
		String operateDate = DateUtil.getCurrentDateTime("yyyy-mm-dd");
		ContentValues cv = new ContentValues();
		cv.put(Tables.DakaInfo.COL_ON_WORK_TIME, onWorkTime);
		cv.put(Tables.DakaInfo.COL_OPERATE_DATE, operateDate);
		long count = insert(Tables.DakaInfo.TABLE_NAME,cv);
		if(count <= 0){
			Log.i(TAG, "<<lilei<<addOnWork insert 0 row! ");
		}
	}
	public void addOffWork(){
		String offWorkTime = DateUtil.getCurrentDateTime("yyyy-mm-dd HH:mm:ss");
		String operateDate = DateUtil.getCurrentDateTime("yyyy-mm-dd");
		String where = " where "+Tables.DakaInfo.COL_OPERATE_DATE
				+" = '"+operateDate+"'";
		ContentValues cv = new ContentValues();
		cv.put(Tables.DakaInfo.COL_OFF_WORK_TIME, offWorkTime);
		long count = update(Tables.DakaInfo.TABLE_NAME,cv,where,null);
		if(count <= 0){
			Log.i(TAG, "<<lilei<<addOffWork update 0 row! ");
		}
	}
	public Cursor getDakaInfo(){
		String strQuery = "select * from "+Tables.DakaInfo.TABLE_NAME
				+" ORDER BY "+Tables.DakaInfo.COL_ID+" desc";
		Cursor cursor = queryRaw(strQuery);
		return cursor;
	}
	
}
