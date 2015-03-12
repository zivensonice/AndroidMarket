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
import org.apache.http.client.methods.HttpRequestBase;

import com.ziven.androidmarket.utils.IOUtils;
import com.ziven.androidmarket.utils.L;
import com.ziven.androidmarket.utils.StringUtils;

public class HttpHelper {
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
			if (null != mInputStream && getCode() < 300) {
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
