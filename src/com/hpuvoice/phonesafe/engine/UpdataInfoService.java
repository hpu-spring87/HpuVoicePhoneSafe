package com.hpuvoice.phonesafe.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.hpuvoice.phonesafe.bean.UpdataInfo;
import com.hpuvoice.phonesafe.utils.StreamUtils;

import android.content.Context;

public class UpdataInfoService {

	private UpdataInfo updataInfo = null;
	private Context context;
	
	public UpdataInfoService(Context context){
		this.context = context;
	}
	public com.hpuvoice.phonesafe.bean.UpdataInfo getUpdataInfo(int urlid) throws JSONException{
		try {
			String path = context.getResources().getString(urlid);
			URL url = new URL(path);
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setRequestMethod("GET");
			int code = conn.getResponseCode();
			if (code == 200) {
			InputStream is = conn.getInputStream();
			String result = StreamUtils.parseStream(is);
			JSONObject jobj = new JSONObject(result);
			updataInfo.setVersion(jobj.getString("code"));
			updataInfo.setDescription(jobj.getString("des"));
			updataInfo.setApkurl(jobj.getString("apkurl"));
		   }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return updataInfo;
	}
	//===========================================================================================
	/**
	 * 上面是json解析，下面是xml解析
	 * 给出inputstream---is
		XmlPullParser parser = Xml.newPullParser();
		UpdataInfo info = new UpdataInfo();
		parser.setInput(is, "UTF-8");
		int type = parser.getEventType();
		while(type!=XmlPullParser.END_DOCUMENT){
			switch (type) {
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					String version = parser.nextText();
					info.setVersion(version);
				}else if("description".equals(parser.getName())){
					String description = parser.nextText();
					info.setDescription(description);
				}else if("apkurl".equals(parser.getName())){
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
				break;

			}
			type = parser.next();
		}
		
		return info;
	 */
	//===========================================================================================
}
