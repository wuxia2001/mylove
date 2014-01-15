package com.wbw.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 工具类，所有通用
 * 
 * @author WBW
 * 
 */
public class Util {
	private static Util util;

	public static Util init() { // 单例，个人习惯用Init,标准是getInstance
		if (util == null)
			util = new Util();
		return util;
	}

	private Util() {

	}
	
	private Context context = null;
	public void setContext(Context c){
		this.context = c;
	}
	public Context getContext(){
		return context;
	}

	/**
	 * 
	 * @param path传入路径字符串
	 * @return File
	 */
	public File creatFileIfNotExist(String path) {
		System.out.println("cr");
		File file = new File(path);
		if (!file.exists()) {
			try {
				new File(path.substring(0, path.lastIndexOf(File.separator)))
						.mkdirs();
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return file;
	}

	/**
	 * 
	 * @param path传入路径字符串
	 * @return File
	 */
	public File creatDirIfNotExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.mkdirs();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return file;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public boolean IsExist(String path) {
		File file = new File(path);
		if (!file.exists())
			return false;
		else
			return true;
	}

	/**
	 * 创建新的文件，如果有旧文件，先删除再创建
	 * 
	 * @param path
	 * @return
	 */
	public File creatNewFile(String path) {
		File file = new File(path);
		if (IsExist(path))
			file.delete();
		creatFileIfNotExist(path);
		return file;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteFile(String path) {
		File file = new File(path);
		if (IsExist(path))
			file.delete();
		return true;
	}

	// 删除一个目录
	public boolean deleteFileDir(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!IsExist(path)) {
			return flag;
		}
		if (!file.isDirectory()) {

			file.delete();
			return true;
		}
		String[] filelist = file.list();
		File temp = null;
		for (int i = 0; i < filelist.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + filelist[i]);
			} else {
				temp = new File(path + File.separator + filelist[i]);
			}
			if (temp.isFile()) {

				temp.delete();
			}
			if (temp.isDirectory()) {
				deleteFileDir(path + "/" + filelist[i]);// 先删除文件夹里面的文件
			}
		}
		file.delete();

		flag = true;
		return flag;
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public String[] getFlieName(String rootpath) {
		File root = new File(rootpath);
		File[] filesOrDirs = root.listFiles();
		if (filesOrDirs != null) {
			String[] dir = new String[filesOrDirs.length];
			int num = 0;
			for (int i = 0; i < filesOrDirs.length; i++) {
				if (filesOrDirs[i].isDirectory()) {
					dir[i] = filesOrDirs[i].getName();

					num++;
				}
			}
			String[] dir_r = new String[num];
			num = 0;
			for (int i = 0; i < dir.length; i++) {
				if (dir[i] != null && !dir[i].equals("")) {
					dir_r[num] = dir[i];
					num++;
				}
			}
			return dir_r;
		} else
			return null;
	}

	/**
	 * //获得流
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public BufferedWriter getWriter(String path) throws FileNotFoundException,
			UnsupportedEncodingException {
		FileOutputStream fileout = null;
		fileout = new FileOutputStream(new File(path));
		OutputStreamWriter writer = null;
		writer = new OutputStreamWriter(fileout, "UTF-8");
		BufferedWriter w = new BufferedWriter(writer); // 缓冲区
		return w;
	}

	public InputStream getInputStream(String path) throws FileNotFoundException {
		// if(Comments.DEBUG) System.out.println("path:"+path);
		FileInputStream filein = null;
		// if(Comments.DEBUG) System.out.println("2");
		// File file = creatFileIfNotExist(path);
		File file = new File(path);
		filein = new FileInputStream(file);
		BufferedInputStream in = null;
		if (filein != null)
			in = new BufferedInputStream(filein);
		return in;
	}

	public boolean StateXmlControl(String path) {
		File f = new File(path);
		if (!f.exists())
			return false;
		if (f.length() == 0)
			return false;
		return true;
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[6 * 1024];
		int count = -1;
		while ((count = in.read(data, 0, 4 * 1024)) != -1)
			outStream.write(data, 0, count);
		data = null;
		return outStream.toByteArray();
	}

	/**
	 * 将OutputStream转换成byte数组
	 * 
	 * @param in
	 *            OutputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] OutputStreamTOByte(OutputStream out)
			throws IOException {

		byte[] data = new byte[6 * 1024];
		out.write(data);
		return data;
	}

	/**
	 * 将byte数组转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream(byte[] in) {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	/**
	 * 将byte数组转换成OutputStream
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static OutputStream byteTOOutputStream(byte[] in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(in);
		return out;
	}

	/**
	 * 把输入流中的数据输入到Path里的文件里
	 * 
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return
	 */
	public File writeFromInputToSD(String path, InputStream inputStream) {
		File file = null;
		OutputStream output = null;
		try {
			file = creatFileIfNotExist(path);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int temp;
			while ((temp = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 把数据输入到Path里的文件里
	 * 
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return
	 */
	public File writeFromInputToSD(String path, byte[] b) {
		File file = null;
		OutputStream output = null;
		try {
			file = creatFileIfNotExist(path);
			output = new FileOutputStream(file);
			output.write(b);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 方法：把一段文本保存到给定的路径中.
	 */
	public void saveTxtFile(String filePath, String text) {
		try {
			// 首先构建一个文件输出流,用于向文件中写入数据.
			creatFileIfNotExist(filePath);
			String txt = readTextLine(filePath);
			text = text + txt;
			FileOutputStream out = new FileOutputStream(filePath);
			// 构建一个写入器,用于向流中写入字符数据
			OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
			writer.write(text);
			// 关闭Writer,关闭输出流
			writer.close();
			out.close();
		} catch (Exception e) {
			String ext = e.getLocalizedMessage();
			// Toast.makeText(this, ext, Toast.LENGTH_LONG).show();
		}

	}

	public void clearTxtFile(String filePath) {
		try {
			// 首先构建一个文件输出流,用于向文件中写入数据.
			String text = "";
			FileOutputStream out = new FileOutputStream(filePath);
			// 构建一个写入器,用于向流中写入字符数据
			OutputStreamWriter writer = new OutputStreamWriter(out, "gb2312");
			writer.write(text);
			// 关闭Writer,关闭输出流
			writer.close();
			out.close();
		} catch (Exception e) {
			String ext = e.getLocalizedMessage();
			// Toast.makeText(this, ext, Toast.LENGTH_LONG).show();
		}
	}

	// 读取一个给定的文本文件内容,并把内容以一个字符串的形式返回
	public String readTextLine(String textFile) {
		try {
			// 首先构建一个文件输入流,该流用于从文本文件中读取数据
			FileInputStream input = new FileInputStream(textFile);
			// 为了能够从流中读取文本数据,我们首先要构建一个特定的Reader的实例,
			// 因为我们是从一个输入流中读取数据,所以这里适合使用InputStreamReader.
			InputStreamReader streamReader = new InputStreamReader(input,
					"gb2312");
			// 为了能够实现一次读取一行文本的功能,我们使用了 LineNumberReader类,
			// 要构建LineNumberReader的实例,必须要传一个Reader实例做参数,
			// 我们传入前面已经构建好的Reder.
			LineNumberReader reader = new LineNumberReader(streamReader);
			// 字符串line用来保存每次读取到的一行文本.
			String line = null;
			// 这里我们使用一个StringBuilder来存储读取到的每一行文本,
			// 之所以不用String,是因为它每次修改都会产生一个新的实例,
			// 所以浪费空间,效率低.
			StringBuilder allLine = new StringBuilder();
			// 每次读取到一行,直到读取完成
			while ((line = reader.readLine()) != null) {
				allLine.append(line);
				// 这里每一行后面,加上一个换行符,LINUX中换行是”\n”,
				// windows中换行是”\r\n”.
				allLine.append("\n");
			}
			// 把Reader和Stream关闭
			streamReader.close();
			reader.close();
			input.close();
			// 把读取的字符串返回
			return allLine.toString();
		} catch (Exception e) {
			// Toast.makeText(this, e.getLocalizedMessage(),
			// Toast.LENGTH_LONG).show();
			return "";
		}
	}

	// 转换dip为px
	public int convertDipOrPx(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	// 转换px为dip
	public int convertPxOrDip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	// 把字加长，使其可以滚动，在音乐界面
	public String dealString(String st, int size) {
		int value = size;
		if (st.length() >= value)
			return "  " + st + "  ";
		else {
			int t = (value - st.length()) / 2;
			for (int i = 0; i < t; i++) {
				st = " " + st + "  ";
			}
			return st;
		}
	}

	public String getTimeByFormat(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public String getDateTimeBylong(long time_data, String dateformat_batt) {
		java.util.Date date = new java.util.Date(time_data);
		SimpleDateFormat format = new SimpleDateFormat(dateformat_batt);
		return format.format(date);
	}

	// 取前面的名字　"."
	public String getNameByFlag(String source, String flag) {
		// String[] source_spli = source.split(flag);
		String s = source.toLowerCase().replace(flag, "");
		return s.trim();
	}

	/**
	 * 取Asset文件夹下文件
	 * @param paramContext
	 * @param paramString
	 * @return
	 * @throws IOException
	 */
	public InputStream getAssetsInputStream(Context paramContext,
			String paramString) throws IOException {
		return paramContext.getResources().getAssets().open(paramString);
	}
	
	//以省内存的方式读取图片
		public Bitmap getBitmap(InputStream is){
			   BitmapFactory.Options opt = new BitmapFactory.Options();   
		        opt.inPreferredConfig = Bitmap.Config.RGB_565;    
		       opt.inPurgeable = true;   
		       opt.inInputShareable = true; 
		       opt.inSampleSize = 4;
		          //获取资源图片   
		       //InputStream is = mContext.getResources().openRawResource(resId);   
		           return BitmapFactory.decodeStream(is,null,opt);   
		}

}
