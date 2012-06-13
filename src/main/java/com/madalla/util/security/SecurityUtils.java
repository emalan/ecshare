package com.madalla.util.security;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class SecurityUtils {

//	public static String encrypt(String string) {
//		if (string == null){
//			throw new RuntimeException("encrypt - Cannot encrypt Null.");
//		}
//		try {
//			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//			messageDigest.update(string.getBytes("UTF-8"));
//			byte[] raw = messageDigest.digest();
//			String hash = new BASE64Encoder().encode(raw);
//			return hash;
//		} catch (NoSuchAlgorithmException e) {
//			log.error("encrypt - No Such Algorithm.",e);
//			throw new RuntimeException(e);
//		} catch (UnsupportedEncodingException e){
//			log.error("encrypt - UnsupportedEncodingException.",e);
//			throw new RuntimeException(e);
//		}
//	}

	public static String encrypt(String string) {
		if (string == null){
			throw new RuntimeException("encrypt - Cannot encrypt Null.");
		}
		BaseDigestPasswordEncoder encoder = new ShaPasswordEncoder(256);
		encoder.setEncodeHashAsBase64(true);
		return encoder.encodePassword(string, null);
	}

	public static String getGeneratedPassword(){
		return RandomStringUtils.randomAlphanumeric(6);
	}

}
