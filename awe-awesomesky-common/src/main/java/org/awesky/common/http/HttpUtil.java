package org.awesky.common.http;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


public class HttpUtil {
			
	private static String CHARSET = "UTF-8";
	private String proxyHost;
	private Integer proxyPort;

	public HttpUtil(){ }
	
	public HttpUtil(String proxyHost, Integer proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	//SSL加密请求
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new HttpsX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/json;;charset=UTF-8");
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	//普通请求
	public static String doHttp(String strUrl,String strJson) {
		String strRet = "";
		OutputStream os = null;
	 	try {
			URL url = new URL(strUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(30000);  
			connection.setReadTimeout(30000);     
			connection.connect();       
			os = connection.getOutputStream(); 
			os.write(strJson.getBytes("UTF-8")); 			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
			    strRet += line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {os.close();} catch (IOException e) {} 
			}
		}
        return strRet;
	}
	
	public void doGet(String url) throws Exception{
		URL localURL = new URL(url);
		URLConnection con = openConnection(localURL);
		HttpURLConnection httpCon = (HttpURLConnection)con;
		httpCon.setRequestProperty("Accept-Charset",CHARSET);
		httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		if(httpCon.getResponseCode()>=300){
			throw new RuntimeException("请求失败...");
		}
		InputStreamReader isr = new InputStreamReader(httpCon.getInputStream());
		BufferedReader reader = new BufferedReader(isr);
		String res = reader.readLine();
		System.out.println(res);
		isr.close();
		reader.close();
	}
	
	public void doPost(String url,String...params)throws Exception{
		URL localURL = new URL(url);
		URLConnection con = localURL.openConnection();
		HttpURLConnection httpCon = (HttpURLConnection)con;
		
		httpCon.setDoOutput(true);     //需要输出
	    httpCon.setDoInput(true);      //需要输入
	    httpCon.setUseCaches(false);  
		httpCon.setRequestMethod("POST");
		
		httpCon.setRequestProperty("Accept-Charset", CHARSET);
        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        StringBuffer param = new StringBuffer();
        for(String p : params){
        	param.append(p+"&");
        }
        if((param.toString()).endsWith("&")){
        	param.deleteCharAt(param.lastIndexOf("&"));
        }
        String p = param.toString();
        DataOutputStream dos = new DataOutputStream(httpCon.getOutputStream());
        dos.write(p.getBytes());
        if(httpCon.getResponseCode()>=300){
        	throw new RuntimeException("访问失败...");
        }
        InputStreamReader isr = new InputStreamReader(httpCon.getInputStream());
		BufferedReader reader = new BufferedReader(isr);
		String res = reader.readLine();
		System.out.println(res);
		isr.close();
		reader.close();
	}
	
	private URLConnection openConnection(URL localURL) throws Exception {
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }
	
	public static void main(String[] args) throws Exception {
		
		String smsTxt = "{\"strText\":\"999999Ψ1008Ψ15Ψ811748Ψ13687018680Ψ3\"}";
		//smsTxt = "{\"strText\":\"13687018680Ψhello\"}";
		String url = "http://115.29.14.115:9524/JsonService/SendSmsMessage";
		String rs = doHttp(url,smsTxt);
		System.out.println(rs);
	}
	
}
