package com.ziven.androidmarket.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import com.ziven.androidmarket.utils.IOUtils;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;

public class HttpHelper {

	/* get请求 */
	public static HttpResult get(String url) {
		HttpGet httpGet = new HttpGet(url);
		return execute(url, httpGet);
	}

	/* post请求 */
	public static HttpResult post(String url, byte[] bytes) {
		HttpPost httpPost = new HttpPost(url);
		ByteArrayEntity entity = new ByteArrayEntity(bytes);
		httpPost.setEntity(entity);
		return execute(url, httpPost);
	}

	/* 下载 */
	public static HttpResult download(String url) {
		HttpGet get = new HttpGet();
		return execute(url, get);
	}

	private static HttpResult execute(String url, HttpRequestBase request) {
		boolean isHttps = url.startsWith("https://");
		AbstractHttpClient httpClient = HttpClientFactory.create(isHttps);
		HttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
		HttpRequestRetryHandler retryHandler = httpClient.getHttpRequestRetryHandler();
		int retryCount = 0;
		boolean retry = true;
		while (retry) {
			try {
				HttpResponse response = httpClient.execute(request, httpContext);
				if (response != null) {
					return new HttpResult(httpClient, request, response);
				}
			} catch (Exception e) {
				IOException ioException = new IOException(e.getMessage());
				retry = retryHandler.retryRequest(ioException, ++retryCount, httpContext);// 把错误异常交给重试机制，以判断是否需要采取从事
				L.e(e);
			}
		}
		return null;
	}

	public static class HttpResult {
		private HttpClient mClient;
		private HttpRequestBase mRequest;
		private HttpResponse mResponse;
		private InputStream mInputStream;
		private String mStr;

		public HttpResult(HttpClient mClient, HttpRequestBase mRequest, HttpResponse mResponse) {
			super();
			this.mClient = mClient;
			this.mRequest = mRequest;
			this.mResponse = mResponse;
		}

		/* 返回响应状态码 */
		public int getCode() {
			StatusLine status = mResponse.getStatusLine();
			return status.getStatusCode();
		}

		/* 从结果中获取字符串,一旦获取成功,会自动关闭连接和流资源,并且把字符串保存起来,方便下次获取 */
		public String getString() {
			if (!StringUtils.isEmpty(mStr)) {
				return mStr;
			}
			InputStream ips = getInputStream();
			ByteArrayOutputStream ops = new ByteArrayOutputStream();
			try {
				byte[] buff = new byte[1024 * 4];
				for (int len = -1; (len = ips.read(buff)) != -1;) {
					ops.write(buff, 0, len);
				}
				byte[] data = ops.toByteArray();
				mStr = new String(data, "utf-8");
			} catch (IOException e) {
				L.e(e);
			} finally {
				// 网络连接和流操作包括数据库在使用完成后一定记得要关闭
				IOUtils.close(ips);
				IOUtils.close(ops);
				close();
			}
			return mStr;
		}

		/* 当返回字节码小于300,返回字节流 */
		public InputStream getInputStream() {
			L.d("getCode" + getCode());
			if (null == mInputStream && getCode() < 300) {
				HttpEntity entity = mResponse.getEntity();
				try {
					mInputStream = entity.getContent();
				} catch (Exception e) {
					L.e(e);
				}
			}
			return mInputStream;
		}

		/* 关闭资源 */
		public void close() {
			if (null != mRequest) {
				mRequest.abort();
			}
			IOUtils.close(mInputStream);
			if (null != mClient) {
				mClient.getConnectionManager().closeExpiredConnections();
			}
		}
	}

}
