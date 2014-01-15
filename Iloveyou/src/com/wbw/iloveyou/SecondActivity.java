package com.wbw.iloveyou;

import java.io.InputStream;

import com.wbw.iloveyou.R;
import com.wbw.util.BitmapCache;
import com.wbw.util.CloseAction;
import com.wbw.util.MediaPlay;
import com.wbw.util.SharedPreferencesXml;
import com.wbw.util.Util;
import com.wbw.view.FirstSurfaceView;
import com.wbw.view.SecondSurfaceView;

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
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SecondActivity extends Activity {

	FrameLayout f2;
	LinearLayout l2;
	private Context mContext;
	private int screen_h;
	private int screen_w;

	 private SharedPreferencesXml spxml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.secondview);
		Display localDisplay = getWindowManager().getDefaultDisplay();
		screen_w = localDisplay.getWidth();
		screen_h = localDisplay.getHeight();
		mContext = getApplicationContext();
		Util.init().setContext(mContext);
	    spxml = SharedPreferencesXml.init();
	    
		findAllViews();
		createActions();
	}
	
	private void findAllViews(){
		f2 = (FrameLayout) findViewById(R.id.f2);
		l2 = (LinearLayout) findViewById(R.id.l2);
	}
	
	private SecondSurfaceView ssv;
	private void createActions(){
		//float al = 0.5f;
		//初始化背景
		  String fb = spxml.getConfigSharedPreferences("second_back", "0");
		  if(fb.equals("") || fb.equals("0"))
			  l2.setBackgroundResource(R.drawable.q6);
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
					l2.setBackgroundDrawable(draw);
				} catch (Exception e) {
					//draw = getLocalDraw();
					l2.setBackgroundResource(R.drawable.q6);
				}			
		  }
		//l2.setBackgroundResource(R.drawable.q6);
		//l2.setAlpha(al);
		ssv = new SecondSurfaceView(this, this.screen_w, this.screen_h,handler);
		f2.removeAllViews();
		f2.addView(ssv, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT));
		//this.l2.setBackgroundColor(getResources().getColor(R.color.black));
		handler.sendEmptyMessageDelayed(showh, 1500L);
		handler.sendEmptyMessageDelayed(showtext, 2500L);
		handler.sendEmptyMessageDelayed(showb, 1500L);
	}
	
	public void goThrid(){
		Intent t = new Intent();
		t.setClass(SecondActivity.this, ThridActivity.class);
		startActivity(t);
		//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
		overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
		SecondActivity.this.finish();
		BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
		MediaPlay.init().stop();
	  }
	

	private final int showh = 1;
	private final int showtext = 2;
	private final int showb = 3;
	private final int gotothrid = 4;
	  private final int NOCONFIG = 6;
	Handler handler = new Handler()
	  {
	    public void handleMessage(Message paramMessage)
	    {
	      switch (paramMessage.what)
	      {
	      default:
	      case showh:
	    	  ssv.showh();
	    	  break;
	      case showtext:
	    	  ssv.showW();    	  
	    	  break;
	      case showb:
	    	  ssv.showBK();
	    	  break;
	      case gotothrid:
	    	  goThrid();
	    	  break;
	      case NOCONFIG:
	    	  isconfig = false;
	    	  break;
	      }
	    }
	  };
	  
	 

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 自动生成的方法存根
		super.onWindowFocusChanged(hasFocus);
		//这里才是实际的高度和宽度，所有的坐标计算距离要注意这里。
		System.out.println("ss:"+f2.getWidth()+" ss_h:"+f2.getHeight());
	}
	
	
	 private boolean isconfig = false;
	  
	  @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO 自动生成的方法存根
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				if(isconfig){
					ssv.setRun(true);
					Intent t = new Intent();
					t.setClass(SecondActivity.this, ConfigActivity.class);
					startActivity(t);
					//overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					SecondActivity.this.finish();
					BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
				}else
					//new CloseAction(FirstActivity.this,fr);
					new CloseAction(SecondActivity.this,ssv);
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
			
}
