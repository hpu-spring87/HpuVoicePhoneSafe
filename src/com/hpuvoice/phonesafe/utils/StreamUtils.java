package com.hpuvoice.phonesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class StreamUtils {

	public static String parseStream(InputStream is) throws IOException{
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bfr = new BufferedReader(isr);
		
		StringWriter sw = new StringWriter();
		String str = null;
		while((str = bfr.readLine())!= null){
			sw.write(str);
		}
		bfr.close();
		sw.close();
		return sw.toString();
	}
}
