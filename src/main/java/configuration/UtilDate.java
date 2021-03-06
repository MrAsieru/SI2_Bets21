package configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class UtilDate {
	
	private UtilDate() {}
	
	public static Date trim(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	public static Date newDate(int year,int month,int day) {
	     Calendar calendar = Calendar.getInstance();
		 calendar.setTimeZone(TimeZone.getTimeZone("CET"));
	     calendar.set(year, month, day,0,0,0);
	     calendar.set(Calendar.MILLISECOND, 0);
	     Logger.getLogger(UtilDate.class.getName()).info("newDate: "+calendar.getTime());
	     return calendar.getTime();
	}
	
	public static Date firstDayMonth(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	
	public static Date lastDayMonth(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();

	}
	
	public static String getString(Date date) {
		int ind1 = date.toString().length();
		int ind2 = ind1-4;
		return date.toString().substring(ind2,ind1)+"/"+date.toString().substring(4,7)+"/"+date.toString().substring(8,10);
	}
	
}
