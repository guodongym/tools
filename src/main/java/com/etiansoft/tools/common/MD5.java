package com.etiansoft.tools.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String md5(String str) {
		try {
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(str.getBytes());
			byte[] digest = alg.digest();
			return byte2hex(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static String byte2hex(byte[] b) {
		StringBuilder result = new StringBuilder(32);
		for (byte element : b) {
			String stmp = Integer.toHexString(element & 0XFF);
			if (stmp.length() == 1) {
				result.append('0').append(stmp);
			} else {
				result.append(stmp);
			}
		}
		return result.toString().toUpperCase();
	}
}
