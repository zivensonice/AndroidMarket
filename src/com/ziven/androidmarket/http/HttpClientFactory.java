package com.ziven.androidmarket.http;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {
	/* http最大并发连接数 */
	private static final int MAX_CONNECTIONS = 10;
	/* 超时时间 */
	private static final int TIME_OUT = 10 * 1000;
	/* 缓存大小 */
	private static final int SOCKET_BUFFER_SIZE = 8 * 1024;
	/* 出错误次数,错误异常表在RetryHandler静态代码块中添加 */
	private static final int MAX_RETRIES = 5;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

	public static DefaultHttpClient create(boolean isHttps) {
		HttpParams params = createHttpParams();
		DefaultHttpClient httpClient = null;
		if (isHttps) {
			// 支持https和http
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory(),
					443));
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
			httpClient = new DefaultHttpClient(cm, params);
		} else {
			httpClient = new DefaultHttpClient(params);
		}
		return httpClient;
	}

	private static HttpParams createHttpParams() {
		// TODO Auto-generated method stub
		return null;
	}
}
