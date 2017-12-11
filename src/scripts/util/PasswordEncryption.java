package scripts.util;

import org.apache.commons.codec.binary.Base64;

public class PasswordEncryption {
	
	public static void main(String[] args) {
		//System.out.println(encryptPassword("veersudhir83"));
	}

	public String encryptPassword(String textPassword) {
		byte[] encodedPwdBytes = Base64.encodeBase64(textPassword.getBytes());
		String encryptString = new String(encodedPwdBytes);
		System.out.println("encryptPassword::" + encryptString);
		return (encryptString);		
	}
	
	public String decryptPassword(String encryptedPassword) {
		byte[] decodedPwdBytes = Base64.decodeBase64(encryptedPassword);
		String decryptString = new String(decodedPwdBytes);
		//System.out.println("decryptPassword::" + decryptString);
		return (decryptString);		
	}

}
