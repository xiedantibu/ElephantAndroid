/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.elephant.utils;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;

/**
 * 应用存储工具包
 */
public final class StorageUtils {

	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
	private static final String INDIVIDUAL_DIR_NAME = "uil-images";

	private StorageUtils() {
	}

	/**
	 * Returns application cache directory. Cache directory will be created on
	 * SD card <i>("/Android/data/[app_package_name]/cache")</i> if card is
	 * mounted and app has appropriate permission. Else - Android defines cache
	 * directory on device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @return Cache {@link File directory}
	 */
	public static File getCacheDirectory(Context context) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				&& hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			L.w("Can't define system cache directory! The app should be re-installed.");
		}
		return appCacheDir;
	}

	/**
	 * Returns individual application cache directory (for only image caching
	 * from ImageLoader). Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is
	 * mounted and app has appropriate permission. Else - Android defines cache
	 * directory on device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @return Cache {@link File directory}
	 */
	public static File getIndividualCacheDirectory(Context context) {
		File cacheDir = getCacheDirectory(context);
		File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = cacheDir;
			}
		}
		return individualCacheDir;
	}

	/**
	 * Returns specified application cache directory. Cache directory will be
	 * created on SD card by defined path if card is mounted and app has
	 * appropriate permission. Else - Android defines cache directory on
	 * device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @param cacheDir
	 *            Cache directory path (e.g.: "AppCacheDir",
	 *            "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				&& hasExternalStoragePermission(context)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(),
					cacheDir);
		}
		if (appCacheDir == null
				|| (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(
				new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				L.w("Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				L.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Checks if there is enough Space on SDCard
	 * 
	 * @param updateSize
	 *            Size to Check
	 * @return True if the Update will fit on SDCard, false if not enough space
	 *         on SDCard Will also return false, if the SDCard is not mounted as
	 *         read/write
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard());
	}

	/**
	 * get the space is left over on sdcard
	 */
	public static long getRealSizeOnSdcard() {
		if (isExternalStorageMounted()) {
			try {
				File path = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath());
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return availableBlocks * blockSize;
			} catch (Exception e) {
				return -1;
			}
		} else {
			return -1;
		}

	}

	/**
	 * Checks if there is enough Space on phone self
	 * 
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * get the space is left over on phone self
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 检查是sd卡是否可用
	 * 
	 * @return
	 */
	public static boolean isExternalStorageMounted() {

		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);

		return !(!canRead || onlyRead || unMounted);
	}
}
