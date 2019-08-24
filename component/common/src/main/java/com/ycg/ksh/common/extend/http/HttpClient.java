package com.ycg.ksh.common.extend.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.util.FileUtils;

public class HttpClient {


	private static final ContentType CONTENTTYPE_TEXT = ContentType.create("text/plain", Consts.UTF_8);
	private static final ContentType CONTENTTYPE_XML = ContentType.create("text/xml", Consts.UTF_8);
	private static final ContentType CONTENTTYPE_MULTIPART = ContentType.create("multipart/form-data", Consts.UTF_8);

	public enum Type{

		POST(true, false), GET(false, false), JSON(true, true);

		private boolean post;
		private boolean json;

		Type(boolean post, boolean json) {
			this.post = post;
			this.json = json;
		}

		public void setPost(boolean post) {
			this.post = post;
		}

		public void setJson(boolean json) {
			this.json = json;
		}
	}

	private static final String HTTP = "http";
	private static final String HTTPS = "https";

	public final static int HTTP_OK = 200;
	public final static int HTTP_TIMEOUT = 408;

	private Type type;
	private int timeout;
	private String urlString;
	private String parameterString;
	private String contentEncoding = "UTF-8";

	private Map<String, Object> parameters;
	private Map<String, String> properties;
	
	private String responseContent;
	private Map<String, String> responseHeaders;


	private boolean usePool = false;
	private static PoolingHttpClientConnectionManager connectionManager;

	static{
		try{
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
			registryBuilder.register(HTTP, PlainConnectionSocketFactory.INSTANCE);
			X509TrustManager trustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{trustManager}, null);
			registryBuilder.register(HTTPS, new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE)).build();
			//创建ConnectionManager，添加Connection配置信息
			connectionManager = new PoolingHttpClientConnectionManager(registryBuilder.build());
		}catch (Exception e){
			e.printStackTrace();
		}
	}



	public HttpClient() {}

	public HttpClient(int timeout) {
		this();
		this.timeout = timeout;
	}
	public HttpClient(String urlString, int timeout) {
		this();
		this.timeout = timeout;
	}

	public HttpClient(String urlString) {
		this(urlString, Type.JSON);
	}

	public HttpClient(String urlString, Type type) {
		super();
		this.type = type;
		this.urlString = urlString;
	}

	public HttpClient(String urlString, Type type, boolean usePool) {
		super();
		this.type = type;
		this.urlString = urlString;
		this.usePool = usePool;
	}

	public static HttpClient createHttpClient(String urlString, Type type) {
		return new HttpClient(urlString, type, true);
	}


	public void reset(){
		if(properties != null){
			properties.clear();
		}
		if(parameters != null){
			parameters.clear();
		}
	}
	
	public void property(String pkey, Serializable value){
		if(properties == null){
			properties = new HashMap<String, String>();
		}
		properties.put(pkey, String.valueOf(value));
	}

	public void property(Map<String, String> _properties){
		if(_properties != null && !_properties.isEmpty()){
			if(properties == null){
				properties = new HashMap<String, String>();
			}
			for (Map.Entry<String, String> entry : _properties.entrySet()) {
				properties.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public void parameter(Map<String, Object> _parameters){
		if(_parameters != null && !_parameters.isEmpty()){
			if(parameters == null){
				parameters = new HashMap<String, Object>();
			}
			for (Map.Entry<String, Object> entry : _parameters.entrySet()) {
				parameters.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public void parameter(String pkey, Object value){
		if(parameters == null){
			parameters = new HashMap<String, Object>();
		}
		parameters.put(pkey, value);
	}

	private void setHeader(AbstractHttpMessage httpMessage){
		if (properties != null) {
			for(Map.Entry<String, String> entry: properties.entrySet()){
				httpMessage.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	private String get(CloseableHttpClient httpclient) throws ClientProtocolException, IOException{
		String lastUrl = urlString;
		StringBuilder builder = new StringBuilder();
		if(StringUtils.isNotEmpty(parameterString)){
			builder.append(parameterString);
		}
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				if(builder.length() > 0){
					builder.append("&");
				}
				builder.append(entry.getKey()).append("=").append(entry.getValue());
			}	
		}
		if (lastUrl.indexOf("?") > 0) {
			builder.insert(0, "&");
			builder.insert(0, lastUrl);
		} else {
			builder.insert(0, '?');
			builder.insert(0, lastUrl);
		}
		HttpGet httpget = new HttpGet(builder.toString());
		setHeader(httpget);
		return execute(httpclient, httpget);
	}

	private String execute(CloseableHttpClient httpclient, HttpUriRequest request) throws IOException {
		CloseableHttpResponse response = httpclient.execute(request);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, contentEncoding);
			}
			Header[] headers = response.getAllHeaders();
			responseHeaders = new HashMap<String, String>(headers.length);
			for (Header header : headers) {
				responseHeaders.put(header.getName(), header.getValue());
			}
		} finally {
			//response.close();
		}
		return responseContent;
	}

	private String json(CloseableHttpClient httpclient) throws ClientProtocolException, IOException{
		HttpPost httppost = new HttpPost(urlString);
		property("Content-Type", "application/json;charset=UTF-8");
		setHeader(httppost);
		if(parameters != null && !parameters.isEmpty()) {
			parameterString = JSONObject.toJSONString(parameters);
		}
		httppost.setEntity(new StringEntity(parameterString, ContentType.APPLICATION_JSON));
		return execute(httpclient, httppost);
	}
	
	private String post(CloseableHttpClient httpclient) throws ClientProtocolException, IOException{
		HttpPost httppost = new HttpPost(urlString);
		setHeader(httppost);
		if(parameters != null){
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object>  entry : parameters.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
			}
			httppost.setEntity(new UrlEncodedFormEntity(formparams, contentEncoding));
		}
		if(StringUtils.isNotBlank(parameterString)){
			Header[] headerCtyp = httppost.getHeaders("ContentType");
			Header[] headerCharSet = httppost.getHeaders("charset");
			ContentType contentType = null;
			try {
				if(headerCtyp!=null && headerCharSet!=null) {
					contentType = ContentType.create(headerCtyp[0].getValue(),headerCharSet[0].getValue());
				}
			} catch (Exception e) {
				contentType = CONTENTTYPE_XML;
			}
			httppost.setEntity(new StringEntity(parameterString, contentType==null?CONTENTTYPE_XML:contentType));
		}
		return execute(httpclient, httppost);
	}

	public String upload(String localFilePath, String serverFieldName) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(urlString);
			setHeader(httppost);
			FileBody binFileBody = new FileBody(new File(localFilePath), CONTENTTYPE_MULTIPART);
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.setContentType(CONTENTTYPE_MULTIPART);
			// 添加文件参数
			multipartEntityBuilder.addPart(serverFieldName, binFileBody);
			// 设置上传的其他参数
			if(parameters != null){
				for (Map.Entry<String, Object>  entry : parameters.entrySet()) {
					if(entry.getValue() == null){ continue; }
					multipartEntityBuilder.addPart(entry.getKey(), new StringBody(String.valueOf(entry.getValue()), CONTENTTYPE_TEXT));
				}
			}
			httppost.setEntity(multipartEntityBuilder.build());
			return execute(httpclient, httppost);
		} finally {
			httpclient.close();
		}
	}

	public File down(String localFilePath) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		File file = null;
		try {
			HttpGet httpGet = new HttpGet(urlString);
			setHeader(httpGet);
			HttpResponse respone = httpclient.execute(httpGet);
			HttpEntity entity = respone.getEntity();
			if(entity != null) {
				InputStream is = null;
				FileOutputStream fos = null;
				try{
					is = entity.getContent();
					file = FileUtils.file(localFilePath);
					fos = new FileOutputStream(file);
					byte[] buffer = new byte[4096];
					int len = -1;
					while((len = is.read(buffer) ) > -1){
						fos.write(buffer, 0, len);
					}
				}finally {
					if(fos != null){ fos.close(); }
					if(is != null){ is.close(); }
				}
			}
		} finally {
			httpclient.close();
		}
		return file;
	}

	public String get() throws IOException{
		return request(Type.GET);
	}
	
	public String post() throws IOException{
		return request(Type.POST);
	}
	
	public String json() throws IOException{
		return request(Type.JSON);
	}
	public String request() throws IOException{
		CloseableHttpClient httpclient = null;
		try {
			HttpClientBuilder clientBuilder = HttpClients.custom();
			if(usePool){
				clientBuilder.setConnectionManager(connectionManager);
			}
			httpclient = clientBuilder.build();
			if(type.post){
				responseContent = type.json ? json(httpclient) : post(httpclient);
			}else{
				responseContent = get(httpclient);
			}
		}finally {
			if (httpclient != null && !usePool) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseContent;
	}

	public String request(Type _type) throws IOException{
		this.type = _type;
		return request();
	}

	public String responseHeader(String name){
		if(responseHeaders != null){
			return responseHeaders.get(name);
		}
		return null;
	}
	
	public Map<String, Object> parameters() {
		return parameters;
	}

	public String getParameterString() {
		return parameterString;
	}

	public void setParameterString(String parameterString) {
		this.parameterString = parameterString;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Url[").append(urlString).append("]");
		if(parameterString != null && !"".equals(parameterString)){
			builder.append(" Params:").append(parameterString);
		}else{
			builder.append(" Params:").append(parameters);
		}
		builder.append(" Receive:").append(responseContent);
		return builder.toString();
	}
}
