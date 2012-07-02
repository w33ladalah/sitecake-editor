package com.sitecake.commons.client.util;

public class StringScrambler {
	
	public static String scramble(String input) {
		return Base64Utils.toBase64(xor(rot13(Base64Utils.toBase64(input))));
	}
	
	public static String unscramble(String input) {
		return Base64Utils.fromBase64(rot13(xor(Base64Utils.fromBase64(input))));
	}
	
	private static String xor(String input) {
		char[] chars = input.toCharArray();

		for (int i=0; i<chars.length; i++) {
			chars[i] = (char)(chars[i] ^ 'a');
		}
		return String.valueOf(chars);
	}
	
	private static String rot13(String input) {
		char[] chars = input.toCharArray();
		
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if       (c >= 'a' && c <= 'm') c += 13;
			else if  (c >= 'n' && c <= 'z') c -= 13;
			else if  (c >= 'A' && c <= 'M') c += 13;
			else if  (c >= 'A' && c <= 'Z') c -= 13;
			chars[i] = c;
		}
		return String.valueOf(chars);
	}
}
