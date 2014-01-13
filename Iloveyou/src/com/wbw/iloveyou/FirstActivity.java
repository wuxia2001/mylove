package com.wbw.iloveyou;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wbw.iloveyou.R;
import com.wbw.util.BitmapCache;
import com.wbw.view.FirstSurfaceView;

public class FirstActivity extends Activity
{
  private final int SHOWHEART = 2;
  private final int SHOWXIN = 1;
  private final int SHOWZI = 3;
  FrameLayout f1;
  FirstSurfaceView fr;
  
  LinearLayout l1;
  private Context mContext;
  private int screen_h;
  private int screen_w;
  
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
    findAllViews();
    createActions();
  }
  
  private void createActions()
  {
    //this.l1.setBackgroundColor(getResources().getColor());
    this.fr = new FirstSurfaceView(this, this.screen_w, this.screen_h,handler);
    this.f1.removeAllViews();
    this.f1.addView(this.fr, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.MATCH_PARENT));
    this.fr.showBackground();
    this.handler.sendEmptyMessageDelayed(SHOWXIN, 2000L);
    this.handler.sendEmptyMessageDelayed(SHOWHEART, 1000L);
    this.handler.sendEmptyMessageDelayed(SHOWZI, 1500L);
  }

  private void findAllViews()
  {
    this.f1 = ((FrameLayout)findViewById(R.id.f1));
    this.l1 = ((LinearLayout)findViewById(R.id.l1));
  }
  
  public void goSecond(){
	Intent t = new Intent();
	t.setClass(FirstActivity.this, SecondActivity.class);
	startActivity(t);
	FirstActivity.this.finish();
	BitmapCache.getInstance().clearCache();  //软引用，跳到新界面可清空原来图片内存
	overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);//小小动画
  }
  
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
      case 4:
    	  goSecond();
    	  break;
      }
     
    }
  };
}