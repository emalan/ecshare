package com.madalla.util.captcha;

public class CaptchaUtils {

	private CaptchaUtils(){}

    public static Integer randomInteger (int min, int max){
        return new Integer(randomInt(min, max));
    }
    public static int randomInt(int min, int max){
        return (int)(Math.random() * (max - min) + min);
    }

    public static String randomString(int min, int max){
        int num = randomInt(min, max);
        byte b[] = new byte[num];
        for (int i = 0; i < num; i++)
            b[i] = (byte)randomInt('a', 'z');
        return new String(b);
    }

}
