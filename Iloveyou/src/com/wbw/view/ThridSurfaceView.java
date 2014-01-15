package com.wbw.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;


import com.wbw.iloveyou.R;
import com.wbw.inter.AllSurfaceView;
import com.wbw.util.BitmapCache;
import com.wbw.util.PhysicalTool;
import com.wbw.util.SharedPreferencesXml;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Matrix;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class ThridSurfaceView extends SurfaceView implements
SurfaceHolder.Callback,AllSurfaceView {
	private volatile boolean isallstop = false;
	BitmapCache bitmapcache;
	int w;
	int h;
	private SurfaceHolder holder;
	private Context mContext;
	
	private SharedPreferencesXml spxml;
	public ThridSurfaceView(Context context, int s_w, int s_h) {
		super(context);
		// TODO 自动生成的构造函数存根
		this.setFocusable(true);
		this.setKeepScreenOn(true);
		this.w = s_w;
		this.h = s_h;
		this.mContext = context;
		this.bitmapcache = BitmapCache.getInstance();
		this.holder = getHolder();
		this.holder.addCallback(this);
		//透明
		setZOrderOnTop(true);
		holder.setFormat(PixelFormat.TRANSPARENT); 
		spxml = SharedPreferencesXml.init();
	}

	private Thread ldthread,hsthread,abthread,cdfgthread;
	//是否在加锁
	private HashMap<Integer,Thread> dropthread = new HashMap<Integer, Thread>();
	private int dropnum = 0;
	
	private void dropthread_Add(int dn,Thread dr){
		synchronized (dropthread) {
			
			dropthread.put(dn,dr);
		}
	}
	
	public void dropthread_Remove(int dn){
		synchronized (dropthread) {
			dropthread.remove(dn);
		}
	}
	
	public void dropthread_Iterator(){
		synchronized (dropthread) {
			
		
			Iterator<Integer> iterator = dropthread.keySet().iterator();        
		    while (iterator.hasNext()) {    
		    	Thread t = dropthread.get(iterator.next());
		    	if(t != null && t.isAlive())
		    		t.interrupt();
		        //System.out.println(hm.get(iterator.next()));    
		    }   
		}
	}

	public void setRun(boolean is){
		isallstop = is;
		if(isallstop){
			if(ldthread != null && ldthread.isAlive())
				ldthread.interrupt();
			if(hsthread != null && hsthread.isAlive())
				hsthread.interrupt();
			if(abthread != null && abthread.isAlive())
				abthread.interrupt();
			if(cdfgthread != null && cdfgthread.isAlive())
				cdfgthread.interrupt();
			dropthread_Iterator();
			//ldthread,hsthread,abthread,cdfgthread;
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO 自动生成的方法存根
		
	}

	public Bitmap love_l;
	public int love_l_w,love_l_h;
	private int dest_x,dest_y;  //画变大的心的最左边和最上边位置
	private int beau_w,beau_h;
	int dnum =20;
	
	int xadd = 0;
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		int ii = 0;
		// 画背景，因为surfaceview用的是双缓冲，所以一定要画两次，把前景和缓冲背景都画上，画任何东西都一样
		//但如果不锁全屏，其实是不用的
		Bitmap beau = bitmapcache.getBitmap(R.drawable.beau1, mContext);  //仙女
		Bitmap stick = bitmapcache.getBitmap(R.drawable.stick, mContext);  //魔棒
		Bitmap big = bitmapcache.getBitmap(R.drawable.big1, mContext);
	
		
		if(w>=500) xadd = xadd+10;
		if(w>=600) xadd = xadd+20;
		if(w>=800) xadd = xadd+20;
		
		if(w>=1000) xadd = xadd+30;
		if(w>=1500) xadd = xadd+30;
 		
		Paint p = new Paint();
		dest_x = 163+big.getWidth()/2 +xadd;  //变大的心的起点
		dest_y = 190;
		beau_w = beau.getWidth();
		beau_h = beau.getHeight();
		//
		dropx = 160 +xadd;  //下缀的起始位置
		dropy = dest_y;
		Bitmap llove = bitmapcache.getBitmap(R.drawable.big_h, mContext);
		int bw = llove.getWidth();
		int bh = llove.getHeight();
		dropw = bw+1;
		droph = bh+1;
		
		while (ii < 3  && !isallstop) {
			synchronized (holder) {
				Canvas c = null;
				try {
					c = holder.lockCanvas();					
					c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
					c.drawBitmap(beau, 0+xadd,0, p);
					c.drawBitmap(stick, dropx-5 ,dropy-70, p);
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
		}
		
		love_l = bitmapcache.getBitmap(R.drawable.love, mContext);
		love_l_h = love_l.getHeight();
		love_l_w = love_l.getWidth();
		//先决定字位置，再决定出现的小爱心位置
		drawText();
		makeLoveDot(dnum);
		//小爱心线程
		 ldthread = new LoveDotThread();
		ldthread.start();
		//变大的心
		 hsthread = new HeartShowThread();
		hsthread.start();
		
		
	}
	
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		
	}
	
	//定义位置
	private int cx,dx,fx,gx,ay,by;
	private int maxziy;
	private void drawText(){
//		String a = "一生一世";
//		String b = "不离不弃";
//		String c = "生死挈阔";
//		String d = "与子成说";
//		String f = "执子之手";
//		String g = "与子偕老";
		String aa = spxml.getResourceString(R.string.thrid_f_et_1);
		String bb = spxml.getResourceString(R.string.thrid_f_et_2);
		String cc = spxml.getResourceString(R.string.thrid_s_et_1);
		String dd = spxml.getResourceString(R.string.thrid_s_et_2);
		String ff = spxml.getResourceString(R.string.thrid_s_et_3);
		String gg = spxml.getResourceString(R.string.thrid_s_et_4);
		
		String a = spxml.getConfigSharedPreferences("thrid_f_et_1", aa);
		String b = spxml.getConfigSharedPreferences("thrid_f_et_2", bb);
		String c = spxml.getConfigSharedPreferences("thrid_s_et_1", cc);
		String d = spxml.getConfigSharedPreferences("thrid_s_et_2", dd);
		String f = spxml.getConfigSharedPreferences("thrid_s_et_3", ff);
		String g = spxml.getConfigSharedPreferences("thrid_s_et_4", gg);
		if(a.length()>4) a = a.substring(0, 4);
		else if(a.length()<4) a = aa;
		if(b.length()>4) b = b.substring(0, 4);
		else if(b.length()<4) b = bb;
		if(c.length()>4) c = c.substring(0, 4);
		else if(c.length()<4) c = cc;
		if(d.length()>4) d = d.substring(0, 4);
		else if(d.length()<4) d = dd;
		if(f.length()>4) f = f.substring(0, 4);
		else if(f.length()<4) f = ff;
		if(g.length()>4) g = g.substring(0, 4);
		else if(g.length()<4) g = gg;
		
		StringKind[] ab = new StringKind[2];
		ay = 100;
		by = 200;
		StringKind a_kind = new StringKind(a,0,beau_w+20+xadd,ay,10);
		StringKind b_kind = new StringKind(b,0,beau_w+20+xadd,by,10);
		ab[0] = a_kind;
		ab[1] = b_kind;
		abthread = new drawTextThread(ab);
		abthread.start();
		
		int w5 = w/4-20;
		
		 cx = w-w5+10;
		 dx = w-2*w5+20;
		//int fx = w-3*w5;
		//int gx = w-4*w5;
		 fx = dropx+dropw-5+xadd;
		 gx = dropx+5-textsize-xadd;
		
		cx = checkTextXInDrop(cx);
		dx = checkTextXInDrop(dx);
		fx = checkTextXInDrop(fx);
		gx = checkTextXInDrop(gx);
		
		int cy = 300,dy = 350,fy=400,gy=450;
		int cg_space = 20;
		
		if(h<=700) {
			textsize = 40;
			cg_space = 10;
			dy = 330;
			fy = 360;
			gy = 390;
		}else if(h<=750){			
				textsize = 40;
				cg_space = 15;
				dy = 335;
				fy = 370;
				gy = 405;			
		}else if(h<=850){
			textsize = 45;
			cg_space = 15;
			dy = 340;
			fy = 380;
			gy = 420;
		}else if(h>=1000 && h<=1200){
			textsize = 60;
			cg_space = 25;
			cy = 370;
			dy = 430;
			fy = 490;
			gy = 550;
		}else if(h>1200){
			textsize = 65;
			cg_space = 25;
			cy = 400;
			dy = 460;
			fy = 520;
			gy = 580;
		}
		
		StringKind[] cdfg = new StringKind[4];
		StringKind c_kind = new StringKind(c,1,cx,cy,cg_space);
		StringKind d_kind = new StringKind(d,1,dx,dy,cg_space);
		StringKind f_kind = new StringKind(f,1,fx,fy,cg_space);
		StringKind g_kind = new StringKind(g,1,gx,gy,cg_space);
		
		maxziy = gy+4*(textsize+20);
		cdfg[0] = c_kind;
		cdfg[1] = d_kind;
		cdfg[2] = f_kind;
		cdfg[3] = g_kind;
		cdfgthread = new drawTextThread(cdfg);
		cdfgthread.start();
	}
	
	//检查是否在drop内
	private int checkTextXInDrop(int x){
		System.out.println("x:"+x+" dropx:"+dropx+" dropw:"+dropw);
		if(x>=dropx && x<= dropx+dropw){
			if(x< dropx+dropw/2) x = dropx-dropw/2;
			else x = dropx+dropw+10;
		}
		return x;
		//return 
	}
	
	int textsize = 55;
	class drawTextThread extends Thread{
		//String text;
		//int or,drawx,drawy,space;
		StringKind[] sk;
		/**
		 * or为0横，为1竖向
		 * @param text
		 * @param orentation
		 * @param drawx
		 * @param drawy
		 * @param space
		 */
		public drawTextThread(StringKind[] sk){
			this.sk = sk;
		}
		
		public void run(){
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			for(int i =0;i<sk.length && !isallstop;i++){
				drawT(sk[i]);
				
			}
		}
		
		private void drawT(StringKind s){
			String text = s.text;
			int or = s.or;
			int drawx = s.drawx;
			int drawy = s.drawy;
			int space = s.space;			
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);//设置非填充
			//paint.setStrokeWidth(5);//笔宽5像素
			paint.setColor(Color.RED);//设置为红笔
			paint.setAntiAlias(true);//锯齿不显示
			paint.setTextSize(textsize);
			paint.setStrokeCap(Paint.Cap.ROUND);  //设置笔刷的样式
			//measureText
			//serif 衬线字体（即末端加强）， 常用于正文
			//Italic 不是斜体 Italic 顾名思义，是意大利体
			paint.setTypeface(Typeface.create(Typeface.SERIF,Typeface.ITALIC));
			paint.setTextAlign(Align.LEFT);
			char[] c =text.toCharArray();
			for(int i = 0; i<c.length && !isallstop;i++){  //字								
				Rect r = new Rect();
				paint.getTextBounds(c, i, 1, r);
				int tw = r.right-r.left;
				int th = r.bottom - r.top;			
				for(int j =1;j<=15 && !isallstop;j++){  //缓冲出现 					
					if(j<10){
						paint.setAlpha(j*10);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}else {
						paint.setAlpha(j*15);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
					synchronized (holder) {
						Canvas ca = null;
						try {
							Rect rtt = new Rect(drawx-textsize,drawy-textsize,drawx+textsize,drawy+textsize);
							ca = holder.lockCanvas(rtt);		
							Xfermode xFermode = new PorterDuffXfermode(Mode.SRC_OVER);
							paint.setXfermode(xFermode);
							ca.drawText(String.valueOf(c[i]), drawx, drawy, paint);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try{
								if (c != null){
									holder.unlockCanvasAndPost(ca);// 结束锁定画图，并提交改变。
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}

					}
					
				}//for缓冲
				if(or == 0){  //x++
					drawx = drawx+textsize+space;
				}else drawy = drawy +textsize+space;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}//for字
			
		}
	}
	
	private int dropx ,dropy,dropw,droph;
	class HeartShowThread extends Thread{
		public void run(){
			show();
		}
		
		public void show(){
			int ii = 30;
			boolean run = true;
			Paint p = new Paint();
			
			//旋转的花
			Bitmap hua = bitmapcache.getBitmap(R.drawable.hua, mContext);
			int huax = dest_x-40+xadd/2;
			int huay = 30;
			int huaw = hua.getWidth();
			int huah = hua.getHeight();
			int huamax = 180;
			int huamin = 0;
			int hua_add_plus = 2;
			int huar=0;	
			int re_num = 0;
			
			//星
			Bitmap xin1 = bitmapcache.getBitmapByLM(R.drawable.xin1, mContext,2);
			Bitmap xin2 = bitmapcache.getBitmapByLM(R.drawable.xin2, mContext,2);
			int xin1w = xin1.getWidth();
			int xin1h = xin1.getHeight();
			int xin2w = xin2.getWidth();
			int xin2h = xin2.getHeight();
			//三个星1，三个星2
			ArrayList<LoveDot> xinall = new ArrayList<LoveDot>();
			xinall.add(new LoveDot(1+xadd,10,1));
			xinall.add(new LoveDot(48+xadd,18,1));
			xinall.add(new LoveDot(110+xadd,40,1));
			xinall.add(new LoveDot(20+xadd,150,2));
			xinall.add(new LoveDot(150+xadd,160,2));
			xinall.add(new LoveDot(130+xadd,190,2));
			
			boolean xinboolean = true;  //为真的时候画星,为假的时候擦除
			int oldx = 0;
			while (true  && !isallstop) {
				try {
					Thread.sleep(150);
				} catch (InterruptedException e2) {
					// TODO 自动生成的 catch 块
					e2.printStackTrace();
				}
				
				//旋转加透明
				synchronized (holder) {
					Canvas c = null;
					Bitmap b2 = null;	
					try {
						//c.drawColor(co);					
						Matrix m = new Matrix();
						m.setRotate(huar);
						p.setAlpha(255-Math.abs(huar));
						b2 = Bitmap.createBitmap(
									hua, 0, 0, huaw,huah, m, true);	
						c = holder.lockCanvas(new Rect(huax,huay,huax+b2.getWidth(),
								huay+b2.getHeight()));
						c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
						c.drawBitmap(b2, huax,huay, p);
						//c.drawBitmap(big, dest_x, dest_y, p);
						
						huar = huar+hua_add_plus;
						if(huar==huamax) hua_add_plus = -2;
						if(huar == huamin) hua_add_plus = 2;
						
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
						//if(b2 != null)
						//	b2.recycle();
					}
				}
				
				
				//星星闪烁
				//为真的时候画星,为假的时候擦除
				if(xinboolean){
					
					LoveDot d = xinall.get(oldx);
					Bitmap xinb = null;
					int xw,xh;
					int xx = d.x;
					int yy = d.y;
					if(d.num == 2){
						xinb = xin2;
						xw = xin2w;
						xh = xin2h;
					}
					else {
						xinb = xin1;
						xw = xin1w;
						xh = xin1h;
					}
					
					synchronized (holder) {
						Canvas c = null;
						try {
							c = holder.lockCanvas(new Rect(xx,yy,xx+xw,yy+xh));							
							p.setAlpha(255);
							//c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
							c.drawBitmap(xinb, xx,yy, p);
						
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
					//oldx = thisone;
					xinboolean = !xinboolean;
				}else{
					int thisone = getRandom(0, xinall.size()-1);
					LoveDot d = xinall.get(thisone);
					int xw,xh;
					int xx = d.x;
					int yy = d.y;
					if(d.num == 2){
						//xinb = xin2;
						xw = xin2w;
						xh = xin2h;
					}
					else {
						//xinb = xin1;
						xw = xin1w;
						xh = xin1h;
					}
					synchronized (holder) {
						Canvas c = null;
						try {
							c = holder.lockCanvas(new Rect(xx,yy,xx+xw,yy+xh));							
							p.setAlpha(255);
							c.drawColor(Color.TRANSPARENT,Mode.CLEAR);						
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
					oldx = thisone;
					xinboolean = !xinboolean;
				}
				
				
				
				re_num++;
				if(re_num>3){
					re_num = 0;
				}else continue;
				
				Bitmap big15 = bitmapcache.getBitmap(R.drawable.big99, mContext);
				//Y为dest ,x 为dest - w/2
				int bw = big15.getWidth();
				int bh = big15.getHeight();
				Bitmap mBitmap = Bitmap.createScaledBitmap(big15, bw-ii, bh-ii, true);  
				bw = mBitmap.getWidth();
				bh = mBitmap.getHeight();
				int x = dest_x-bw/2;
				int y = dest_y;
				//
				dropx = x;
				dropy = y;
				//dropw = ,droph
				synchronized (holder) {
					Canvas c = null;
					try {
						c = holder.lockCanvas(new Rect(x-1,y-1,x+1+bw,y+1+bh));					
						p.setAlpha(255);
						c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
						c.drawBitmap(mBitmap, x,y, p);
						//c.drawBitmap(big, dest_x, dest_y, p);
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
				ii--;
				if(ii <=0) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					ii = 30;

					synchronized (holder) {
						Canvas c = null;
						try {
							c = holder.lockCanvas(new Rect(x-1,y-1,x+1+bw,y+1+bh));
							//c.drawColor(co);
							c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
							//c.drawBitmap(big15, x,y, p);
							//c.drawBitmap(big, dest_x, dest_y, p);
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
					if(!isallstop){
						dropnum++;
						Thread drop = new LoveDrop(dropnum,x,y);
						drop.start();
						dropthread_Add(dropnum, drop);
						//new LoveDrop(x,y).start();
					}
							
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	class LoveDrop extends Thread{
		int numkey;
		private LoveDrop(int numkey,int x,int y){
			startx = x;
			starty = y+10;
			this.numkey = numkey;
		}
		public void run(){
			drop();
			dropthread_Remove(numkey);
		}
		int startx,starty;
		int endy,endx;
		private void drop(){
			//startx = dest_x;
			//starty = dest_y+10;
			endy = h-100;  //最多下落到这
			endx = startx;
			Bitmap llove = bitmapcache.getBitmap(R.drawable.big_h, mContext);
			int bw = llove.getWidth();
			int bh = llove.getHeight();
			Paint p = new Paint();
			boolean isr = true;
			long de = 60;
			dropw = bw+1;
			droph = bh+1;
			while(isr && !isallstop){
				synchronized (holder) {
					Canvas c = null;
					try {
						//Xfermode xFermode = new PorterDuffXfermode(Mode.DST_ATOP);
						//p.setXfermode(xFermode);
						c = holder.lockCanvas(new Rect(startx,starty-2,startx+bw,starty+bh+1));						
						c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
						c.drawBitmap(llove, startx,starty++, p);						
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
				try {
					Thread.sleep(de);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				de=(long) (de-0.05);
				if(de <=40) de=(long) (de-0.01);
				if(de <=20) de = 20;
				if(starty >= endy && !isallstop){//准备往左右
					isr = false;  //结束线程
					 // 初始化y轴数据					
					int centerY = endy;
					//int[] Y_axis = new int[w-startx];
					int left_right = getRandom(0, 10);
					boolean isright = true;  //为真为右，为假为左，为右的机会大些
					if(left_right<4){  //左
						isright = false;
					}
					int le,top;
					
					int maxhh = endy - maxziy;
					int rmin = 80,rmax = 100;
					int lmin = 40,lmax = 50;
					if(maxhh <100 && maxhh>50 ) {
						rmin = 50;
						rmax = 60;
					}
					if(maxhh < 50){
						rmin = 30;
						rmax = 40;
						lmin = 20;
						lmax = 30;
					}
					
					if(isright){
						le = w-startx;
						top = getRandom(rmin, rmax);
					}
					else {
						le = startx+bw;
						top = getRandom(lmin,lmax);
					}
					for (int i = 1; i < le && !isallstop; i++) {// 计算正弦波
						int x;
						if(isright)
							x = startx+i;
						else x = startx-i;
						//y=Asin（ωx+φ φ（初相位）：决定波形与X轴位置关系或横向移动距离（左加右减）
						//ω：决定周期（最小正周期T=2π/|ω|）
						//		A：决定峰值（即纵向拉伸压缩的倍数） 	
						int y = centerY-Math.abs( (int) (top * Math.sin(i * 2 * Math.PI/ 180)));
						synchronized (holder) {
							Canvas c = null;
							try {
								c = holder.lockCanvas(new Rect(x-2,
										y-15, x + bw+1, y + bh
												+ 15));								
								c.drawColor(Color.TRANSPARENT, Mode.CLEAR);
								c.drawBitmap(llove, x, y, p);								
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

						}// sy
						int delay = endy - y;
						try {
							//顶峰慢，delay 大，越慢，时间长
							Thread.sleep(50+delay*2);
							//System.out.println("y:"+y);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}	
				}
			}
		}
	}
	
	private Vector<LoveDot> lovelist = new Vector<LoveDot>();
	private void makeLoveDot(int num){
		lovelist.clear();
		for(int i=0;i<num;i++){
			LoveDot ld = new LoveDot();
			int[] get = getRandom(0, w,0,h);
			ld.x = get[0];
			ld.y = get[1];
			lovelist.add(ld);
		}
	}
	
	
	
	class LoveDotThread extends Thread{
		
		public void run(){
			showAlldot();
		}
		
		private void showAlldot(){
			boolean isrun = true;
			Paint paint = new Paint();
			Xfermode xFermode = new PorterDuffXfermode(Mode.DST_OVER);
			paint.setXfermode(xFermode);
			while(isrun && !isallstop){
				//下一个小心
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				//Xfermode xFermode = new PorterDuffXfermode(Mode.DST_OVER);
				//paint.setXfermode(xFermode);
				synchronized (lovelist) {
					//小于就添加
					if(lovelist.size()<dnum){						
							LoveDot ld = new LoveDot();
							int[] get = getRandom(0, w,0,h);
							ld.x = get[0];
							ld.y = get[1];
							
							lovelist.add(ld);					
					}
						int i = getRandom(0, lovelist.size()-1);
						LoveDot ld = lovelist.get(i);
						synchronized (holder) {
							Canvas c = null;
							Rect rt = null;							
							try {
								rt = new Rect(ld.x,ld.y,ld.x+love_l_w,ld.y+love_l_h);
								c = holder.lockCanvas(rt);															
								c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
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
							try {
								Thread.sleep(200);
							} catch (InterruptedException e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
						synchronized (holder) {
							Canvas c = null;
							Rect rt = null;	
							try {
								paint.setAlpha((10-ld.num)*18);
								//这样写最是最低层，不会影响其他
								//不会，还是会被擦除
								//Xfermode xFermode = new PorterDuffXfermode(Mode.DST_ATOP);
								//paint.setXfermode(xFermode);
								rt = new Rect(ld.x,ld.y,ld.x+love_l_w,ld.y+love_l_h);
								c = holder.lockCanvas(rt);								
								c.drawBitmap(love_l,ld.x,ld.y,paint);
								
								
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
							ld.num++;
							if(ld.num>=10 && !isallstop){
								synchronized (holder) {
									Canvas c = null;
									Rect rt = null;	
								try {
									rt = new Rect(ld.x,ld.y,ld.x+love_l_w,ld.y+love_l_h);
									c = holder.lockCanvas(rt);								
									//c.drawColor(co);
									c.drawColor(Color.TRANSPARENT,Mode.CLEAR);
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
								lovelist.remove(i);
							}
							
						}//sy
					//}//for
				}//sy
			}//while
		}
	
	
	private int getRandom(int begin,int end){
		return (int)Math.round(Math.random()*(end-begin)+begin);
	}
	
	//得到随机数,这里得到的随机数有位置限制
	//得到x和y;
		private int[] getRandom(int beginx ,int endx,int beginy, int endy){
			int getx = (int)Math.round(Math.random()*(endx-beginx)+beginx);
			int gety = (int)Math.round(Math.random()*(endy-beginy)+beginy);
			//不能在仙女范围内
			if(getx <= beau_w  && gety <= beau_h){
				if(Math.random()>0.5)
					getx = getx+beau_w;
				else gety = gety+beau_h;
			}
			//检查在竖四行字里
			if(getx>=cx && getx <=cx+textsize)
				getx = cx+textsize+5;
			if(getx>=fx && getx <=fx+textsize)
				getx = fx+textsize+5;
			if(getx>=gx && getx <=gx+textsize)
				getx = gx+textsize+5;
			if(getx>=dx && getx <=dx+textsize)
				getx = dx+textsize+5;
			
			//检查横二行
			if(gety>=ay-textsize && gety<=ay)
				gety = ay+5;
			if(gety>=by-textsize && gety<=by)
				gety = by+5;
			
			int[] get = new int[2];
			get[0] = getx;
			get[1] = gety;
			return get;
		}
		
		private int[] nextXY(int beginx,int beginy,int endx,int endy){
			int gotox,gotoy;
			int[] go = new int[2];
			if(beginx >= endx) gotox = beginx-1;
			else gotox = beginx+1;
			if(beginy >= endy) gotoy = beginy-1;
			else gotoy = beginy+1;
			go[0] = gotox;
			go[1] = gotoy;
			return go;
		}
	
	class LoveDot {
		private int x;
		private int y;
		private int num = 0;
		public LoveDot(){
			
		}
		public LoveDot(int x,int y,int num){
			this.x = x;
			this.y = y;
			this.num = num;
		}
	}
	class StringKind{
		String text;
		int or;
		int drawx;
		int drawy;
		int space;
		private StringKind(String text,int or,int drawx,int drawy,int space){
			this.text =text;
			this.or = or;
			this.drawx = drawx;
			this.drawy = drawy;
			this.space = space;
		}
	}
}
