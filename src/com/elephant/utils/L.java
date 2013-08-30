package com.elephant.utils;

import android.util.Log;

import com.elephant.BuildConfig;
import com.elephant.universalimageloader.core.ImageLoader;

/**
 * log工具包
 * 
 * @author xlm
 * 
 */

public final class L {

	/**
	 * log name
	 */
	private static final String TAG = "DEUBG";
	private static final String LOG_FORMAT = "%1$s\n%2$s";

	public static void e(Object object, String err) {
		if (BuildConfig.DEBUG) {
			Log.e(getPureClassName(object), err);
		}
	}

	public static void e(String tag, String err) {
		if (BuildConfig.DEBUG) {
			Log.e(tag, err);
		}
	}

	public static void e(String msg) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, msg);
		}
	}

	public static void v(String tag, String err) {
		if (BuildConfig.DEBUG) {
			Log.v(tag, err);
		}
	}

	public static void v(String msg) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, msg);
		}
	}

	public static void d(Object object, String debug) {
		if (BuildConfig.DEBUG) {
			Log.d(getPureClassName(object), debug);
		}
	}

	public static void d(String tag, String debug) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, debug);
		}
	}

	public static void d(String tag, String debug, Throwable e) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, debug, e);
		}
	}

	public static void d(String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(Object object, String info) {
		if (BuildConfig.DEBUG) {
			Log.i(getPureClassName(object), info);
		}
	}

	public static void i(String msg) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void w(Object object, String info) {
		if (BuildConfig.DEBUG) {
			Log.w(getPureClassName(object), info);
		}
	}

	public static void w(String msg) {
		if (BuildConfig.DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void d(String message, Object... args) {
		log(Log.DEBUG, null, message, args);
	}

	public static void i(String message, Object... args) {
		log(Log.INFO, null, message, args);
	}

	public static void w(String message, Object... args) {
		log(Log.WARN, null, message, args);
	}

	public static void e(Throwable ex) {
		log(Log.ERROR, ex, null);
	}

	public static void e(String message, Object... args) {
		log(Log.ERROR, null, message, args);
	}

	public static void e(Throwable ex, String message, Object... args) {
		log(Log.ERROR, ex, message, args);
	}

	private static void log(int priority, Throwable ex, String message,
			Object... args) {
		if (BuildConfig.DEBUG)
			return;
		if (args.length > 0) {
			message = String.format(message, args);
		}

		String log;
		if (ex == null) {
			log = message;
		} else {
			String logMessage = message == null ? ex.getMessage() : message;
			String logBody = Log.getStackTraceString(ex);
			log = String.format(LOG_FORMAT, logMessage, logBody);
		}
		Log.println(priority, ImageLoader.TAG, log);
	}

	/**
	 * 
	 * @param object
	 * 
	 * @param tr
	 * 
	 */
	public static void jw(Object object, Throwable tr) {
		if (BuildConfig.DEBUG) {
			Log.w(getPureClassName(object), "", filterThrowable(tr));
		}
	}

	/**
	 * 
	 * @param object
	 * 
	 * @param tr
	 * 
	 */
	public static void je(Object object, Throwable tr) {
		if (BuildConfig.DEBUG) {
			Log.e(getPureClassName(object), "", filterThrowable(tr));
		}
	}

	private static Throwable filterThrowable(Throwable tr) {
		StackTraceElement[] ste = tr.getStackTrace();
		tr.setStackTrace(new StackTraceElement[] { ste[0] });
		return tr;
	}

	private static String getPureClassName(Object object) {
		String name = object.getClass().getName();
		if ("java.lang.String".equals(name)) {
			return object.toString();
		}
		int idx = name.lastIndexOf('.');
		if (idx > 0) {
			return name.substring(idx + 1);
		}
		return name;
	}

}
