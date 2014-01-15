package com.wbw.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;


public class BitmapCache {
    static private BitmapCache cache;
    /** 用于Chche内容的存储 */
    private Hashtable<String, MySoftRef> hashRefs;
    /** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
    private ReferenceQueue<Bitmap> q;

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
      */
    private class MySoftRef extends SoftReference<Bitmap> {
        private String _key = "0";

        public MySoftRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
            super(bmp, q);
            _key = key;
        }
    }

    private BitmapCache() {
        hashRefs = new Hashtable<String, MySoftRef>();
        q = new ReferenceQueue<Bitmap>();
    }

    /**
     * 取得缓存器实例
      */
    public static BitmapCache getInstance() {
        if (cache == null) {
            cache = new BitmapCache();
        }
        return cache;
    }

    /**
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
      */
    private void addCacheBitmap(Bitmap bmp, String key) {
        cleanCache();// 清除垃圾引用
         MySoftRef ref = new MySoftRef(bmp, q, key);
        hashRefs.put(key, ref);
    }

    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     */
    public Bitmap getBitmap(int resId, Context context) {
        Bitmap bmp = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
         if (hashRefs.containsKey(String.valueOf(resId))) {
            MySoftRef ref = (MySoftRef) hashRefs.get(String.valueOf(resId));
            bmp = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
         // 并保存对这个新建实例的软引用
         if (bmp == null) {
            // 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，
              // 无需再使用java层的createBitmap，从而节省了java层的空间。
              bmp = BitmapFactory.decodeStream(context.getResources()
                    .openRawResource(resId));
            this.addCacheBitmap(bmp, String.valueOf(resId));
        }
        return bmp;
    }

    
    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     */
    public Bitmap getBitmap(int resId, Context context,int w,int h) {
        Bitmap bmp = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
         if (hashRefs.containsKey(String.valueOf(resId))) {
            MySoftRef ref = (MySoftRef) hashRefs.get(String.valueOf(resId));
            bmp = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
         // 并保存对这个新建实例的软引用
         if (bmp == null) {
            // 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，
              // 无需再使用java层的createBitmap，从而节省了java层的空间。
              bmp = getBitmapByLM(resId, context, w, h);
            this.addCacheBitmap(bmp, String.valueOf(resId));
        }
        return bmp;
    }
    
   /**
    * 资源ID,需要的长和宽，但出来的并不是绝对的你所要的长和宽，只是按比例来的，只能缩小
    * @param resid
    * @param context
    * @param w
    * @param h
    * @return
    */
	public static Bitmap getBitmapByLM(int resid ,Context context,int w,int h){
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = false;
		Bitmap bit = BitmapFactory.decodeResource(context.getResources(), resid);
		//Bitmap bit = new BitmapFactory().decodeFile(path, op);
		if(bit == null){
			
			return null;
		}
		float realw = bit.getWidth();
		float realh = bit.getHeight();
		int WIDTH_NEED,HEIGHT_NEED;
		
			WIDTH_NEED = w;
			HEIGHT_NEED = h;
		
		int scalew = (int) (realw/WIDTH_NEED);
		int scaleh = (int) (realh/HEIGHT_NEED);
		int scale = (scalew>scaleh?scalew:scaleh);
		if(scale < 1) scale = 1;
		
		System.out.println("图片宽："+ realw+" 长："+realh+"  scale:"+scale);
		
		op.inPreferredConfig = Bitmap.Config.RGB_565;    
	    op.inPurgeable = true;   
	    op.inInputShareable = true; 
	    op.inSampleSize = scale;
	    
	   // bit = new BitmapFactory().decodeFile(path,op);
	    bit = new BitmapFactory().decodeResource(context.getResources(), resid,op);
	    return bit;
	}
	
	 /**
	    * 资源ID,需要的长和宽，n为比例
	    * @param resid
	    * @param context
	    * @param n 
	    * @return
	    */
		public static Bitmap getBitmapByLM(int resid ,Context context,int n){
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inJustDecodeBounds = false;
			Bitmap bit = BitmapFactory.decodeResource(context.getResources(), resid);
			//Bitmap bit = new BitmapFactory().decodeFile(path, op);
			if(bit == null){
				
				return null;
			}
			int scale = n;
			if(scale < 1) scale = 1;
			
			
			op.inPreferredConfig = Bitmap.Config.RGB_565;    
		    op.inPurgeable = true;   
		    op.inInputShareable = true; 
		    op.inSampleSize = scale;
		    
		   // bit = new BitmapFactory().decodeFile(path,op);
		    bit = new BitmapFactory().decodeResource(context.getResources(), resid,op);
		    return bit;
		}
	
    
//    public Bitmap getBitmap(String key,String path,String filename,int kind){
//    	Bitmap bmp = null;
//    	if(hashRefs.containsKey(key)){
//    		MySoftRef ref = hashRefs.get(key);
//    		bmp = ref.get();
//    	}
//    	if(bmp == null){
//    		bmp = getPic.getBitmapByLM(path+filename,kind);
//    		if(bmp == null) return null;
//    		this.addCacheBitmap(bmp, key);
//    	}
//    	return bmp;
//    }
    
    
    private void cleanCache() {
        MySoftRef ref = null;
        while ((ref = (MySoftRef) q.poll()) != null) {
            hashRefs.remove(ref._key);
        }
    }
    
    public void deleteByID(String key){
    	if(hashRefs.containsKey(key)){
    		hashRefs.remove(key);
    		cleanCache();
    		//q
    	}
    }

    /**
     * 清除Cache内的全部内容
     */
    public void clearCache() {

        cleanCache();
        //int i=0;
        Enumeration<String> en = hashRefs.keys();
        while (en.hasMoreElements()) {
        	//i++;
        	//Log.v("CAMERA", "i:"+i);
        	String s = en.nextElement();
        	hashRefs.get(s).get().recycle();
        }
   
        hashRefs.clear();

        System.gc();
        System.runFinalization();
    }
    
    public int getCount(){
    	return hashRefs.size();
    }
    

    
    
    
}