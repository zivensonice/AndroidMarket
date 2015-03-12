package com.ziven.androidmarket.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

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
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https",
					org.apache.http.conn.ssl.SSLSocketFactory
							.getSocketFactory(), 443));
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			httpClient = new DefaultHttpClient(cm, params);
		} else {
			httpClient = new DefaultHttpClient(params);
		}
		return httpClient;
	}

	private static HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();
		// 是否启动旧连接检查,默认开启,关闭这个检查可以提高20~30ms的性能,但是增加了I/O出错的风险
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		// 连接超时时间
		HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
		// Socket超时时间
		HttpConnectionParams.setSoTimeout(params, TIME_OUT);
		// Socket缓存
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
		// 延迟发送
		HttpConnectionParams.setTcpNoDelay(params, true);
		// 协议版本
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// 异常处理机制
		HttpProtocolParams.setUseExpectContinue(params, true);
		// 设置编码方式
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		// 是否重定向
		HttpClientParams.setRedirecting(params, false);
		// 设置超时
		ConnManagerParams.setTimeout(params, TIME_OUT);
		// 设置多线程最大连接数
		ConnManagerParams.setMaxConnectionsPerRoute(params,
				new ConnPerRouteBean(MAX_CONNECTIONS));
		// 多线程总连接数
		ConnManagerParams.setMaxTotalConnections(params, 10);
		return params;
	}

	/* 当服务器返回数据是GZIP压缩的数据,填充Response返回实体数据,则返回Gzip解压流 */
	private static class InflatingEntity extends HttpEntityWrapper {

		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength() {
			return -1;
		}

	}
}
