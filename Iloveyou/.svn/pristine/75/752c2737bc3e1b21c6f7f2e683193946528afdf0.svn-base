package com.wbw.view;


import com.wbw.iloveyou.R;
import com.wbw.util.BitmapCache;
import com.wbw.util.SharedPreferencesXml;
import com.wbw.util.Util;
import com.wbw.widget.cropper.CropImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class PopConfigWindows extends PopupWindow {
	public static final int	PIC_LOVE_TOP_LEFT = 0, PIC_LOVE_TOP_RIGHT=1,PIC_LOVE_BOTTOM_LEFT=2,
			PIC_LOVE_BOTTOM_RIGHT=3,PIC_LOVE_LOVE_CENTER=4,TEXT_NAME_LEFT=5,
			TEXT_NAME_RIGHT=6,TEXT_SYA_LOVE=7;
	public static final String[] NAMES = {"pic_love_top_left","pic_love_top_right","pic_love_bottom_left",
		"pic_love_bottom_right","pic_love_love_center","text_name_left",
		"test_name_right","text_say_love"
	};
	
		
	private SharedPreferencesXml spxml;
	public PopConfigWindows(Context context, final Handler handler,final int type,Bitmap bitmap,String defaultString) {
		 spxml = SharedPreferencesXml.init();
		LayoutInflater inflater = LayoutInflater.from(context);
		View contentView = inflater.inflate(R.layout.popwindows, null);

		ImageView iv_close_scroll_device = (ImageView) contentView
				.findViewById(R.id.iv_close);
		iv_close_scroll_device.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopConfigWindows.this.dismiss();
			}
		});
		
		final EditText et_text = (EditText) contentView.findViewById(R.id.et_text);
		final CropImageView civ_crop = (CropImageView) contentView.findViewById(R.id.civ_crop);
		Button btn_submit = (Button) contentView.findViewById(R.id.btn_submit);
		
		OnClickListener onClickListener = null;
		
		if(type==PIC_LOVE_BOTTOM_LEFT||type==PIC_LOVE_BOTTOM_RIGHT||
				type==PIC_LOVE_TOP_LEFT||type==PIC_LOVE_TOP_RIGHT||
				type==PIC_LOVE_LOVE_CENTER){
			et_text.setVisibility(View.GONE);
			civ_crop.setVisibility(View.VISIBLE);
			civ_crop.setImageBitmap(bitmap);
			//civ_crop.setFixedAspectRatio(false);
//			if(type == PIC_LOVE_LOVE_CENTER){
//				civ_crop.setAspectRatio(320, 200);
//			}else 
//				civ_crop.setAspectRatio(100, 100);
			onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PopConfigWindows.this.dismiss();
					Bitmap croppedImage = civ_crop.getCroppedImage();
					//保存到文件，保存到配置里，放到弱引用里
					String base = Util.base;
					if(base.equals("")){
						 base  = Environment.getExternalStorageDirectory().getPath()+"/iloveyou";
					}
					String name=getName(type);
					String suffix = ".jpg";
					String pathString = base+"/"+name+suffix;
					saveBitmap(pathString,croppedImage);
					
					spxml.setConfigSharedPreferences(name,pathString);
					
					BitmapCache.getInstance().addCacheBitmap(croppedImage, name);
					
					handler.sendEmptyMessage(type);
					
					
				}
			};
		}else if(type == TEXT_NAME_LEFT || type == TEXT_NAME_RIGHT || type == TEXT_SYA_LOVE){
			civ_crop.setVisibility(View.GONE);
			et_text.setVisibility(View.VISIBLE);
			if(defaultString!=null && !defaultString.equals(""))
				et_text.setText(defaultString);
			onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PopConfigWindows.this.dismiss();
					String gettextString = et_text.getText().toString().trim();
					if(gettextString!=null && !gettextString.equals("")){
						String name=getName(type);
						spxml.setConfigSharedPreferences(name,gettextString);
						handler.sendEmptyMessage(type);
					}
				}
			};
		}
		
		if(onClickListener!=null)
			btn_submit.setOnClickListener(onClickListener);

		this.setFocusable(true);
		this.setContentView(contentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		this.setBackgroundDrawable(new BitmapDrawable());

	}
	
	private String getName(int type){
		return NAMES[type];
	}
	
	private void saveBitmap(final String path,final Bitmap bitmap){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Util.init().writeFromInputToSD(path, bitmap);
			}
		}).start();
	}
}
