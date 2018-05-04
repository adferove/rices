package co.com.rices;

import org.apache.log4j.Logger;

public interface IConstants {
	Logger	log			= Logger.getLogger("rices");
	String	ACTIVO		= "A";
	String	INACTIVO	= "I";
	Integer ROL_ADMINISTRADOR = 1;
	String  CUPON_REGISTRO = "RICESTOGO";
	String PATH_DISK = "/home/ricestog/public_html/images/";//"c:/Web/";"/home/ricestog/public_html/images/"
	String TX_MOON   = "TX_MOON";
	String TX_NOON   = "TX_NOON";
	String TX_PR_DA  = "TX_PR_DA";
	String TX_PR_PO  = "TX_PR_PO";
}
