package org.awesky.common.other;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * SHA-1,MD5加密
 * 
 *
 */

public class EncryptUtil {

	static String salt = "success pass";
	
	public static String encrypt(String text,String type) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance(type);
			
			byte[] digest = md.digest(text.getBytes());
			outStr = byteToString(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return outStr;
	}

	private static String byteToString(byte[] digest) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			String tempStr = Integer.toHexString(digest[i] & 0xff);
			if (tempStr.length() == 1) {
				buf.append("0").append(tempStr);
			} else {
				buf.append(tempStr);
			}
		}
		return buf.toString().toLowerCase();
	}
	
}
