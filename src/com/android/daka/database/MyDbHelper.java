package com.android.daka.database;

import com.android.daka.Config;
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
	static final String TAG = Config.TAG_APP+"MyDbHelper";
	private static MyDbHelper mInstance;
	
	public MyDbHelper(Context context){
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}
	
	public static synchronized MyDbHelper getInstance(Context context){
	    Log.i(TAG, "getInstance context:"+context+" mInstance:"+mInstance);
		if(mInstance == null){
			mInstance = new MyDbHelper(context);
		}
		return mInstance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		  Log.i(TAG, "onCreate");
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
		Log.i(TAG, "onUpgrade");
		String sql=" DROP TABLE IF EXISTS daka_info";  
		arg0.execSQL(sql);
        onCreate(arg0);  
	}
	public Cursor queryRaw(String strQuery){
			Log.i(TAG, "queryRaw()>>strQuery:"+strQuery);
			SQLiteDatabase db=this.getWritableDatabase();
			Cursor cursor = db.rawQuery(strQuery, null);
			return cursor;
	}
	public long insert(String tableName,ContentValues cv){
		Log.i(TAG, "insert table:"+tableName+" valeus:"+cv.toString());
		if(tableName.equals(Tables.DakaInfo.TABLE_NAME)){
			SQLiteDatabase db=this.getWritableDatabase();
			long row=db.insert(Tables.DakaInfo.TABLE_NAME, null, cv);  
	        return row; 
		}
		return -1;
	}
	public long delete(String tableName,String whereClause,String[] whereArgs){
		Log.i(TAG, "delete table:"+tableName+" where:"+whereClause
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
		Log.i(TAG, "update table:"+tableName+" valeus:"+cv.toString()+
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
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public boolean addOnWork(){
		String onWorkTime = DateUtil.getCurrentDateTime(DATE_TIME_FORMAT);
		String operateDate = DateUtil.getCurrentDateTime(DATE_FORMAT);
		if(operateDateInDb(operateDate)){
		    return false;
		}
		ContentValues cv = new ContentValues();
		cv.put(Tables.DakaInfo.COL_ON_WORK_TIME, onWorkTime);
		cv.put(Tables.DakaInfo.COL_OPERATE_DATE, operateDate);
		long insertNum = insert(Tables.DakaInfo.TABLE_NAME,cv);
		Log.i(TAG, "addOnWork() insertNum is:"+insertNum);
		if(insertNum <= 0){
			Log.i(TAG, "addOnWork insert 0 row! ");
		}
		return true;
	}
	public boolean addOffWork(){
		String offWorkTime = DateUtil.getCurrentDateTime(DATE_TIME_FORMAT);
		String operateDate = DateUtil.getCurrentDateTime(DATE_FORMAT);
		if(!operateDateInDb(operateDate)){
		    return false;
		}
		String where = Tables.DakaInfo.COL_OPERATE_DATE
				+" = '"+operateDate+"'";
		ContentValues cv = new ContentValues();
		cv.put(Tables.DakaInfo.COL_OFF_WORK_TIME, offWorkTime);
		long updateNum = update(Tables.DakaInfo.TABLE_NAME,cv,where,null);
		Log.i(TAG, "addOffWork updateNum is:"+updateNum);
		if(updateNum <= 0){
			Log.i(TAG, "addOffWork update 0 row! ");
		}
		return true;
	}
	public boolean operateDateInDb(String operateDate){
	    Cursor cursor = getDakaInfoByOperateDate(operateDate);
        int count = cursor.getCount();
        cursor.close();
        Log.i(TAG, "addOffWork()  operateDate:"+operateDate
                +" account count is:"+count);
        if(count <=0){
            return false;
        }else{
            Log.i(TAG, "isOpDateAdded()  operateDate:"
                    +operateDate+" record count is:"+count);
            return true;
        }
	}
	public Cursor getDakaInfoByOperateDate(String operateDate){
	    String strQuery = "select * from "+Tables.DakaInfo.TABLE_NAME
	            +" where "+Tables.DakaInfo.COL_OPERATE_DATE
	            +" = '"+operateDate+"'"
                +" ORDER BY "+Tables.DakaInfo.COL_ID+" desc";
        Cursor cursor = queryRaw(strQuery);
        return cursor;
	}
	public Cursor getDakaIfoByRangeDate(String startDate,String endDate){
	    
	    return null;
	}
	public Cursor getDakaInfo(){
		String strQuery = "select * from "+Tables.DakaInfo.TABLE_NAME
				+" ORDER BY "+Tables.DakaInfo.COL_ID+" desc";
		Cursor cursor = queryRaw(strQuery);
		return cursor;
	}
	
}
