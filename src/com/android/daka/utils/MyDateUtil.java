package com.android.daka.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtil {
	/***
	 * 
	 * @param strDateBefore ex:"2014-1-1 9:05:05"
	 * @param strDateAfter  ex:"2014-1-1 17:05:05"
	 * @return return formate "hh:mm:ss"
	 */
	public static String getDiffTime(String strDateBefore,String strDateAfter){
		String strDiffTime = "error";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
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
}
