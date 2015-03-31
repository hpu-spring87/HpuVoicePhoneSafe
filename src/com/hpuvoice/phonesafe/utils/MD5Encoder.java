package com.hpuvoice.phonesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {

	public static String disgest(String password){
		try {
			// ������һ��md5�ļ����㷨
			MessageDigest digest=MessageDigest.getInstance("MD5");
			//  ��һ���ֽ����龭��һϵ�и��ӵ��㷨 ���ܳ�һ���µ�byte���� 
			StringBuilder sb=new StringBuilder();
			byte[] bs = digest.digest(password.getBytes());
			for(byte b:bs){
				int i=(b&0xff)+3;  // �Ѹ���ת��������    +3  -1 ����  ������md5����
				String hexString = Integer.toHexString(i);//��10���Ƶ��� ת����16���Ƶ��ַ���
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
