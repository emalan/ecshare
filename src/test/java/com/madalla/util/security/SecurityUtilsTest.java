package com.madalla.util.security;

import junit.framework.TestCase;

public class SecurityUtilsTest extends TestCase{

	public void testEncrypt(){
		String password = "password";
		String hash1 = SecurityUtils.encrypt(password);

		String hash2 = SecurityUtils.encrypt(password);

		assertTrue(hash1.equals(hash2));

		System.out.println("hash1="+hash1);
		System.out.println("hash2="+hash2);
	}
}
