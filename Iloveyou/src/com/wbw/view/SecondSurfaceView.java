package com.wbw.view;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.wbw.iloveyou.R;
import com.wbw.inter.AllSurfaceView;
import com.wbw.util.BitmapCache;
import com.wbw.util.SharedPreferencesXml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


//全局如果没有锁全屏的，可以不用双界面都画
public class SecondSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback,AllSurfaceView {
	private volatile boolean isallstop = false;
	BitmapCache bitmapcache;
	//int co;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;
	Handler handler;
	private SharedPreferencesXml spxml;
	public SecondSurfaceView(Context context, int s_w, int s_h,Handler handler) {
		super(context);
		// TODO 自动生成的构造函数存根
		this.setFocusable(true);
		this.setKeepScreenOn(true);
		this.w = s_w;
		this.h = s_h;
		this.mContext = context;
		//this.co = context.getResources().getColor(R.color.black);
		this.bitmapcache = BitmapCache.getInstance();
		this.holder = getHolder();
		this.holder.addCallback(this);
		//holder.setFormat(PixelFormat.TRANSPARENT); 
		//透明
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		this.handler = handler;
		goOn();
		spxml = SharedPreferencesXml.init();
	}
	
	private Thread goonthread ;
	private Thread sbh;
	private Thread st;
	private Thread sbk_up,sbk_down,sbk_left,sbk_right;
	public void setRun(boolean is){
		isallstop = is;
		if(isallstop){
			if(goonthread != null && goonthread.isAlive())
				goonthread.interrupt();
			if(sbh != null && sbh.isAlive())
				sbh.interrupt();
			if(st != null && st.isAlive())
				st.interrupt();
			if(sbk_up != null && sbk_up.isAlive())
				sbk_up.interrupt();
			if(sbk_down != null && sbk_down.isAlive())
				goonthread.interrupt();
			if(sbk_left != null && sbk_left.isAlive())
				sbk_left.interrupt();
			if(sbk_right != null && sbk_right.isAlive())
				sbk_right.interrupt();
		}
	}
	
	// 计数器，三个线程全部运行完时自动跳到新界面
		final CountDownLatch begin = new CountDownLatch(2);
		public void goOn() {
			
			goonthread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					try {
						begin.await();
						Thread.sleep(3000);
						handler.sendEmptyMessage(4);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			});
			goonthread.start();
		}
	
		
		//1
	
	public void showh(){
		sbh = new ShowBigHeart();
		sbh.start();
	}
	
	//2
	public void showW(){
		size = 24;  //字体大小
		if(w>=540) size = 25;
		if(w>=640) size = 26;
		if(w>=800) size = 29;
		if(w>=1000) size = 31;
		if(w>=1200) size = 33;
		
		String se = spxml.getResourceString(R.string.second_words);
		String t = 
				spxml.getConfigSharedPreferences("second_words", se); 
		if(t.equals("")) t = se;
		st = new ShowText(t, 70, 110, 100);
		st.start();
	}
	
	
	//画背景  3
	public void showB(){
		//ShowBack sb = new ShowBack();
		//sb.start();
	}
	
	
	//画边框  //7
	public void showBK(){
		 sbk_up = new ShowBianKuan(1);
		sbk_up.start();
		sbk_down = new ShowBianKuan(2);
		sbk_down.start();
		sbk_left = new ShowBianKuan(3);
		sbk_left.start();
		sbk_right = new ShowBianKuan(4);
		sbk_right.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		System.out.println("create");
		showB();

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
	
	//本来想画背景渐显，做个动态的效果，但会把前景也同样去掉，这个线程没用上
	//再改，动态背景，只能亮不能暗
	private class ShowBack extends Thread{
		public void ShowBack(){
			
		}
		
		public void run(){
			int ii = 0;
			Bitmap bit = bitmapcache.getBitmap(R.drawable.bg_second, mContext,w,h);
			boolean f = true;
			while (f  && !isallstop) {
				
				ii++;
				if(ii>=4) f = false;
				synchronized (holder) {
					Canvas c = null;
					try {
						c = holder.lockCanvas();
						c.save();
//						c.drawColor(Color.TRANSPARENT,
//								android.graphics.PorterDuff.Mode.SCREEN);  
						Paint p = new Paint();
						p.setAlpha(20*ii);
						c.drawBitmap(bit, 0, 0, p);
						c.restore();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}

				}
				ii++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
			
			//begin.countDown();
		}
	}
	
	private int size;
	private class ShowText extends Thread{
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
			begin.countDown();
		}
		
		  private void drawtext(){	
			 //int C =  mContext.getResources().getColor(R.color.huang);
	             System.out.println("createsd");
	             Paint p = new Paint(); //创建画笔
	        	 int C =  mContext.getResources().getColor(R.color.huang);
		     	String getc = spxml.getConfigSharedPreferences("second_color", String.valueOf(C));
		            try{
		            	p.setColor(Integer.valueOf(getc));	            	
		            }catch(Exception e){
		            	e.printStackTrace();
		            	p.setColor(C);
		            	
		            }
	             //int size = 24;  //字体大小
	             p.setTextSize(size);
	             Xfermode xFermode = new PorterDuffXfermode(Mode.SRC_OVER);
	             p.setXfermode(xFermode);
	             String[] allt = text.split("\n");
	            for(int i=0;i<allt.length && !isallstop;i++){
	             int max = allt[i].length();
		             for(int count = 1;count<max+1 && !isallstop;count++){
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
		     					String tm_old = allt[i].substring(0,count-1);
		     					String tm = allt[i].substring(0,count);	
		     					c.drawText(tm_old, startx,starty+size*i+10*i, p);
		    	                 c.drawText(tm, startx,starty+size*i+10*i, p);
		     				} catch (Exception e) {
		     					e.printStackTrace();
		     				} finally {
		     					try{
									if (c != null){
										holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
									}
								}catch(Exception e){
									e.printStackTrace();
								}
		     				}
	
		     			}
		            	 synchronized (holder) {
			     				Canvas c = null;
			     				try {
			     					c = holder.lockCanvas(new Rect(0, starty-size*(i+1)+10*i,
			     							w, starty+size*(i+1)+10*i));
			     					c.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.OVERLAY);  
			     					String tm_old = allt[i].substring(0,count-1);
			     					String tm = allt[i].substring(0,count);	
			     					c.drawText(tm_old, startx,starty+size*i+10*i, p);		                       
			    	                 c.drawText(tm, startx,starty+size*i+10*i, p);
			     				} catch (Exception e) {
			     					e.printStackTrace();
			     				} finally {
			     					try{
										if (c != null){
											holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
										}
									}catch(Exception e){
										e.printStackTrace();
									}
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
			begin.countDown();
			//handler.sendEmptyMessage(4);
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
			for (i = 0; i < max && !isallstop; i++) {

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
					
					for (j = 0; j < max && !isallstop; j=j+numm) {
						
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
							try{
								if (c != null){
									holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						}
				}
			}
		}
	}// thread


	
	/**
	 * type 1 上部 up
	 * type 2 下部 down
	 * type 3 左部 left
	 * type 4 右部 right
	 * @author wbw
	 *
	 */
	private class ShowBianKuan extends Thread{
		private int type;
		private int startx,endx,starty,endy;
		private int ro_de_begin,ro_de_end;
		private Set<Bitmap> bit_set = new HashSet<Bitmap>();
		private int jiajuli;  //每次要加的距离
		public ShowBianKuan(int type){
			this.type = type;
		}
		int r_angle = 90;
		private int maxal = 200,minal = 150;  //大爱心
		private int max_little_al = 200,min_little_al = 100;
		
		public void run(){
			
			if(type == 1){
				startx = 0;
				endx = w;
				starty = 0;
				endy = 50;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawUp(type);
				drawUpDown(type);
			}else if(type == 2){
				startx = 0;
				endx = w;
				starty = h-60-50;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawUp(type);
				drawUpDown(type);
			}else if(type == 3){
				startx = 0;
				endx = 40;
				starty = 50;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				drawLeftRight();
			}else if(type == 4){
				startx = w-50;
				endx = w;
				starty = 50;
				endy = h-60;
				ro_de_begin = -10;
				ro_de_end = 10;
				drawLeftRight();
			}

			
			for (Bitmap bit_get: bit_set) {  
			      bit_get.recycle(); 
			}  
			bit_set.clear();
			//begin.countDown();
		}
		
		private int[] bit_large = {R.drawable.rightdown1,R.drawable.right1,R.drawable.right4};
		/**
		 * kind 1 up
		 * king 2 down
		 * @param kind
		 */
		private void drawUp(int kind){
			boolean isr = true;		
			Paint p = new Paint();
			int albe ;
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			
			int isup = 0;  //标记为Y位置			
			int maxjia = 2;  //最大个数
			
			int beginx = startx;
			int beginy;
			jiajuli = 20;			
			beginy = starty;
			albe = minal;
			
			//boolean isup = true;
			while(isr && !isallstop){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}	
				albe = albe+5;
				if(albe>=maxal) albe = minal;
				p.setAlpha(albe);
				//画正常的
				Bitmap bit_rotate = null;
				
				int position = getRandom(0, bit_large.length-1);
				bit_rotate = rotateBitmap(bit_large[position], 
							ro_de_begin, ro_de_end);	
					
				if(bit_rotate == null) continue;
				int bith = bit_rotate.getHeight();
				int bitw = bit_rotate.getWidth();
				//画出的图在框架内
				int xx = beginx;
				int yy = beginy;
				
				synchronized (holder) {
					Canvas c = null;
					Rect rt = null;
					try {
						if(bit_old != null){
							int xx_r,yy_r,w_r,h_r;
							xx_r= xx>oldx?oldx:xx;
							yy_r = yy>oldy?oldy:yy;
							w_r = bit_old.getWidth();
							h_r = bit_old.getHeight();
							rt = new Rect(xx_r-10,yy_r-10,
									xx_r+w_r+bitw,yy_r+h_r+bith-jiajuli);
						
						}else
							rt = new Rect(xx-10,yy-10,xx+bitw+20,yy+bith+20);
						
						c = holder.lockCanvas(rt);
						if(bit_old != null){
							c.drawBitmap(bit_old, oldx, oldy, p);
						}
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						bit_set.add(bit_rotate);
						oldx = xx;
						oldy = yy;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}			
				
				if(beginx >endx) isr= false;
				beginx = beginx+bitw-10;	
				
				isup++;
				if(isup >= maxjia) isup =0;
				
				if(isup == 0){
					beginy = starty;
				}else if(isup == 1){
					beginy = starty + jiajuli;
				}else if(isup == 2){
					beginy = starty + jiajuli +jiajuli;
				}
				
			}
		}
		
		private void drawLeftRight(){
			boolean isr = true;		
			Paint p = new Paint();
			int albe ;
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			int beginx = startx;
			int beginy;
			jiajuli = 20;			
			beginy = starty;
			albe = minal;
			
			//boolean isup = true;
			while(isr  && !isallstop){
				try {
					//if(type == 3)
					//	Thread.sleep(1500);
					 Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}	
				albe = albe+5;
				if(albe>=maxal) albe = minal;
				p.setAlpha(albe);
				//画正常的
				Bitmap bit_rotate = null;
				
				int position = getRandom(0, bit_large.length-1);
				bit_rotate = rotateBitmap(bit_large[position], 
							ro_de_begin, ro_de_end);	
					
				if(bit_rotate == null) continue;
				int bith = bit_rotate.getHeight();
				int bitw = bit_rotate.getWidth();
				//画出的图在框架内
				int xx = beginx;
				int yy = beginy;
				
				synchronized (holder) {
					Canvas c = null;
					Rect rt = null;
					try {

						rt = new Rect(xx-10,yy-10,xx+bitw+20,yy+bith+20);						
						c = holder.lockCanvas(rt);
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						bit_set.add(bit_rotate);
						oldx = xx;
						oldy = yy;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}			
				
				beginy = beginy+bith-5;			
				if(beginy >= endy-40) isr= false;
			}
		}
		
		private int[] dianzui = {R.drawable.love,R.drawable.love_middle_down,R.drawable.ulr1};
		//小图标随机点缀
		private void drawUpDown(int type){
			boolean isr = true;
			int num = 0;  //画方向图片下标
			Paint p = new Paint();			
			
			Bitmap bit_old = null;
			int oldx=-1,oldy = -1;
			
			while(isr && !isallstop){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}	
				int pa = getRandom(min_little_al,max_little_al);
				p.setAlpha(pa);
				
				//画正常的
				int position = getRandom(0, dianzui.length-1);
				Bitmap bit_rotate = rotateBitmap(dianzui[position], 
						ro_de_begin, ro_de_end);
				if(bit_rotate == null) continue;
				int bitw = bit_rotate.getWidth();
				int bith = bit_rotate.getHeight();
				//画出的图在框架内
				int xx;
				
				xx = getRandom(startx,endx);
				int yy ;
				yy = getRandom(starty,endy);
				
				synchronized (holder) {
					Canvas c = null;
					Rect rt = null;
					try {
						
						rt = new Rect(xx,yy,xx+bitw,yy+bith);						
						c = holder.lockCanvas(rt);
						if(bit_old != null){
							c.drawBitmap(bit_old, oldx, oldy, p);						
						}
						c.drawBitmap(bit_rotate, xx, yy, p);
						bit_old = bit_rotate;
						bit_set.add(bit_rotate);
						oldx = xx;
						oldy = yy;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try{
							if (c != null){
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}	
				num++;
				if(num>30) isr = false;
			}
		}
		
		//得到旋转的图片，记得回收
		private Bitmap rotateBitmap(int resourceid , int ro_begin,int ro_end){
			int de = getRandom(ro_begin, ro_end);
			Bitmap b = bitmapcache.getBitmap(resourceid, mContext);
			Matrix m = new Matrix();
			m.setRotate(de,
					(float) b.getWidth() / 2, (float) b.getHeight() / 2);
			Bitmap b2 = null;
			if(!b.isRecycled())
				b2 = Bitmap.createBitmap(
						b, 0, 0, b.getWidth(), b.getHeight(), m, true);
			
			return b2;
		}

	}
	
	
	//得到随机数
	private int getRandom(int begin ,int end){
		return (int)Math.round(Math.random()*(end-begin)+begin);
	}
	
	
	
}
