package co.com.rices;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class Test {

	//	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	//	static SecureRandom rnd = new SecureRandom();
	//
	//	static String randomString( int len ){
	//	   StringBuilder sb = new StringBuilder( len );
	//	   for( int i = 0; i < len; i++ ) 
	//	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	//	   return sb.toString();
	//	}

	//	public static void main(String[] args) {
	//		
	////		String code = Test.randomString(7);
	////		System.out.println(code);
	////	    String[] ids = TimeZone.getAvailableIDs();
	////	    for (String id : ids) {
	////	        System.out.println(displayTimeZone(TimeZone.getTimeZone(id)));
	////	    }
	////
	////	    System.out.println("\nTotal TimeZone ID " + ids.length);
	//
	//	}

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

//	public static void main( String[] args )
//    {
//    	BufferedImage image = null;
//        try {
//          
//            URL url = new URL("http://www.ricestogo.com/rices/images/logo.png");
//            image = ImageIO.read(url);
//            
////            ImageIO.write(image, "jpg",new File("C:\\Web\\out.jpg"));
////            ImageIO.write(image, "gif",new File("C:\\out.gif"));
//            ImageIO.write(image, "png",new File("C:\\Web\\out.png"));
//            
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
//        System.out.println("Done");
//    }
	
//	public static void main(String[] args) {
//
//		try {
//
//			byte[] imageInByte;
//			BufferedImage originalImage = ImageIO.read(new File("c:/Web/out.png"));
//
//			// convert BufferedImage to byte array
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(originalImage, "jpg", baos);
//			baos.flush();
//			imageInByte = baos.toByteArray();
//			baos.close();
//
//			// convert byte array back to BufferedImage
//			InputStream in = new ByteArrayInputStream(imageInByte);
//			BufferedImage bImageFromConvert = ImageIO.read(in);
//
//			ImageIO.write(bImageFromConvert, "png", new File("c:/Web/new-darksouls.png"));
//
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
	public static void main(String[] args) {
		String cadena = "Nulla sodales ut tellus blandit accumsan. Aliquam erat volutpat. Morbi quis vestibulum erat. Nam malesuada lobortis tempus. Fusce fermentum libero fringilla odio pharetra malesuada. Suspendisse potenti. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nullam ultrices lectus quis consequat fringilla. Mauris non ex et purus sollicitudin tempus vitae quis nisi.";
		System.out.println(cadena.length());
	}
}
