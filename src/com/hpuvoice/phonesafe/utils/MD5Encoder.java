package com.hpuvoice.phonesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {

	public static String disgest(String password){
		try {
			// 创建了一个md5的加密算法
			MessageDigest digest=MessageDigest.getInstance("MD5");
			//  把一个字节数组经过一系列复杂的算法 加密成一个新的byte数组 
			StringBuilder sb=new StringBuilder();
			byte[] bs = digest.digest(password.getBytes());
			for(byte b:bs){
				int i=(b&0xff)+3;  // 把负数转换成正数    +3  -1 加盐  不规则md5加密
				String hexString = Integer.toHexString(i);//把10进制的数 转换成16进制的字符串
				if(hexString.length()<2){
					sb.append("0");
				}
				sb.append(hexString);
				//System.out.println(hexString);
			}
			String string = sb.toString();
			//System.out.println(string);
			return string;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
