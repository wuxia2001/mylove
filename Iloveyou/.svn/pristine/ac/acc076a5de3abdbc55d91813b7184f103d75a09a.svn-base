package com.wbw.util;

import java.util.HashMap;

import com.wbw.iloveyou.R;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlay {
	public static final int HEARTV = 1;
	
	private static SoundPlay soundplay = null;
	public static SoundPlay init(){
		if(soundplay == null)
			soundplay = new SoundPlay();
		return soundplay;
	}
	
	private SoundPool soundpool;
	private HashMap<Integer,Integer> soundpoolmap;
	// 音效的音量
		int streamVolume;
	
	private Context mContext;
	public void setContext(Context context){
		this.mContext = context;
	}
	
	public void initSound(){
		soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
		soundpoolmap = new HashMap<Integer,Integer>();
		// 获得声音设备和设备音量
		AudioManager mgr = (AudioManager) mContext
			.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		int heartv_id = soundpool.load(mContext, R.raw.heartv, 0);
		soundpoolmap.put(HEARTV, heartv_id);
	}
	
	/***************************************************************
	 * Function: play(); Parameters: sound:要播放的音效的ID, loop:循环次数 Returns: None.
	 * Description: 播放声音 Notes: none.
	 ***************************************************************/
	public int play(int sound, int uLoop) {
		int sid = soundpool.play(soundpoolmap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
		return sid;
	}
	
	public void pause(int sid){
		soundpool.pause(sid);
	}
	
	public void stop(int sid){
		soundpool.stop(sid);
	}
	
	public void resume(int sid){
		soundpool.resume(sid);
	}
	
	public void clearAll(){
		soundpool.release();
		soundpoolmap.clear();
	}
	
	
	
}
