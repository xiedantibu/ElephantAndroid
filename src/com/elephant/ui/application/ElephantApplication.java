package com.elephant.ui.application;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.elephant.db.FinalDb;
import com.elephant.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.elephant.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.elephant.universalimageloader.cache.memory.MemoryCacheAware;
import com.elephant.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.elephant.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.elephant.universalimageloader.core.ImageLoader;
import com.elephant.universalimageloader.core.ImageLoaderConfiguration;
import com.elephant.universalimageloader.core.assist.QueueProcessingType;
import com.elephant.utils.StorageUtils;

public class ElephantApplication extends Application {

	private static ElephantApplication instance = null;
	private File cacheDir;
	private FinalDb mFinalDb;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initImageLoader(getApplicationContext());
		mFinalDb = FinalDb.create(getApplicationContext(), "elephant_db");
	}

	public static ElephantApplication getInstance() {
		return instance;
	}

	public void initImageLoader(Context context) {
		cacheDir = StorageUtils.getCacheDirectory(instance);
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.memoryCache(memoryCache).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(config);
	}

	/**
	 * 全局通用FinalDb,通过ElephantApplication.getInstance().getFinalDb()调用
	 * 
	 * @return
	 */
	public FinalDb getFinalDb() {
		return mFinalDb;
	}
}
