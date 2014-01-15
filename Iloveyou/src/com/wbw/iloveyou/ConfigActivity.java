package com.wbw.iloveyou;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;

import org.xmlpull.v1.XmlPullParserException;


import com.wbw.info.ApkVersionInfo;
import com.wbw.info.VersionXml;
import com.wbw.util.BitmapCache;
import com.wbw.util.CloseAction;
import com.wbw.util.HttpDownFile;
import com.wbw.util.MediaPlay;
import com.wbw.util.SharedPreferencesXml;
import com.wbw.util.Util;
import com.wbw.view.ColorPickerDialog;
import com.wbw.view.SwitchButton;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends Activity{
	private SharedPreferencesXml spxml;
	private Context mContext;
	private String base;
	 @Override
	  protected void onCreate(Bundle savedInstanceState) 
	  {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.config);
	    mContext = getApplicationContext();
	    Util.init().setContext(mContext);
	    spxml = SharedPreferencesXml.init();
	    base  = Environment.getExternalStorageDirectory().getPath()+"/iloveyou";
	    Util.init().creatDirIfNotExist(base+"/");
	    MediaPlay.init().stop();
	    findAllViews();
	    setDefaultValues();
	    createActions();
	  }
	 
	 private ImageView cancle,ok;
	 private Button reset_bt;
	 private EditText first_name_1,first_name_2;
	 private Button first_back_bt,first_music_bt;
	 private EditText second_et;
	 private Button second_back_bt,second_enter_bt;
	 private EditText thrid_f_et_1,thrid_f_et_2;
	 private EditText thrid_s_et_1,thrid_s_et_2,thrid_s_et_3,thrid_s_et_4;
	 private Button thrid_back_bt,thrid_music_bt;
	 private SwitchButton music_on_off;
	 
	 private Button second_textcolor_bt;
	 private ImageView isnew;
	 private Button downapk_tv;
	 private Button youmi_bt;
	 
	 private void findAllViews(){
		 cancle = (ImageView) findViewById(R.id.cancle);
		 ok = (ImageView) findViewById(R.id.ok);
		 reset_bt = (Button) findViewById(R.id.reset_bt);
		 first_name_1 = (EditText) findViewById(R.id.first_et_1);
		 first_name_2 = (EditText) findViewById(R.id.first_et_2);
		 first_back_bt = (Button) findViewById(R.id.first_back_bt);
		 first_music_bt = (Button) findViewById(R.id.first_music_bt);
		 second_et = (EditText) findViewById(R.id.second_et);
		 second_back_bt = (Button) findViewById(R.id.second_back_bt);
		 second_enter_bt = (Button) findViewById(R.id.second_enter_bt);
		 thrid_f_et_1 = (EditText) findViewById(R.id.thrid_f_et_1);  //frist
		 thrid_f_et_2 = (EditText) findViewById(R.id.thrid_f_et_2);
		 thrid_s_et_1 = (EditText) findViewById(R.id.thrid_s_et_1);  //second
		 thrid_s_et_2 = (EditText) findViewById(R.id.thrid_s_et_2);
		 thrid_s_et_3 = (EditText) findViewById(R.id.thrid_s_et_3);
		 thrid_s_et_4 = (EditText) findViewById(R.id.thrid_s_et_4);
		 thrid_back_bt = (Button) findViewById(R.id.thrid_back_bt);
		 thrid_music_bt = (Button) findViewById(R.id.thrid_music_bt);
		 music_on_off = (SwitchButton) findViewById(R.id.music_on_off);
		 
		 second_textcolor_bt = (Button) findViewById(R.id.second_textcolor_bt);
		 downapk_tv = (Button) findViewById(R.id.downapk_tv);
		 isnew = (ImageView) findViewById(R.id.isnew);
		 
		 youmi_bt = (Button)findViewById(R.id.youmi_bt);
	 }
	 
	 //初始化值
	 private void setDefaultValues(){
		 String fn1 = spxml.getConfigSharedPreferences("first_name_1", R.string.first_et_1);
		 String fn2 = spxml.getConfigSharedPreferences("first_name_2", R.string.first_et_2);
		 first_name_1.setText(fn1);
		 first_name_2.setText(fn2);
		 
		 String second_words = spxml.getConfigSharedPreferences("second_words", R.string.second_words);
		 second_et.setText(second_words);
		 
		 thrid_f_et_1.setText(spxml.getConfigSharedPreferences("thrid_f_et_1", R.string.thrid_f_et_1));
		 thrid_f_et_2.setText(spxml.getConfigSharedPreferences("thrid_f_et_2", R.string.thrid_f_et_2));
		 thrid_s_et_1.setText(spxml.getConfigSharedPreferences("thrid_s_et_1", R.string.thrid_s_et_1));
		 thrid_s_et_2.setText(spxml.getConfigSharedPreferences("thrid_s_et_2", R.string.thrid_s_et_2));
		 thrid_s_et_3.setText(spxml.getConfigSharedPreferences("thrid_s_et_3", R.string.thrid_s_et_3));
		 thrid_s_et_4.setText(spxml.getConfigSharedPreferences("thrid_s_et_4", R.string.thrid_s_et_4));
		 
		 MusicOn_off();
	 }
	 
	 private void MusicOn_off(){
		 String on_off = spxml.getConfigSharedPreferences("music_on_off", "on");
		 if(on_off.equals("off")){
			 music_on_off.setChecked(false);
		 }else{
			 music_on_off.setChecked(true,false);
		 }
	 }
	 
	 private void createActions(){
		 isnew.setVisibility(View.GONE);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					checkVersion();
					
				}
			}).start();
		 downapk_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				downApk();
			}
		});
		 
		 
		 first_back_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Toast.makeText(mContext, R.string.configbackpic, Toast.LENGTH_SHORT).show();
				setLoaclBackPicAndMu(pic_kind, first_back);
			}
		});
		 
		 first_music_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setLoaclBackPicAndMu(music_kind, first_music);
			}
		});
		 
		 second_back_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Toast.makeText(mContext, R.string.configbackpic, Toast.LENGTH_SHORT).show();
				setLoaclBackPicAndMu(pic_kind, second_back);
			}
		});
		 
		 thrid_back_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Toast.makeText(mContext, R.string.configbackpic, Toast.LENGTH_SHORT).show();
				setLoaclBackPicAndMu(pic_kind, thrid_back);
			}
		});
		 
		 thrid_music_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setLoaclBackPicAndMu(music_kind, thrid_music);
			}
		});
		 
		 //换行
		 second_enter_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				second_et.append("\n");
				//second_et.
			}
		});
		 
		 music_on_off.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 自动生成的方法存根
				if(isChecked){
					spxml.setConfigSharedPreferences("music_on_off", "on");
				}else {
					spxml.setConfigSharedPreferences("music_on_off", "off");
				}
			}
		});
		 
		 reset_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				new AlertDialog.Builder(ConfigActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.dialog_title)
				.setMessage(R.string.reset_message)
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
						
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//初始化
								spxml.setDefault();
								//更新此界面
								setDefaultValues();
							}
						})
			
				.create().show();
			}
		});
		 
		 cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				gotoFirstAc();
			}
		});
		 
		 second_textcolor_bt.setOnClickListener(new View.OnClickListener() {   
             
	            @Override  
	            public void onClick(View v) {   
	           	 int C =  mContext.getResources().getColor(R.color.huang);
	     		String getc = spxml.getConfigSharedPreferences("second_color", String.valueOf(C));
	            	ColorPickerDialog dialog = new ColorPickerDialog(ConfigActivity.this, Integer.valueOf(getc),    
	                        getResources().getString(R.string.btn_color_picker),    
	                        new ColorPickerDialog.OnColorChangedListener() {   
	                        
	                    @Override  
	                    public void colorChanged(int color) {   
	                    	spxml.setConfigSharedPreferences("second_color", String.valueOf(color));
	                    }   
	                });   
	                dialog.show();   
	            }   
	        });   
		 
		 ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String firstconfig = spxml.getConfigSharedPreferences("firstconfig", "true");
				if(Boolean.valueOf(firstconfig)){
					if(PointsManager.getInstance(mContext).spendPoints(20)){           
				        //Log.d("test","已消费100积分");
					 	Toast.makeText(mContext, "首次保存消耗20积分，只消耗此一次，此后无再需积分",
					 			Toast.LENGTH_LONG).show();
					 	spxml.setConfigSharedPreferences("firstconfig", "false");
				    }else{
				        //Log.d("text","消费积分失败(积分余额不足)");
				    	Toast.makeText(mContext, "首次保存需消耗20积分，只消耗此一次，此后无再需积分，" +
				    			"您积分不够，保存失败",
					 			Toast.LENGTH_LONG).show();
				    	return;
				    }   
				 }
				String f_1 = first_name_1.getText().toString().trim();
				String f_2 = first_name_2.getText().toString();
				
				String s_s = second_et.getText().toString();
				
				String t_f_11 = thrid_f_et_1.getText().toString();
				String t_f_12 = thrid_f_et_2.getText().toString();
				
				String t_s_11 = thrid_s_et_1.getText().toString();
				String t_s_12 = thrid_s_et_2.getText().toString();
				String t_s_13 = thrid_s_et_3.getText().toString();
				String t_s_14 = thrid_s_et_4.getText().toString();
						
				spxml.setConfigSharedPreferences("first_name_1", f_1);
				spxml.setConfigSharedPreferences("first_name_2", f_2);
				spxml.setConfigSharedPreferences("second_words", s_s);
				spxml.setConfigSharedPreferences("thrid_f_et_1", t_f_11);
				spxml.setConfigSharedPreferences("thrid_f_et_2", t_f_12);
				spxml.setConfigSharedPreferences("thrid_s_et_1", t_s_11);
				spxml.setConfigSharedPreferences("thrid_s_et_2", t_s_12);
				spxml.setConfigSharedPreferences("thrid_s_et_3", t_s_13);
				spxml.setConfigSharedPreferences("thrid_s_et_4", t_s_14);
				gotoFirstAc();
			}
		});
		 
		 PointsManager.getInstance(this).awardPoints(1);    
		 youmi_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				OffersManager.getInstance(mContext).showOffersWall();
			}
		});
		 
	 }
	 
	private void  checkVersion() {
	
		
		String localpath = base+"/person_ily.xml";
		String downurl = "http://www.iever.cn/YJdown/person_ily.xml";		
		final HttpDownFile down2 = new HttpDownFile(downurl,localpath
				);
		boolean f = down2.DownFile();
		if (!f) {
			return ;
		}
		
		try {
			VersionXml.init().getApkVersionXml(localpath);
			ApkVersionInfo info = ApkVersionInfo.downloadinfo;
			String cur;
			cur = getCurrentVersionName();
			if(cur.equals(info.getVersionCode())){ 
				handler.obtainMessage(ISNEW_VISIBLE, 0, 0).sendToTarget();
				//isnew.setVisibility(View.GONE);
				//return false;
			}else{
				handler.obtainMessage(ISNEW_VISIBLE, 1, 1).sendToTarget();
				//isnew.setVisibility(View.VISIBLE);
				//return true;
			}
		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	

	//获取当前的版本名字
	private String getCurrentVersionName() throws NameNotFoundException{
		PackageManager packagemanager = mContext.getPackageManager();
		PackageInfo packageinfo = packagemanager.getPackageInfo(
				mContext.getPackageName(), 0);
		return packageinfo.versionName;
	}
	
	private ProgressBar pro;
	private void downApk(){
		//final String base  = Environment.getExternalStorageDirectory().getPath();
		String localpath = base+"/person_ily.xml";
		
		ApkVersionInfo info = ApkVersionInfo.downloadinfo;
		String cur;
		try {
			cur = getCurrentVersionName();
			if(cur.equals(info.getVersionCode())){ 
				Toast.makeText(mContext, "版本相同，无需下载", Toast.LENGTH_SHORT).show();
			}else{
				//下载
				AlertDialog.Builder builer = new Builder(ConfigActivity.this) ; 
				builer.setTitle("版本升级"); 
				LayoutInflater mInflater = (LayoutInflater) ConfigActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = mInflater.inflate(R.layout.con_downapk, null);
				TextView current_version = (TextView) view.findViewById(R.id.current_version);
				 pro = (ProgressBar) view.findViewById(R.id.pro);
				TextView text = (TextView) view.findViewById(R.id.text);
				current_version.setText("当前版本是："+cur);
				text.setText(info.getUpdataDescription());
				
				builer.setView(view);
				AlertDialog dialog = builer.create(); 
				dialog.show();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						String g = ApkVersionInfo.downloadinfo.getApkUrl();
						String lo = base+"/iloveyou.apk";
						try {
							getFileFromServer(g,lo);
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}).start();
			}
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	private final int changepro = 1;
	private final int DOWNOK = 2;
	private final int ISNEW_VISIBLE = 3;
	 Handler handler = new Handler()
	  {
	    public void handleMessage(Message mm)
	    {
	      switch (mm.what)
	      {
	      case changepro:
	    	  if(pro != null)
	    		  pro.setProgress(mm.arg1);
	    	  break;
	      case DOWNOK:
	    	  installApk();
	    	  break;
	      case ISNEW_VISIBLE:
	    	  if(mm.arg1 == 1)
	    		  isnew.setVisibility(View.VISIBLE);
	    	  else isnew.setVisibility(View.GONE);
	    	  break;
	      }
	    }
	  };
	  
	// 安装apk
	protected void installApk() {
		//final String base  = Environment.getExternalStorageDirectory().getPath();
		String lo = base+"/iloveyou.apk";
		File file = new File(lo);
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		ConfigActivity.this.startActivity(intent);

	}
	  
	  
	/**
	 * 从服务器上取出更新的APK放到/sdcard/YongJingSmartHome/updata.apk（Comments）里
	 * @param path 为UpdataInfo里ApkUrl
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public  File getFileFromServer(String urlpath,String localp) throws Exception{ 
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的 
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
			FileOutputStream fos=null;
			BufferedInputStream bis=null;
			InputStream is=null;
			try{
				System.out.println("apht:"+urlpath);
			URL url = new URL(urlpath); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			conn.setConnectTimeout(5000); 
			//获取到文件的大小 
			//pd.setMax(100); 
			 is = conn.getInputStream(); 
			File file = Util.init().creatFileIfNotExist(localp); 
			 fos = new FileOutputStream(file); 
			 bis = new BufferedInputStream(is); 
			
			byte[] buffer = new byte[1024]; 
			int len ; 
			
			int total=0; 
			 int progress;
			 int all = conn.getContentLength();
			while((len =bis.read(buffer))!=-1){ 
				fos.write(buffer, 0, len); 
				total+= len; 
				//获取当前下载量 
				 progress= (total*100/all);
				 handler.obtainMessage(changepro, progress, progress).sendToTarget();
				
			} 
			handler.sendEmptyMessage(DOWNOK);
			return file; 
			}catch(SocketTimeoutException e){
			
				
				return null;
			}finally{
				fos.close(); 				
				bis.close(); 
				is.close(); 
			}
		} else{ 
		return null; 
		} 
	} 
	
	 
	 
	 public void gotoFirstAc(){
		 Intent t = new Intent();
			t.setClass(ConfigActivity.this, FirstActivity.class);
			startActivity(t);
			//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			ConfigActivity.this.finish();
			BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
	 }
	 
	 
	 public int pic_kind = 1;
	 public int music_kind = 2;
	 
	 public int first_back = 11;
	 public int first_music = 12;
	 private int second_back = 21;
	 private int thrid_back = 31;
	 private int thrid_music = 32;
	//本地图片按钮响应
	public void setLoaclBackPicAndMu(int kind_type,int activity){
		//取消对话框	
		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		if(kind_type == music_kind)
			intent.setType("audio/*");
		else 
			intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		/* 取得相片后返回本画面 */
		startActivityForResult(intent, activity);
	}
			
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			
			if(requestCode == first_back){
				spxml.setConfigSharedPreferences("first_back", uri.toString());
			}
			else if(requestCode == first_music){
				spxml.setConfigSharedPreferences("first_music", uri.toString());
			}
			else if(requestCode == second_back){
				spxml.setConfigSharedPreferences("second_back", uri.toString());
			}
			else if(requestCode == thrid_back){
				spxml.setConfigSharedPreferences("thrid_back", uri.toString());
			}
			else if(requestCode == thrid_music){
				spxml.setConfigSharedPreferences("thrid_music", uri.toString());
			}
						
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gotoFirstAc();
			return true;
		}else
			return super.onKeyDown(keyCode, event);
	}
		
	
}
