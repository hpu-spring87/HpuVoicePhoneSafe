package com.hpuvoice.phonesafe.engine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.hpuvoice.phonesafe.bean.MsgInfo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

@SuppressLint("SdCardPath") public class BackUpMsgService {

	public interface BackUpProcess{
		public void BackUpMax(int max);
		public void BackUpNow(int nowProcess);
	}
	
	/**
	 * 短信备份---
	 * @param context
	 * @param backUpProcess
	 */
	
	public static void backUpMsg(Context context,BackUpProcess backUpProcess) {
		XmlSerializer newSerializer = Xml.newSerializer();
		try {
			//File file = new File("/mnt/sdcard/msgBackUp.xml");
			//OutputStream os = new FileOutputStream(file);
			//---第一种写法，硬代码直接写到固定文件---
			
			FileOutputStream os = context.openFileOutput("msgBackUp.xml", Context.MODE_PRIVATE);
			newSerializer.setOutput(os, "utf-8");
			newSerializer.startDocument("utf-8", true);
			newSerializer.startTag(null, "SmSs");
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms");
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"date", "type", "body" }, null, null, null);
			int max = cursor.getCount();
			backUpProcess.BackUpMax(max);
			int nowProcess = 0;
			while (cursor.moveToNext()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				newSerializer.startTag(null, "Msg");
				
				newSerializer.startTag(null, "address");
				String address = cursor.getString(0);
				newSerializer.text(address);
				newSerializer.endTag(null, "address");
				
				newSerializer.startTag(null, "date");
				String date = cursor.getString(1);
				newSerializer.text(date);
				newSerializer.endTag(null, "date");
				
				newSerializer.startTag(null, "type");
				String type = cursor.getString(2);
				newSerializer.text(type);
				newSerializer.endTag(null, "type");
				
				newSerializer.startTag(null, "body");
				String body = cursor.getString(2);
				newSerializer.text(body);
				newSerializer.endTag(null, "body");
				
				newSerializer.endTag(null, "Msg");
				
				nowProcess++;
				backUpProcess.BackUpNow(nowProcess);
			}
			newSerializer.endTag(null, "SmSs");
			newSerializer.endDocument();
			cursor.close();
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解析备份短信---
	 * @param context
	 * @return
	 */
	public static List<MsgInfo> parseBackUpMsg(Context context){
		List<MsgInfo> backUpMsgs = null ;
		MsgInfo msgInfo = null;
		
		XmlPullParser newPullParser = Xml.newPullParser();
		
		InputStream inputStream;
		try {
			inputStream = context.openFileInput("msgBackUp.xml");
			newPullParser.setInput(inputStream , "utf-8");
			int eventType = newPullParser.getEventType();
			
			while(eventType!=XmlPullParser.END_DOCUMENT){
				String nodeName = newPullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					backUpMsgs = new ArrayList<MsgInfo>();
					break;
				case XmlPullParser.START_TAG:
					if("Msg".equals(nodeName)){
						msgInfo = new MsgInfo();
					}else if("address".equals(nodeName)){
						String address = newPullParser.nextText();
						msgInfo.setAddress(address);
					}else if("date".equals(nodeName)){
						String date = newPullParser.nextText();
						msgInfo.setDate(date);
					}else if("type".equals(nodeName)){
						String type = newPullParser.nextText();
						msgInfo.setType(type);
					}else if("body".equals(nodeName)){
						String body = newPullParser.nextText();
						msgInfo.setBody(body);
					}
					break;
				case XmlPullParser.END_TAG:
					if("Msg".equals(nodeName)){
						backUpMsgs.add(msgInfo);
						msgInfo = null;
					}
					break;

				default:
					break;
				}
				eventType = newPullParser.next();
			}
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return backUpMsgs;
	}
	
	
	
	
	
	
}
