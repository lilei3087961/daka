package com.android.daka.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";//MM month; mm minutes;
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/***
	 * 
	 * @param strDateBefore ex:"2014-1-1 9:05:05"
	 * @param strDateAfter  ex:"2014-1-1 17:05:05"
	 * @return return formate "hh:mm:ss"
	 */
	public static String getDiffTime(String strDateBefore,String strDateAfter){
		String strDiffTime = "error";
		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
			Date dateOn = sdf.parse(strDateBefore);
			Date dateOff = sdf.parse(strDateAfter);
			long diffMills = dateOff.getTime() - dateOn.getTime();
			int hours = (int) (diffMills/3600000);
			int minutes = (int) ((diffMills-hours*3600000)/60000);
			int seconds = (int) ((diffMills-hours*3600000-minutes*60000)/1000);
			strDiffTime=(hours>9?""+hours:("0"+hours))+":"
					+(minutes>9?""+minutes:("0"+minutes))+":"
					+(seconds>9?""+seconds:("0"+seconds));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strDiffTime;
	}
	/***
	 * get current datetime string with specified formate 
	 * @param strFormat ex:yyyy-mm-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentDateTime(String strFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		Date dataNow = new Date(System.currentTimeMillis());
		String str = sdf.format(dataNow);
		return str;
	}
	public static String getCurrentMonth1DateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar calenadar = Calendar.getInstance();
		calenadar.setTimeInMillis(System.currentTimeMillis());
		calenadar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDayOfMonth = sdf.format(calenadar.getTime());
		return firstDayOfMonth;
	}
}
