package com.ntredize.redstop.support.utils;

import android.content.Context;
import android.util.Log;

import com.ntredize.redstop.common.exception.ApplicationException;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import okhttp3.HttpUrl;

public class HttpClient {
	
	public final String TAG = this.getClass().getName();
	
	private final int GET_TYPE_API = 1;
	private final int GET_TYPE_IMAGE = 2;
	
	private final int TIMEOUT_GET = 60000;
	private final String API_SERVER_URL = "https://api.redstop.info";
	private final String API_CONTEXT_ROOT = "/redstop/jaxrs";
	private final String IMAGE_SERVER_URL = "https://image.redstop.info";
	private final String IMAGE_CONTEXT_ROOT = "/redstop/images";
	
	public final Context context;
	
	
	/* Init */
	public HttpClient(Context context) {
		this.context = context;
	}
	
	
	/* Http URL*/
	private HttpUrl buildApiHttpUrl(String apiUrl, Map<String, String> queryItems) {
		String url = API_SERVER_URL + API_CONTEXT_ROOT + apiUrl;
		HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
		
		if (queryItems != null) {
			for (Map.Entry<String, String> entry : queryItems.entrySet()) {
				builder.addQueryParameter(entry.getKey(), entry.getValue());
			}
		}
		
		return builder.build();
	}
	
	private HttpUrl buildImageHttpUrl(String imageUrl) {
		String url = IMAGE_SERVER_URL + IMAGE_CONTEXT_ROOT + imageUrl;
		return Objects.requireNonNull(HttpUrl.parse(url)).newBuilder().build();
	}
	
	
	/* GET */
	public byte[] doGet(String apiUrl) throws ApplicationException {
		return doGet(true, GET_TYPE_API, apiUrl, null);
	}
	
	public byte[] doGet(String apiUrl, Map<String, String> queryItems) throws ApplicationException {
		return doGet(true, GET_TYPE_API, apiUrl, queryItems);
	}
	
	public byte[] doGetImage(String url) {
		try {
			return doGet(false, GET_TYPE_IMAGE, url, null);
		} catch (Exception ignored) {
			return null;
		}
	}
	
	private byte[] doGet(boolean showLog, int getType, String apiUrl, Map<String, String> queryItems) throws ApplicationException {
		if (showLog) Log.i(TAG, "GET: " + apiUrl + (queryItems != null ? " " + queryItems : ""));
		
		ByteArrayOutputStream buffer = null;
		
		try {
			// build connection
			HttpURLConnection connection;
			if (getType == GET_TYPE_API) connection = (HttpURLConnection) buildApiHttpUrl(apiUrl, queryItems).url().openConnection();
			else if (getType == GET_TYPE_IMAGE) connection = (HttpURLConnection) buildImageHttpUrl(apiUrl).url().openConnection();
			else throw new Exception("Invalid GET Type");
			
			connection.setRequestMethod("GET");
			connection.setReadTimeout(TIMEOUT_GET);
			connection.setConnectTimeout(TIMEOUT_GET);
			
			// connect
			connection.connect();
			int statusCode = connection.getResponseCode();
			
			// output
			buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = connection.getInputStream().read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			byte[] b = buffer.toByteArray();
			
			// not ok status code
			if (statusCode <= 199 || statusCode >= 300) {
				if (showLog) Log.e(TAG, "GET Error: " + new String(b));
				throw new ApplicationException(context, ApplicationException.HTTP_CLIENT_ERROR);
			}
			
			// ok status code
			else return b;
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			// unknown error
			if (showLog) Log.e(TAG, Objects.requireNonNull(e.getMessage()));
			throw new ApplicationException(context, ApplicationException.HTTP_CLIENT_ERROR);
		} finally {
			if (buffer != null) {
				try {
					buffer.flush();
					buffer.close();
				} catch (Exception ignored) {}
			}
		}
		
	}
	
	
	/* Post */
	public byte[] doPost(String apiUrl) throws ApplicationException {
		return doPost(true, apiUrl, null);
	}
	
	public byte[] doPost(String apiUrl, String jsonRequest) throws ApplicationException {
		return doPost(true, apiUrl, jsonRequest);
	}
	
	private byte[] doPost(boolean showLog, String apiUrl, String jsonRequest) throws ApplicationException {
		if (showLog) Log.i(TAG, "POST: " + apiUrl);
		
		OutputStream os = null;
		ByteArrayOutputStream buffer = null;
		
		try {
			// build connection
			HttpURLConnection connection = (HttpURLConnection) buildApiHttpUrl(apiUrl, null).url().openConnection();
			connection.setRequestMethod("POST");
			connection.setReadTimeout(TIMEOUT_GET);
			connection.setConnectTimeout(TIMEOUT_GET);
			
			// json request
			if (jsonRequest != null) {
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json; utf-8");
				
				byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
				os = connection.getOutputStream();
				os.write(input, 0, input.length);
			}
			
			// connect
			connection.connect();
			int statusCode = connection.getResponseCode();
			
			// output
			buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = connection.getInputStream().read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			byte[] b = buffer.toByteArray();
			
			// not ok status code
			if (statusCode <= 199 || statusCode >= 300) {
				if (showLog) Log.e(TAG, "GET Error: " + new String(b));
				throw new ApplicationException(context, ApplicationException.HTTP_CLIENT_ERROR);
			}
			
			// ok status code
			else return b;
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			// unknown error
			if (showLog) Log.e(TAG, Objects.requireNonNull(e.getMessage()));
			throw new ApplicationException(context, ApplicationException.HTTP_CLIENT_ERROR);
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (Exception ignored) {}
			}
			
			if (buffer != null) {
				try {
					buffer.flush();
					buffer.close();
				} catch (Exception ignored) {}
			}
		}
		
	}
	
}
