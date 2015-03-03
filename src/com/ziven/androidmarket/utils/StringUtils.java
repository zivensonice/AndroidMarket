package com.ziven.androidmarket.utils;

import java.text.DecimalFormat;

public class StringUtils {

	/* 判断字符串是否为空 */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/* 判断字符串是否相等 ,空字符不相等 */
	public static boolean isEquals(String... args) {
		String last = null;
		for (int i = 0; i < args.length; i++) {
			String str = args[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/* 格式化文件大小,不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/* 格式化文件大小,保留末尾的0 达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / (float) 100)) + "KB";
		} else if (len < 100 * 1024) {
			size = String.valueOf(formatKeepOneZero.format(len * 100 / 1024 / 100f)) + "KB";
		} else if (len < 1024 * 1024) {
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 10 / 1024 / 1024 / 10f)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}
}
