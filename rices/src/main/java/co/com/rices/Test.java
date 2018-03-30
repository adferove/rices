package co.com.rices;

import java.security.SecureRandom;

public class Test {
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static SecureRandom rnd = new SecureRandom();

	static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}

	public static void main(String[] args) {

		String code = Test.randomString(7);
		System.out.println(code);
//	    String[] ids = TimeZone.getAvailableIDs();
//	    for (String id : ids) {
//	        System.out.println(displayTimeZone(TimeZone.getTimeZone(id)));
//	    }
//
//	    System.out.println("\nTotal TimeZone ID " + ids.length);

	}

//	private static String displayTimeZone(TimeZone tz) {
//
//	    long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
//	    long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
//	                              - TimeUnit.HOURS.toMinutes(hours);
//	    // avoid -4:-30 issue
//	    minutes = Math.abs(minutes);
//
//	    String result = "";
//	    if (hours > 0) {
//	        result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
//	    } else {
//	        result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
//	    }
//
//	    return result;
//
//	}
}
