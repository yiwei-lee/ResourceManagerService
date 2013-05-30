package thu.cs.lyw.rm.util;

import java.util.Random;

public class RStringGenerator {
	private final static String charSet = "abcdefghijklmnopqrstuvwxyz0123456789";
	private final static Random rd = new Random();
	
	public static String getString(int length){
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length ; i++){
			str.append(charSet.charAt(rd.nextInt(charSet.length())));
		}
		return str.toString();
	}
//	public static void main(String[] args){
//		System.out.println(RStringGenerator.getString(8));
//	}
}
