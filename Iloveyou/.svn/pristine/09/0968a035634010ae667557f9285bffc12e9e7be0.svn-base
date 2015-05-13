package com.wbw.info;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.wbw.util.Util;

import android.util.Log;
import android.util.Xml;




public class VersionXml {
	private  static VersionXml versionxml = null;
	public static VersionXml init(){
		if(versionxml == null)
			versionxml = new VersionXml();
		return versionxml;
	}
	
	
	/**
	 * 解析从服务器下载下来的XML，保存到全局静态数组
	 * @param path
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public void  getApkVersionXml(String path) throws XmlPullParserException, IOException{
		
		InputStream in = Util.init().getInputStream(path);
		ApkVersionInfo info = ApkVersionInfo.downloadinfo;
		
		XmlPullParser parser = Xml.newPullParser();
		//list.clear();  //凡是调用这个方法时先清空里面的值 ，要不然size会翻倍
		
		parser.setInput(in, "UTF-8");
		
		int event = parser.getEventType();
		
		while(event!=XmlPullParser.END_DOCUMENT){
			switch(event){
				case XmlPullParser.START_DOCUMENT:
					
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					 if(name.equals("versioncode")){
						info.setVersionCode(parser.nextText());
					}
					else if(name.equals("apkurl")){
						info.setApkUrl(parser.nextText());
					}
					else if(name.equals("description"))
						info.setUpdataDescription(parser.nextText());
					
					break;
				case XmlPullParser.END_TAG:
					break;
			}
			event = parser.next();				
		}	
		in.close();
	}
	


}


