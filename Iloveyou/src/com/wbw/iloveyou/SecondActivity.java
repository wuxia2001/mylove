package com.wbw.iloveyou;

import com.wbw.iloveyou.R;
import com.wbw.view.FirstSurfaceView;
import com.wbw.view.SecondSurfaceView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SecondActivity extends Activity {

	FrameLayout f2;
	LinearLayout l2;
	private Context mContext;
	private int screen_h;
	private int screen_w;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.secondview);
		Display localDisplay = getWindowManager().getDefaultDisplay();
		screen_w = localDisplay.getWidth();
		screen_h = localDisplay.getHeight();
		mContext = getApplicationContext();
		findAllViews();
		createActions();
	}
	
	private void findAllViews(){
		f2 = (FrameLayout) findViewById(R.id.f2);
		l2 = (LinearLayout) findViewById(R.id.l2);
	}
	
	private SecondSurfaceView ssv;
	private void createActions(){
		ssv = new SecondSurfaceView(this, this.screen_w, this.screen_h);
		f2.removeAllViews();
		f2.addView(ssv, new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT));
		
		handler.sendEmptyMessageDelayed(showh, 500L);
		handler.sendEmptyMessageDelayed(showtext, 1500L);
	}

	private final int showh = 1;
	private final int showtext = 2;
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
	      }
	    }
	  };
}
