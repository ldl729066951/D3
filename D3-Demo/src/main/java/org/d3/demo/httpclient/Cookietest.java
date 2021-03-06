package org.d3.demo.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

public class Cookietest {

	public static void main(String[] args) throws IOException {
		String url = "http://weixin.sogou.com/weixin?"
        		+ "type=2&"
        		+ "query=%E5%85%B0%E5%B7%9E&"
        		+ "ie=utf8&"
        		+ "_ast=1420441472&"
        		+ "_asf=null&w=01019900&"
        		+ "p=40040100&dp=1&"
        		+ "cid=null&"
        		+ "sut=5372&"
        		+ "sst0=1420441488584&"
//        		+ "page=1&"
        		+ "lkt=0%2C0%2C0";
		url = "http://weixin.sogou.com/weixin?type=2&query=%E5%85%B0%E5%B7%9E&fr=sgsearch&ie=utf8&_ast=1421141632&_asf=null&w=01029901&p=40040100&dp=1&cid=null";

		doGet(url);
	}
	
	public static String doGet(String url)
			throws IOException {
		String content = "";
		BasicCookieStore cookieStore = new BasicCookieStore();
		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {

				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {
						// Oh, I am easy
					}
				};
			}

		};
		Registry<CookieSpecProvider> r = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY,
						new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();

		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec("easy").setSocketTimeout(10000)
				.setConnectTimeout(10000).build();

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieSpecRegistry(r)
				.setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore)

				.build();
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		if (hashMap != null) {
//			Iterator<String> it = hashMap.keySet().iterator();
//			while (it.hasNext()) {
//
//				String key = it.next();
//				String value = hashMap.get(key);
//				nvps.add(new BasicNameValuePair(key, value));
//			}
//		}

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");
		httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Host", "weixin.sogou.com");
		httpGet.setHeader("Referer", "http://weixin.sogou.com/weixin?type=2&query=%E5%85%B0%E5%B7%9E&fr=sgsearch&ie=utf8&_ast=1421146752&_asf=null&w=01029901&p=40040100&dp=1&cid=null");
		httpGet.setHeader("Cookie", "IPLOC=CN3100; SUID=590AF63A4FC80D0A0000000054780EE8; SUV=1417154281273474; sct=37; CXID=DA7F3CE6D3F57B0E716099AC108B171F; ABTEST=0|1421146807|v17; SNUID=1C1816B08E88820D3515D7368E4F6B52; ld=R2WDnyllll2Ur2r0lllllVv18ZYlllllnsY3QyllllwlllllRvoll5@@@@@@@@@@; ad=sZllllllll2Uri@jlllllVvsoV6lllll5BAQ6kllllwllllljylll5@@@@@@@@@@; LSTMV=605%2C75; LCLKINT=6371");
//		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

		httpGet.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
//	"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:28.0) Gecko/20100101 Firefox/28.0");
		// 如果参数是中文，需要进行转码
//		httpGet.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			for (Header s : response.getAllHeaders()) {
				System.out.println("post header====" + s);
			}
			InputStream is = entity.getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(is,
					Consts.UTF_8));
			String line = "";
			while ((line = in.readLine()) != null) {

				content += (line + "\n");
			}
			System.out.println(content);
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				// 读取Cookie
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("post request - "
							+ cookies.get(i).toString());
				}
			}

		} finally {
			if (response != null)
				response.close();
		}
		return content;
	}

	public static String doPost(String url, HashMap<String, String> hashMap)
			throws IOException {
		String content = "";
		BasicCookieStore cookieStore = new BasicCookieStore();
		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {

				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {
						// Oh, I am easy
					}
				};
			}

		};
		Registry<CookieSpecProvider> r = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY,
						new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();

		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec("easy").setSocketTimeout(10000)
				.setConnectTimeout(10000).build();

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieSpecRegistry(r)
				.setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore)

				.build();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (hashMap != null) {
			Iterator<String> it = hashMap.keySet().iterator();
			while (it.hasNext()) {

				String key = it.next();
				String value = hashMap.get(key);
				nvps.add(new BasicNameValuePair(key, value));
			}
		}

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate");
		httpPost.setHeader("Accept-Language", "en-US,en;q=0.5");
		httpPost.setHeader("Cache-Control", "max-age=0");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:28.0) Gecko/20100101 Firefox/28.0");
		// 如果参数是中文，需要进行转码
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			for (Header s : response.getAllHeaders()) {
				System.out.println("post header====" + s);
			}
			InputStream is = entity.getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(is,
					Consts.UTF_8));
			String line = "";
			while ((line = in.readLine()) != null) {

				content += line;
			}

			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				// 读取Cookie
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("post request - "
							+ cookies.get(i).toString());
				}
			}

		} finally {
			if (response != null)
				response.close();
		}
		return content;
	}
}