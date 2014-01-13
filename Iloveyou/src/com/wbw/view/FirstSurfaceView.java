package com.wbw.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.wbw.iloveyou.R;
import com.wbw.util.BitmapCache;
import com.wbw.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FirstSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	// 图片软引用
	BitmapCache bitmapcache;
	// 背景颜色
	int co;
	int w;
	int h;
	// 画爱心的蝴蝶
	int[] heart_all = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
			R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7,
			R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11,
			R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
			R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19 };
	private SurfaceHolder holder;
	private Context mContext;
	ShowBackgroundThread sbthread;
	int[] xin_all = { R.drawable.xin1, R.drawable.xin2, R.drawable.xin3,
			R.drawable.xin4 };
	private boolean isrun = true;

	private Handler handler;

	public FirstSurfaceView(Context context, int s_w, int s_h, Handler handler) {
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
		this.handler = handler;
		goOn();
	}

	// 计数器，三个线程全部运行完时自动跳到新界面
	final CountDownLatch begin = new CountDownLatch(3);

	public void goOn() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				try {
					begin.await();
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				handler.sendEmptyMessage(4);
			}
		}).start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自动生成的方法存根
		System.out.println("create");
		int ii = 0;
		// 画背景，因为surfaceview用的是双缓冲，所以一定要画两次，把前景和缓冲背景都画上，画任何东西都一样
		while (ii < 3) {
			synchronized (holder) {
				Canvas c = null;
				try {
					c = holder.lockCanvas();
					c.drawColor(co);
					Thread.sleep(100);
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

	public void showBackground() // 只是用来初始化，无多大意义
	{
		this.holder = getHolder();
		this.holder.addCallback(this);
	}

	public void showHeart() // 画心
	{
		new showheart(this.holder, "showback", this.w, this.h).start();
	}

	public void showWenzi() // 画文字
	{
		new showWenZi(this.holder, "showwenzi", "", this.w, this.h).start();
	}

	public void showXin() // 画星星
	{
		new ShowBackgroundThread(this.holder, "showback", this.w, this.h)
				.start();
	}

	// 花 type 1为花，2为爱心
	/**
	 * 开始x,开始Y坐标，字的宽，高，文件名字，放大倍数，type为图片种类
	 * @param stx
	 * @param sty
	 * @param w
	 * @param h
	 * @param filename
	 * @param beishu
	 * @param type
	 */
	public void show_I(int stx, int sty, int w, int h, String filename,
			int beishu, int type) {
		int startx = stx, starty = sty;
		try {
			int weith = w;
			int height = h;
			boolean[][] arr = new boolean[weith][height]; // 插入的数组
			String file = filename;
			InputStream ins = Util.init().getAssetsInputStream(mContext, file);
			BufferedReader in = new BufferedReader(new InputStreamReader(ins)); //
			String line; // 一行数据
			int row = 0;
			// 逐行读取，并将每个数组放入到数组中
			StringBuilder sb = new StringBuilder();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			System.out.println(sb.toString());
			in.close();
			String all = sb.toString();
			String[] all_a = all.split("n");
			//先得到坐标
			for (int i = 0; i < all_a.length; i++) {
				String[] all_b = all_a[i].split("b");
				System.out.println();
				for (int j = 0; j < all_b.length; j++) {
					if (all_b[j].equals("0")) {
						arr[j][i] = false;
					} else
						arr[j][i] = true;
				}
			}
			int bei = beishu;
			int dis = 25;
			int old_num = -1;
			for (int j = 0; j < height && isrun; j++) {
				for (int i = 0; i <= weith && isrun; i++) {
					//一定要sleep，要不然其他线程画不了东西
					Thread.sleep(25);
					Random rm = new Random();
					Bitmap bitmap = null;
					int num = 0;
					if (type == 1) {
						num = rm.nextInt(heart_all.length - 1);
						bitmap = bitmapcache
								.getBitmap(heart_all[num], mContext);
					} else if (type == 2) {
						bitmap = bitmapcache.getBitmap(R.drawable.love,
								mContext);
					}
					if (i >= weith) {
						//嘿嘿，好像这段sy可以不要，懒得去验证啦，加了没多大坏处啦
						synchronized (holder) {
							Canvas c = null;
							try {
								Paint p = new Paint(); // 创建画笔
								p.setColor(Color.RED);
								int xx_b = i - 1;
								int yy_b = j;
								c = holder.lockCanvas(new Rect(startx
										+ (int) xx_b * bei - dis, starty
										+ (int) yy_b * bei - dis, startx
										+ (int) xx_b * bei + dis, starty
										+ (int) yy_b * bei + dis));

								if (arr[xx_b][yy_b]) {  
									if (old_num != -1) {
										if (type == 1)
											c.drawBitmap(bitmapcache.getBitmap(
													heart_all[old_num],
													mContext), startx + xx_b
													* bei, starty + yy_b * bei,
													p);
										else if (type == 2) {
											c.drawBitmap(bitmapcache.getBitmap(
													R.drawable.love, mContext),
													startx + xx_b * bei, starty
															+ yy_b * bei, p);
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
							}
							continue;
						}
					}

					synchronized (holder) {
						Canvas c = null;
						try {
							float xx = (float) i;
							float yy = (float) j;
							//不要轻易去锁定整个屏幕
							c = holder.lockCanvas(new Rect(startx + (int) xx
									* bei - dis, starty + (int) yy * bei - dis,
									startx + (int) xx * bei + dis, starty
											+ (int) yy * bei + dis));

							// c = holder.lockCanvas();
							Paint p = new Paint(); // 创建画笔
							p.setColor(Color.RED);
							//下面这段是保证双缓冲能都画上东西，从而不会闪烁
							if (i > 0) {
								int xx_b = i - 1;
								int yy_b = j;
								if (arr[xx_b][yy_b]) {
									if (old_num != -1) {
										if (type == 1)
											c.drawBitmap(bitmapcache.getBitmap(
													heart_all[old_num],
													mContext), startx + xx_b
													* bei, starty + yy_b * bei,
													p);
										else if (type == 2) {
											c.drawBitmap(bitmapcache.getBitmap(
													R.drawable.love, mContext),
													startx + xx_b * bei, starty
															+ yy_b * bei, p);
										}
									}
								}
							}
							if (arr[i][j]) {
								c.drawBitmap(bitmap, startx + xx * bei, starty
										+ yy * bei, p);
							}
							old_num = num;
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
						}
					}
					System.out.print("@");// 替换成你喜欢的图案
					// } else {
					// }
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class ShowBackgroundThread extends Thread {
		private SurfaceHolder holder;
		int sh;
		int sw;

		public ShowBackgroundThread(SurfaceHolder holder, String threadname,
				int sw, int sh) {
			this.holder = holder;
			setName(threadname);
			this.sw = sw;
			this.sh = sh;
		}

		@Override
		public void run() {
			for (int i = 0; i <= 9; i++) {
				//当i等于9时画左下角的蝴蝶，其他时候画星星
				if (i == 9) {
					Bitmap b = bitmapcache
							.getBitmap(R.drawable.hudei, mContext);
					for (int j = 1; j < 11; j++) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

						synchronized (holder) {
							Canvas c = null;
							try {
								//锁右下边一部分
								c = holder.lockCanvas(new Rect(new Rect(
										3 * this.sw / 4, 3 * this.sh / 4,
										this.sw, this.sh)));
								Paint p = new Paint();
								p.setAlpha(j * 10);  //透明度
								c.drawBitmap(b, 3 * this.sw / 4,
										3 * this.sh / 4, p);
							} catch (Exception e) {

								e.printStackTrace();
							} finally {
								holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
							}
						}// syn
					}
					continue;
				}

				//随机位置随机星星
				Random rm = new Random();
				int show_x = rm.nextInt(this.sw);
				int show_y = rm.nextInt(this.sh);
				int xin = rm.nextInt(3);
				Bitmap bit = bitmapcache.getBitmap(xin_all[xin], mContext);
				for (int j = 1; j < 11; j++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

					synchronized (holder) {
						Canvas c = null;
						try {
							c = holder.lockCanvas(new Rect(show_x, show_y,
									show_x + 55, show_y + 55));
							Paint p = new Paint();
							p.setAlpha(j * 10);  //透明度从0开始，渐现
							c.drawBitmap(bit, show_x, show_y, p);
						} catch (Exception e) {

							e.printStackTrace();
						} finally {
							holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
						}
					}// syn
				}
			}
			begin.countDown();
		}
	}

	class showWenZi extends Thread {
		public final int[] mask = { 128, 64, 32, 16, 8, 4, 2, 1 };
		public final String ZK_PATH = "HZK16";
		public final String ENCODE = "GB2312";
		public final String ZK32_PATH = "HZK32F";
		int sw, sh;
		private SurfaceHolder holder;
		int startx = 300, starty = 300;

		private String wenzi;

		public showWenZi(SurfaceHolder holder, String name, String wenzi,
				int w, int h) {
			this.holder = holder;
			setName(name);
			this.sw = w;
			this.sh = h;
			this.wenzi = wenzi;
		}

		@Override
		public void run() {
			System.out.println("create_wenzi");
			holder.setKeepScreenOn(true);
			// show_I(int stx,int sty,int w,int h,String filename,int beishu
			// show_I(3,20,19,19,"array_zhang.txt",9,2);
			show_I(3, 20, 20, 20, "array_chu.txt", 9, 2);
			show_I(sw / 2 + 60, 20, 20, 20, "array_chu.txt", 9, 2);
			// drawText("晚晚");
			begin.countDown();
		}// run

	}

	class showheart extends Thread {
		private SurfaceHolder holder;
		int sh;
		int sw;

		public showheart(SurfaceHolder holder, String threadname, int sw, int sh) {
			this.holder = holder;
			setName(threadname);
			this.sw = sw;
			this.sh = sh;
		}

		public void run() {
			System.out.println("create1");
			this.holder.setKeepScreenOn(true);
			FirstSurfaceView.this.show_I(-50 + this.sw / 2, 15, 7, 9,
					"array_I.txt", 20, 1);
			run_hua_heart();
			FirstSurfaceView.this.show_I(-105 + this.sw / 2, 160 + this.sh / 2,
					10, 10, "array_U.txt", 20, 1);
			begin.countDown();
		}

		private void run_hua_heart() {
			// TODO 自动生成的方法存根
			int startx = sw / 2 - 16, starty = sh / 2 - 68;
			int maxh = 100;  
			int y_dao = starty;
			double begin = 10; // 起始位置
			Random rm = new Random();
			int old_num = -1;
			float old_xx = 0, old_yy = 0;
			for (int i = 0; i < maxh; i++) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

				int hua_num = rm.nextInt(18);
				Bitmap bit = bitmapcache
						.getBitmap(heart_all[hua_num], mContext);
				begin = begin + 0.2;  //密度
				double b = begin / Math.PI;
				double a = 13.5 * (16 * Math.pow(Math.sin(b), 3));  //这里的13.5可以控制大小
				double d = -13.5
						* (13 * Math.cos(b) - 5 * Math.cos(2 * b) - 2
								* Math.cos(3 * b) - Math.cos(4 * b));
				synchronized (holder) {
					Canvas c = null;
					try {
						float xx = (float) a;
						float yy = (float) d;

						c = holder.lockCanvas(new Rect(
								(int) (startx + xx - 40),
								(int) (starty + yy - 40),
								(int) (startx + xx + 40),
								(int) (starty + yy + 40)));
						Paint p = new Paint(); // 创建画笔
						p.setColor(Color.RED);
						//画上一个，要不然会闪烁
						if (old_num != -1) {
							Bitmap bb = bitmapcache.getBitmap(
									heart_all[old_num], mContext);
							c.drawBitmap(bb, startx + old_xx, starty + old_yy,
									p);
						}
						c.drawBitmap(bit, startx + xx, starty + yy, p);
						old_num = hua_num;
						old_xx = xx;
						old_yy = yy;
						// c.drawPoint(startx+xx,starty+yy, p);
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
