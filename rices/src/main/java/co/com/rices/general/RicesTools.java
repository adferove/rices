package co.com.rices.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class RicesTools {

	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static PBEKeySpec pbeKeySpec;
	private static PBEParameterSpec pbeParamSpec;
	private static SecretKeyFactory keyFac;
	private static Cipher pbeCipher;
	private static SecretKey pbeKey;


	private static byte[] salt = {(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99};
	private static char[] llave = {'k','e','n'};

	/**
	 * Función que elimina acentos y caracteres especiales de una cadena de texto.
	 * 
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public static String sustituirCaracterEspecial(String input) {
		String original = "áàäéèëíìïóòöúùuÁÀÄÉÈËÍÌÏÓÒÖÚÙÜçÇ";
		String ascii = "aaaeeeiiiooouuuAAAEEEIIIOOOUUUcC";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}
		return output;
	}

	/**
	 * Validate given email with regular expression.
	 * 
	 * @param email
	 *            email for validation
	 * @return true valid email, otherwise false
	 */
	public static boolean validateEmail(String email) {
		// Compiles the given regular expression into a pattern.
		Pattern pattern = Pattern.compile(PATTERN_EMAIL);
		// Match the given input against this pattern
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isNumber(String pValor){
		try{
			new Double(pValor);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	public static String encriptar(String aContrasena) throws Exception {
		String cadena="";

		try {
			// Crear el parámetro PBE con los valores de los parámetros de encriptación
			// clave y count
			pbeParamSpec = new PBEParameterSpec(salt, 20);
			pbeKeySpec = new PBEKeySpec(llave);
			keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Crear el objeto PBE Cipher
			pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");			

			// Inicializar el objeto PBE Cipher con la llave y los parámetros
			pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

			String password= aContrasena;  
			byte[] texto_normal = password.getBytes();

			// Encriptar el password
			byte[] texto_cifrado = pbeCipher.doFinal(texto_normal);

			//Convertir en la cadena a guardar en la base de datos
			int temp;
			for(int i=0;i<texto_cifrado.length;i++) 
			{
				temp=(int)texto_cifrado[i];
				if (temp<0)
					if (temp>-16)
						cadena=cadena+"0"+Integer.toString(temp,16);
					else
						cadena=cadena+Integer.toString(temp,16);
				else
					if (temp<16)
						cadena=cadena+"00"+Integer.toString(temp,16);
					else
						cadena=cadena+"0"+Integer.toString(temp,16);
			}
		}
		catch(Exception e) {
			throw new Exception(e);
		}
		//Retornar el password encriptado
		return cadena;
	}

}
