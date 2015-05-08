package com.wbw.iloveyou;



import java.io.InputStream;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.wbw.iloveyou.R;
import com.wbw.util.BitmapCache;
import com.wbw.util.CloseAction;
import com.wbw.util.MediaPlay;
import com.wbw.util.SharedPreferencesXml;
import com.wbw.util.SoundPlay;
import com.wbw.util.Util;
import com.wbw.view.FirstSurfaceView;

public class FirstActivity extends Activity
{
  private final int SHOWHEART = 2;
  private final int SHOWXIN = 1;
  private final int SHOWZI = 3;
  private final int GOTOSECOND = 4;
  private final int SOUND = 5;
  FrameLayout f1;
  FirstSurfaceView fr;
  
  //static int CO;
  
  LinearLayout l1;
  private Context mContext;
  private int screen_h;
  private int screen_w;
  
  private SharedPreferencesXml spxml;
  
 // private SoundPlay soundplay ;
  private MediaPlay me;
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.firstview);
    Display localDisplay = getWindowManager().getDefaultDisplay();
    this.screen_w = localDisplay.getWidth();
    this.screen_h = localDisplay.getHeight();
    this.mContext = getApplicationContext();
   // CO = getResources().getColor(R.color.black);
//    soundplay = SoundPlay.init();
//    soundplay.setContext(mContext);
//    soundplay.initSound();
    
    //有米
    AdManager.getInstance(this).init("8e48d0e4dad83d5c","a19d10440d91f5e4", false); 
    OffersManager.getInstance(this).onAppLaunch(); 
    PointsManager.getInstance(this).setEnableEarnPointsNotification(false);
    //关闭积分到账悬浮框提示功能
    PointsManager.getInstance(this).setEnableEarnPointsToastTips(false);
    
    Util.init().setContext(mContext);
    spxml = SharedPreferencesXml.init();
    
    //初始化音乐
    me = MediaPlay.init();
    String first_music = spxml.getConfigSharedPreferences("first_music", "0");
    if(first_music.equals("") || first_music.equals("0"))
    	me.InitMediaPlay(mContext,R.raw.heartv);
    else 
    	me.InitMediaPlay(mContext,first_music);
    findAllViews();
    createActions();
    //首次运行时显示提示
    String firstrun = spxml.getConfigSharedPreferences("firstrun", "true");
    System.out.println("fir:"+firstrun);
    if(Boolean.valueOf(firstrun)){
    	Toast.makeText(mContext, R.string.firstrun, Toast.LENGTH_LONG).show();
    	spxml.setConfigSharedPreferences("firstrun", "false");
    }
    
    System.out.println("w:"+screen_w+"  h:"+screen_h);
    
  }
  
  
  private void findAllViews()
  {
    f1 = ((FrameLayout)findViewById(R.id.f1));
    l1 = ((LinearLayout)findViewById(R.id.l1));
  }
  
  private void createActions()
  {
    //this.l1.setBackgroundColor(CO);
	  //初始化背景
	  String fb = spxml.getConfigSharedPreferences("first_back", "0");
	  if(fb.equals("") || fb.equals("0"))
		  l1.setBackgroundResource(R.drawable.q2);
	  else {
			try {
				Drawable draw = null;
				Uri uri = Uri.parse(fb);
				ContentResolver cr = mContext.getContentResolver();
				InputStream in = cr.openInputStream(uri);
				Bitmap bitmap = Util.init().getBitmap(in);
				//ImageView imageView = (ImageView) findViewById(R.id.iv01);
				/* 将Bitmap设定到ImageView */
				//imageView.setImageBitmap(bitmap);
						
				draw = new BitmapDrawable(mContext.getResources(), bitmap);				
				in.close();
				l1.setBackgroundDrawable(draw);
			} catch (Exception e) {
				//draw = getLocalDraw();
				l1.setBackgroundResource(R.drawable.q2);
			}			
	  }
    this.fr = new FirstSurfaceView(this, this.screen_w, this.screen_h,handler);
    this.f1.removeAllViews();
    this.f1.addView(this.fr, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.MATCH_PARENT));
    this.fr.showBackground();
    this.handler.sendEmptyMessageDelayed(SHOWXIN, 2000L);
    this.handler.sendEmptyMessageDelayed(SHOWHEART, 1000L);
    this.handler.sendEmptyMessageDelayed(SHOWZI, 1500L);
    this.handler.sendEmptyMessageDelayed(SOUND, 1100);
    //fr.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE); 
  }
  
  

 
  
  public void goSecond(){
	Intent t = new Intent();
	t.setClass(FirstActivity.this, SecondActivity.class);
	startActivity(t);
	//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
	overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	FirstActivity.this.finish();
	BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
  }
  
  private final int NOCONFIG = 6;
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case SHOWXIN:
    	  fr.showXin();
    	  break;
      case SHOWHEART:
    	  fr.showHeart();
    	  break;
      case SHOWZI:
    	  fr.showWenzi();
    	  break;
      case GOTOSECOND:
    	  goSecond();
    	  break;
      case SOUND:
    	  System.out.println("palyaa");
    	  //soundplay.play(SoundPlay.HEARTV, 2);
    	  String m_onoff = spxml.getConfigSharedPreferences("music_on_off", "on");
    	  if(!m_onoff.equals("off"))
    		  me.play();
    	  break;
      case NOCONFIG:
    	  isconfig = false;
    	  break;
      }
     
    }
  };
  
  private boolean isconfig = false;
  
  @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(isconfig){
				fr.setRun(true);
				Intent t = new Intent();
				t.setClass(FirstActivity.this, ConfigActivity.class);
				startActivity(t);
				//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				FirstActivity.this.finish();
				BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
			}else{
				new CloseAction(FirstActivity.this,fr);
			}
			return true;
		} else if(keyCode == KeyEvent.KEYCODE_MENU){
			if(!isconfig){
				isconfig = true;
				handler.sendEmptyMessageDelayed(NOCONFIG, 2000);
			}
			return true;
		}else
			return super.onKeyDown(keyCode, event);
		}
		
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

  
}