package com.lody.virtual.helper.utils;

import android.os.Bundle;
import android.util.Log;

import java.util.Set;

/**
 * @author Lody
 *
 */
public class VLog {

	public static boolean OPEN_LOG = true;

	private static String formatSafely(String msg, Object... format) {
		if (msg == null) return "null";
		if (format == null || format.length == 0) {
			return msg;
		}
		try {
			return String.format(msg, format);
		} catch (Throwable t) {
			return msg;
		}
	}

	public static void i(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			Log.i(tag, formatSafely(msg, format));
		}
	}

	public static void d(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			Log.d(tag, formatSafely(msg, format));
		}
	}

	public static void w(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			Log.w(tag, formatSafely(msg, format));
		}
	}

	public static void e(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			Log.e(tag, formatSafely(msg, format));
		}
	}

	public static void v(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			Log.v(tag, formatSafely(msg, format));
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (OPEN_LOG) {
			Log.e(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (OPEN_LOG) {
			Log.w(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (OPEN_LOG) {
			Log.i(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (OPEN_LOG) {
			Log.d(tag, msg, tr);
		}
	}

	@SuppressWarnings("deprecation")
	public static String toString(Bundle bundle){
		if(bundle==null)return null;
		if(Reflect.on(bundle).get("mParcelledData")!=null){
			Set<String> keys=bundle.keySet();
			StringBuilder stringBuilder=new StringBuilder("Bundle[");
			if(keys!=null) {
				for (String key : keys) {
					stringBuilder.append(key);
					stringBuilder.append("=");
					stringBuilder.append(bundle.get(key));
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
			return stringBuilder.toString();
		}
		return bundle.toString();
	}

	public static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}

	public static void printStackTrace(String tag) {
		Log.e(tag, getStackTraceString(new Exception()));
	}

	public static void e(String tag, Throwable e) {
		Log.e(tag, getStackTraceString(e));
	}
}
