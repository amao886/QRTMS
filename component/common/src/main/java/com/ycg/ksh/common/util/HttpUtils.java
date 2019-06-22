package com.ycg.ksh.common.util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public abstract class HttpUtils {
	public final static int HTTP_OK = 200;
	public final static int HTTP_TIMEOUT = 408;
	public final static String METHOD_POST = "POST";
	public final static String METHOD_GET = "GET";
	private final static String DEFAULT_ENCODING = "UTF-8";
	private static SSLSocketFactory ssf;


	private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
		@Override
		public boolean verify(String s, SSLSession sslsession) {
			return true;
		}
	};

	private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {
		private X509Certificate[] certificates;
		@Override
		public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = certificates;
			}

		}
		@Override
		public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = ax509certificate;
			}
		}
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	static {
		HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
		try {
			TrustManager[] tm = { ignoreCertificationTrustManger };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			ssf = sslContext.getSocketFactory();
		} catch (Exception e) { e.printStackTrace(); }
	}

	private static String respone(URLConnection connection) throws Exception{
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if(builder.length() > 0){
					builder.append('\n');
				}
				builder.append(line);
			}
			bufferedReader.close();
			String encoding = connection.getContentEncoding();
			if (encoding == null) {
				encoding = DEFAULT_ENCODING;
			}
			return builder.toString();
		}finally {
			try {
				if(bufferedReader != null){
					bufferedReader.close();
				}
			} catch (IOException e) {
				throw e;
			} catch (Exception e2) { }
		}
	}
	
	public static String get(String urlString) throws Exception{
		return get(urlString, null);
	}
	
	public static String post(String urlString, String bodyString) throws Exception{
		return post(urlString, bodyString, null);
	}
	
	public static String get(String urlString, Map<String, String> propertys) throws Exception{
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			if (urlString.startsWith("https://")) {
				((HttpsURLConnection) urlConnection).setSSLSocketFactory(ssf);
			}
			urlConnection.setRequestMethod(METHOD_GET);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			if(propertys != null && !propertys.isEmpty()){
				for (Map.Entry<String, String> entry : propertys.entrySet()) {
					urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			return respone(urlConnection);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (null != urlConnection) {
					urlConnection.disconnect();
				}
			} catch (Exception e2) { }
		}
	}
	
	public static String post(String urlString, String bodyString, Map<String, String> propertys) throws Exception{
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			if (urlString.startsWith("https://")) {
				((HttpsURLConnection) urlConnection).setSSLSocketFactory(ssf);
			}
			urlConnection.setRequestMethod(METHOD_POST);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			if(propertys != null && !propertys.isEmpty()){
				for (Map.Entry<String, String> entry : propertys.entrySet()) {
					urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if(bodyString != null && bodyString.length() > 0){
				OutputStream out = null;
				try {
					out = urlConnection.getOutputStream();
					out.write(bodyString.getBytes(DEFAULT_ENCODING));
				} catch (IOException e) {
					throw e;
				}finally {
					try {
						if (null != out) {
							out.flush();
							out.close();
						}
					} catch (Exception e2) { }
				}
			}
			return respone(urlConnection);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (null != urlConnection) {
					urlConnection.disconnect();
				}
			} catch (Exception e2) { }
		}
	}
	
}
