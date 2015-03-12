package com.ziven.androidmarket.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
	/* 关闭流 */
	public static boolean close(Closeable io) {
		if (null != io) {
			try {
				io.close();
			} catch (IOException e) {
				L.e(e);
				return false;
			}
		}
		return false;
	}
}
