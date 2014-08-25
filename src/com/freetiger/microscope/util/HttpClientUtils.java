package com.freetiger.microscope.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;

public class HttpClientUtils {
	private static final Log log = LogFactory.getLog(HttpClientUtils.class);
	private static PoolingHttpClientConnectionManager cm = null;
	static {
		cm = new PoolingHttpClientConnectionManager();
		try {
			int maxTotal = 100;
			cm.setMaxTotal(maxTotal);
		} catch (NumberFormatException e) {
			log.error("Key[httpclient.max_total] Not Found in systemConfig.properties", e);
		}
		// 每条通道的并发连接数设置（连接池）
		try {
			int defaultMaxConnection = 50;
			cm.setDefaultMaxPerRoute(defaultMaxConnection);
		} catch (NumberFormatException e) {
			log.error("Key[httpclient.default_max_connection] Not Found in systemConfig.properties", e);
		}

		// 指定专门的route，设置最大连接数为80
		try {
			HttpHost localhost = new HttpHost("locahost", 80);
			cm.setMaxPerRoute(new HttpRoute(localhost), 50);
		} catch (NumberFormatException e) {
			log.error("Key[httpclient.max_connection] Not Found in systemConfig.properties", e);
		}
	}

	public static HttpClient getHttpClient() {
		// 创建httpClient
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultHeaders(defaultHeader()).build();
//
//		HttpParams params = new BasicHttpParams();
//		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // 3000ms
		return httpClient;
	}

	private static List<Header> defaultHeader() {
		ArrayList<Header> headers = new ArrayList<Header>();
		Header header = new BasicHeader(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1.toString());	//TODO
		headers.add(header);
//		if (!StringUtils.isEmpty(referer)) {
//			headers.add(new BasicHeader(HttpHeaders.REFERER, referer));
//		}
//		if (!StringUtils.isEmpty(cookie)) {
//			headers.add(new BasicHeader("Cookie", cookie));
//		}
		return headers;
	}

	public static void release() {
		if (cm != null) {
			cm.shutdown();
		}
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			long l1 = System.currentTimeMillis();
			HttpClient client = getHttpClient();

			HttpGet get = new HttpGet("http://www.baidu.com/s?wd=" + r.nextInt(5000));
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				long l = entity.getContentLength();
				System.out.println("回应结果长度:" + l);
			}
			System.out.println("查询耗时" + (System.currentTimeMillis() - l1));
		}
	}

}
