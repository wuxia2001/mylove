package com.wbw.view;

import com.wbw.iloveyou.R;
import com.wbw.util.BitmapCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SecondSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	BitmapCache bitmapcache;
	int co;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;

	public SecondSurfaceView(Context context, int s_w, int s_h) {
		super(context);
		// TODO 自动生成的构造函数存根
		this.setFocusable(true);
		this.setKeepScreenOn(true);
		this.w = s_w;
		this.h = s_h;
		this.mContext = context;
		this.co = context.getResources().getColor(R.color.black);
		this.bitmapcache = BitmapCache.getInstance();
		this.holder = getHolder();
		this.holder.addCallback(this);
		holder.setFormat(PixelFormat.TRANSPARENT); 
	}
	
	public void showh(){
		ShowBigHeart sbh = new ShowBigHeart();
		sbh.start();
	}
	
	public void showW(){
		String t = "瓶：用我一生换你十年天真无邪。\n邪：用我一生天真无邪换你再陪我十年。\n我愿爱你一生一世！" +
				"\n我愿守护你一生精彩！\n认识你，让我的幸福如此悦耳。\n你是我的天，我的举世无双." +
				"\n能想到最甜蜜的事就是我喜欢你的每天\n你也在喜欢我\n一生一世   \n";
		ShowText st = new ShowText(t, 50, 100, 100);
		st.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		System.out.println("create");
		int ii = 0;
		boolean f = true;
		Bitmap bit = bitmapcache.getBitmap(R.drawable.bg_second, mContext,w,h);
		//画背景。一定要至少画两次，要不然会闪烁
		while(f){
			ii++;
			if(ii>3) f = false;
			synchronized (holder) {
				Canvas c = null;
				try {
					c = holder.lockCanvas();					
					Paint p = new Paint();
					p.setAlpha(10*8);
					c.drawBitmap(bit, 0, 0, p);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
				}

			}
			ii++;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO 自动生成的方法存根
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
	}
	
	//本来想画背景渐显，做个动态的效果，但会把前景也同样去掉，帮这个线程没用上
	private class ShowBack extends Thread{
		public void ShowBack(){
			
		}
		
		public void run(){
			int ii = 0;
			Bitmap bit = bitmapcache.getBitmap(R.drawable.bg_second, mContext,w,h);
			boolean f = true;
			while (f) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				ii++;
				if(ii>10) ii = 01;
				synchronized (holder) {
					Canvas c = null;
					try {
						c = holder.lockCanvas();
						c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.SRC_OVER);  
						//c.drawColor(co);
						//bit.
						Paint p = new Paint();
						p.setAlpha(10*ii);
						c.drawBitmap(bit, 0, 0, p);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
					}

				}
				ii++;
			}
		}
	}
	
	
	private class ShowText extends Thread{
		//一些字体特效
		//paint.setMaskFilter(BlurMaskFilter filter)；
        //BlurMaskFilter filter = new BlurMaskFilter(float radius, Blur style);
		  //BlurMaskFilter filter = new BlurMaskFilter(10, Blur.SOLID);
		
//		字体高亮：
//		SpannableStringBuilder style=new SpannableStringBuilder(string); 
//		style.setSpan(new ForegroundColorSpan(Color.RED,start,end,Spannable.SPAN_EX_...)); 
//		textView.setText(style); 
		
		int startx,starty;
		String text;
		public ShowText(String text,int startx,int starty,int speed){
			this.startx = startx;
			this.starty = starty;
			this.text = text;
		}
		
		@Override
		public void run(){
			drawtext();
		}
		
		  private void drawtext(){		    	 
	             System.out.println("createsd");
	             Paint p = new Paint(); //创建画笔
	             p.setColor(Color.WHITE);
	             int size = 22;  //字体大小
	             p.setTextSize(size);
	             String[] allt = text.split("\n");
	            for(int i=0;i<allt.length;i++){
	             int max = allt[i].length();
		             for(int count = 1;count<max+1;count++){
		            	 try {
							Thread.sleep(300);
						} catch (InterruptedException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
		            	 synchronized (holder) {
		     				Canvas c = null;
		     				try {
		     					c = holder.lockCanvas(new Rect(0, starty-size*(i+1)+10*i,
		     							w, starty+size*(i+1)+10*i));
		     					c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
		     					 String tm = allt[i].substring(0,count-1);		                       
		    	                 c.drawText(tm, startx,starty+size*i+10*i, p);
		     				} catch (Exception e) {
		     					e.printStackTrace();
		     				} finally {
		     					if(c != null)
		     					holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
		     				}
	
		     			}
		            	 synchronized (holder) {
			     				Canvas c = null;
			     				try {
			     					c = holder.lockCanvas(new Rect(0, starty-size*(i+1)+10*i,
			     							w, starty+size*(i+1)+10*i));
			     					c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
			     					 String tm = allt[i].substring(0,count);		                       
			    	                 c.drawText(tm, startx,starty+size*i+10*i, p);
			     				} catch (Exception e) {
			     					e.printStackTrace();
			     				} finally {
			     					holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
			     				}
		
			     			}
		             }
	            }       
		    }
	}
	
	private class ShowBigHeart extends Thread {
		public ShowBigHeart() {
		}

		public void run() {
			run_heart() ;
		}

		public void run_heart() {
			int i, j;
			double x, y, r;
			int max = 180;
			//先计算出所有的位置，再去画图
			float[][] x_ff = new float[max][max];
			float[][] y_ff = new float[max][max];
			for (i = 0; i < max; i++) {
				for (j = 0; j < max; j++) {
					double pi = Math.PI;
					r = (pi / 45 * i * (1 - (Math.sin(pi / 45 * j))) * 18);
					x = ((r * (Math.cos(pi / 45 * j)) * (Math.sin(pi / 45 * i)) + w / 2) * 1.01);
					y = ((-r * (Math.sin(pi / 45 * j)) + h / 4) * 1.01);
					x_ff[i][j] = (float) x;
					y_ff[i][j] = (float) y;
				}
			}

			i = 0;
			j = 0;
			for (i = 0; i < max; i++) {

//					//sleep,屏幕
					try {
						Thread.sleep(10);
//						clearAll();
					} catch (InterruptedException e) {
//						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					Canvas c = null;
					int numm = 10;
					
					for (j = 0; j < max; j=j+numm) {
						
						synchronized (holder) {
						try {
							Paint p = new Paint(); // 创建画笔
							p.setColor(Color.RED);
							//找出最大最小
							float xx_min=x_ff[i][j],
									xx_max=x_ff[i][j],
									yy_min=y_ff[i][j],
									yy_max=y_ff[i][j];
							for(int k =0;k<numm;k++){
								float xx_n = x_ff[i][j+k];
								float yy_n = y_ff[i][j+k];
								if(xx_n >= xx_max) xx_max = xx_n;
								if(xx_n <= xx_min) xx_min = xx_n;
								if(yy_n >= yy_max) yy_max = yy_n;
								if(yy_n <= yy_min) yy_min = yy_n;
										
							}
							int xmin,xmax,ymin,ymax;
							if(xx_min == 0) xmin = 0;
							else xmin = (int) (xx_min-5>0?xx_min-5:0);
							if(yy_min == 0) ymin = 0;
							else ymin = (int) (yy_min-5>0?yy_min-5:0);
							xmax = (int) (xx_max+5);
							ymax = (int) (yy_max+5);
							
						
							//c = holder.lockCanvas(new Rect(xi,yi,xa,ya));
							c = holder.lockCanvas(new Rect(xmin,ymin,xmax,ymax));
							
							if(j!=0){
								int m = j-numm;
								for(int k =0;k<numm;k++){
									float xx_n = x_ff[i][m+k];
									float yy_n = y_ff[i][m+k];
									c.drawPoint(xx_n, yy_n, p);
								}
							}
							for(int k =0;k<numm;k++){
								float xx_n = x_ff[i][j+k];
								float yy_n = y_ff[i][j+k];
								c.drawPoint(xx_n, yy_n, p);
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
						}
						}
				}
			}
		}
	}// thread

	
	public void clearAll(){
		int ii = 0;
		while (ii < 3) {
			synchronized (holder) {
				Canvas c = null;
				try {
					c = holder.lockCanvas();
					c.drawColor(co);
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
				}

			}
			ii++;
		}
	}
}
