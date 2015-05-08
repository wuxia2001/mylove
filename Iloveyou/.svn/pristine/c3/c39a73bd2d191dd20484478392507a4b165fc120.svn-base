package com.wbw.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;



public class HttpDownFile  {
	private String urlstring;
	private String targetpath;
	private boolean issuccess;
	//private Context mContext;
	
	
	public HttpDownFile(){
		
	}
	
	/**
	 * 
	 * @param urlstring
	 * @param targetpath
	 */
	public HttpDownFile(String urlstring,String targetpath){
		this.urlstring = urlstring;
		this.targetpath = targetpath;
		
	}
	
	
	public boolean DownFile(){
		issuccess = downFile(urlstring,targetpath);
		if(issuccess)
			return true;
		else{
			
			//NormalToast.makeText(mContext, "下载获取版本文件失败", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	
	public boolean getIssuccess(){
		return issuccess;
	}
	
	/**
	 *  返回类型
	 *  -1：代表文件下载出错
	 *  0：代表文件下载成功
	 *  1：代表文件已经存在
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return
	 */
	public boolean downFile(String urlStr,String path){
		 System.out.println("downing file :urlstr:"+urlStr);
		//Log.e(Comments.TAG, "downing file path:"+urlStr);
		InputStream inputStream = null;
		try{			
				inputStream = getInputStreamFromUrl(urlStr);
			
				if(inputStream == null)
				{
					return false;
				}else{
					System.out.println( "ready to get writeSD");
					File resultFile = Util.init().writeFromInputToSD(path, inputStream);
					//Log.v(Comments.TAG, "finist to write");
					if(resultFile==null){
						return false;
					}
				return true;
				}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			try{
				if(inputStream != null)
					inputStream.close();
			}catch(Exception e ){
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 根据URL获取输入流
	 * @param urlStr
	 * @return
	 * @throws IOException 
	 */
	public InputStream getInputStreamFromUrl(String urlStr) throws IOException{
		URL url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();	
			urlConn.setConnectTimeout(5000); 
			urlConn.setReadTimeout(70000);
			InputStream inputStream = urlConn.getInputStream();
			return inputStream;
		
	}
	
	public static void main(String args[])
	{
		//HttpDownFile hd = new HttpDownFile();
		//hd.downFile("http://192.168.1.100:8080/load?filename=version.xml", "d:/xxx.xml");
	}
	 
}
