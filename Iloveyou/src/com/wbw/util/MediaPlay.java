package com.wbw.util;

import java.io.IOException;
import java.net.URI;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class MediaPlay {
	private static MediaPlay mp;
	public static MediaPlay init(){
		if(mp == null)
			mp = new MediaPlay();
		return mp;
	}
	
	
	MediaPlayer mediaPlayer01 = null;
	public void InitMediaPlay(Context context , int resource){
		mediaPlayer01 = MediaPlayer.create(context, resource);
		mediaPlayer01.setLooping(false);
		//mediaPlayer01.setOnCompletionListener();
		//mediaPlayer01.
	}
	
	public void InitMediaPlay(Context context ,String fpath){
		System.out.println("fa:"+fpath);
		Uri u = Uri.parse(fpath);
		try {
//			mediaPlayer01 = new MediaPlayer();
//			mediaPlayer01.setDataSource(fpath);
			mediaPlayer01 = MediaPlayer.create(context, u);
			mediaPlayer01.setLooping(false);
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
	}
	
	public void play(){
		if(mediaPlayer01 != null)
			mediaPlayer01.start();
	}
	
	public void stop(){
		if(mediaPlayer01 != null)
			mediaPlayer01.stop();
	}
	
	
}
