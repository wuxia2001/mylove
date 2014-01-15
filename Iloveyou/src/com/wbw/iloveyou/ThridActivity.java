package com.wbw.iloveyou;

import java.io.InputStream;

import com.wbw.util.BitmapCache;
import com.wbw.util.CloseAction;
import com.wbw.util.MediaPlay;
import com.wbw.util.SharedPreferencesXml;
import com.wbw.util.Util;
import com.wbw.view.SecondSurfaceView;
import com.wbw.view.ThridSurfaceView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ThridActivity extends Activity{
	FrameLayout f3;
	LinearLayout l3;
	private Context mContext;
	private int screen_h;
	private int screen_w;

	 
	private SharedPreferencesXml spxml;
	private MediaPlay me;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.thridview);
		Display localDisplay = getWindowManager().getDefaultDisplay();
		screen_w = localDisplay.getWidth();
		screen_h = localDisplay.getHeight();
		mContext = getApplicationContext();
		Util.init().setContext(mContext);
		spxml = SharedPreferencesXml.init();
		    
		    //初始化音乐
		    me = MediaPlay.init();
		    String thrid_music = spxml.getConfigSharedPreferences("thrid_music", "0");
		    if(thrid_music.equals("") || thrid_music.equals("0"))
		    	me.InitMediaPlay(mContext,R.raw.thrid_nan);
		    else 
		    	me.InitMediaPlay(mContext,thrid_music);
	    
		findAllViews();
		createActions();
	}
	
	private void findAllViews(){
		f3 = (FrameLayout) findViewById(R.id.f3);
		l3 = (LinearLayout) findViewById(R.id.l3);
	}
	
	private ThridSurfaceView tsv;
	private void createActions(){
		//int CO = getResources().getColor(R.color.black);
		//this.l3.setBackgroundColor(CO);
		String tb = spxml.getConfigSharedPreferences("thrid_back", "0");
		  if(tb.equals("") || tb.equals("0"))
			  l3.setBackgroundResource(R.drawable.q5);
		  else {
				try {
					Drawable draw = null;
					Uri uri = Uri.parse(tb);
					ContentResolver cr = mContext.getContentResolver();
					InputStream in = cr.openInputStream(uri);
					Bitmap bitmap = Util.init().getBitmap(in);
					//ImageView imageView = (ImageView) findViewById(R.id.iv01);
					/* 将Bitmap设定到ImageView */
					//imageView.setImageBitmap(bitmap);
							
					draw = new BitmapDrawable(mContext.getResources(), bitmap);				
					in.close();
					l3.setBackgroundDrawable(draw);
				} catch (Exception e) {
					//draw = getLocalDraw();
					l3.setBackgroundResource(R.drawable.q5);
				}			
		  }
		//l3.setBackgroundResource(R.drawable.q5);
		tsv = new ThridSurfaceView(mContext, screen_w, screen_h);
		f3.removeAllViews();
		f3.addView(tsv, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT));
		this.handler.sendEmptyMessageDelayed(SOUND, 1100);
	}
	
	

	  private final int SOUND = 5;
	  private final int NOCONFIG = 6;
	Handler handler = new Handler()
	  {
	    public void handleMessage(Message paramMessage)
	    {
	      switch (paramMessage.what)
	      {
	      default:
	      case SOUND:	    	  
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
					tsv.setRun(true);
					Intent t = new Intent();
					t.setClass(ThridActivity.this, ConfigActivity.class);
					startActivity(t);
					//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					ThridActivity.this.finish();
					BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
				}else
					new CloseAction(ThridActivity.this,tsv);
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
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 自动生成的方法存根
		super.onWindowFocusChanged(hasFocus);
	}
}
