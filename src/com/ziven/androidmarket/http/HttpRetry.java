package com.ziven.androidmarket.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Currency;
import java.util.HashSet;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.os.SystemClock;

public class HttpRetry implements HttpRequestRetryHandler {
	// 重试休息时间
	private static final int RETRY_SLEEP_TIME_MILLIS = 1000;
	// 异常白名单,出现继续请求
	private static HashSet<Class<?>> exceptionWhiteList = new HashSet<Class<?>>();
	// 异常黑名单,出现不继续请求
	private static HashSet<Class<?>> exceptionBlackList = new HashSet<Class<?>>();

	static {
		// 连接上了服务器,但是没响应异常
		exceptionBlackList.add(NoHttpResponseException.class);
		// host未响应异常,一般是网络原因造成
		exceptionBlackList.add(UnknownHostException.class);
		// Socket问题,一般是网络异常
		exceptionBlackList.add(SocketException.class);

		// 连接中断异常,一般是超时引起
		exceptionWhiteList.add(InterruptedIOException.class);
		// SSL握手失败
		exceptionWhiteList.add(SSLHandshakeException.class);
	}
	// 最大重试次数
	private final int maxRetries;

	private HttpRetry(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	/* 重试请求 */
	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		boolean retry = true;
		// 请求是否到达
		Boolean b = (Boolean) context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
		boolean sent = (null != b && b.booleanValue());
		if (executionCount > maxRetries) {
			retry = false;
		} else if (exceptionBlackList.contains(exception.getClass())) {
			retry = false;
		} else if (exceptionWhiteList.contains(exception.getClass())) {
			retry = true;
		} else if (!sent) {
			retry = true;
		}
		if (retry) {
			// 获取request
			HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			retry = currentReq != null;
		}
		if (retry) {
			SystemClock.sleep(RETRY_SLEEP_TIME_MILLIS);
		} else {
			exception.printStackTrace();
		}
		return retry;
	}
}
