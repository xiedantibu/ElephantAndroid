package com.elephant.utils;

/*
 * Copyright (C) 2010 Michael Pardo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources.NotFoundException;
import android.os.Build;

/**
 * android应用工具包
 * 
 * @author xlm
 * 
 */
public class ApkUtils {
	private ApkUtils() {
	}

	/**
	 * 获取app名字
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		return getAppName(context, null);
	}

	private static String getAppName(Context context, String packageName) {
		String applicationName;

		if (packageName == null) {
			packageName = context.getPackageName();
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, 0);
			applicationName = context
					.getString(packageInfo.applicationInfo.labelRes);
		} catch (Exception e) {
			L.w("Failed to get version number.", e);
			applicationName = "";
		}

		return applicationName;
	}

	/**
	 * 获取版本名字versionName
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionNumber(Context context) {
		return getAppVersionNumber(context, null);
	}

	private static String getAppVersionNumber(Context context,
			String packageName) {
		String versionName;

		if (packageName == null) {
			packageName = context.getPackageName();
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, 0);
			versionName = packageInfo.versionName;
		} catch (Exception e) {
			L.w("Failed to get version number.", e);
			versionName = "";
		}

		return versionName;
	}

	/**
	 * 获取版本号versionCode
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionCode(Context context) {
		return getAppVersionCode(context, null);
	}

	private static String getAppVersionCode(Context context, String packageName) {
		String versionCode;

		if (packageName == null) {
			packageName = context.getPackageName();
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, 0);
			versionCode = Integer.toString(packageInfo.versionCode);
		} catch (Exception e) {
			L.w("Failed to get version code.", e);
			versionCode = "";
		}

		return versionCode;
	}

	/**
	 * 获取SDK_INT
	 * 
	 * @return
	 */
	public static int getSdkVersion() {
		try {
			return Build.VERSION.class.getField("SDK_INT").getInt(null);
		} catch (Exception e) {
			return 3;
		}
	}

	@Deprecated
	/*
	 * 判断是不是发布版本等同于BuildConfig.DEBUG
	 */
	public static boolean isRelease(Context context) {
		final String releaseSignatureString = "release_signature";
		if (releaseSignatureString == null
				|| releaseSignatureString.length() == 0) {
			throw new RuntimeException(
					"Release signature string is null or missing.");
		}

		final Signature releaseSignature = new Signature("release_signature");

		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature sig : pi.signatures) {
				if (sig.equals(releaseSignature)) {
					L.v("Determined that this is a RELEASE build.");
					return true;
				}
			}
		} catch (Exception e) {
			L.w("Exception thrown when detecting if app is signed by a release keystore, assuming this is a release build.",
					e);

			// Return true if we can't figure it out
			return true;
		}

		L.v("Determined that this is a DEBUG build.");

		return false;
	}

	/**
	 * 判断是不是模拟器
	 * 
	 * @return
	 */
	public static boolean isEmulator() {
		return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
	}

	/**
	 * 指定PackageName包名的应用是否已安装.
	 * 
	 * @param context
	 * @param PackageName
	 * @return
	 * @throws NotFoundException
	 */
	public static boolean hasInstallApk(Context context, String PackageName)
			throws NotFoundException {
		boolean flag = true;
		PackageManager mPm = context.getPackageManager();
		PackageInfo pkgInfo = null;
		try {
			pkgInfo = mPm.getPackageInfo(PackageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (NameNotFoundException e) {
			flag = false;
		} catch (Exception ex) {
			flag = false;
			if (pkgInfo == null) {
				throw new NotFoundException();
			}
		}
		return flag;
	}

	/**
	 * 检查系统中是否存在-这个Activity
	 * 
	 * @param context
	 * @param pkg
	 * @param cls
	 * @return
	 */
	public static boolean hasActivity(Context context, String pkg, String cls) {
		boolean has = true;
		Intent intent = new Intent();
		intent.setClassName(pkg, cls);
		if (context.getPackageManager().resolveActivity(intent, 0) == null) {
			// 说明系统中不存在这个activity
			has = false;
		}
		return has;
	}
}
