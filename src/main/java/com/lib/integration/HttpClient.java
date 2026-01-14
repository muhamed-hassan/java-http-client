package com.lib.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Designed to be extended (inherited) to hide the low level complexity a bit and generalize the HTTP verbs usage.
 * 
 * Abstract Example: 
 * - ChildClientClass extends HttpClient => Where `ChildClient` is replaced with a proper name where it follows SRP and KISS 
 * 
 * */
public class HttpClient {

	private String providerBaseUrl;
	
	public HttpClient(String providerBaseUrl) {
		this.providerBaseUrl = providerBaseUrl;
	}
	
	protected Object get(String requestPath, Class<? extends Object> responseBodyType) {
		
		HttpURLConnection connection = null;
		Object responseBody = null;		
		try {
			
			String requestUrl = providerBaseUrl + requestPath;
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");			

			ObjectMapper objectMapper = new ObjectMapper();
			
			int responseCode = connection.getResponseCode();
			switch (responseCode) {
				case HttpURLConnection.HTTP_OK:					
					responseBody = objectMapper.readValue((InputStream) connection.getContent(), responseBodyType);
					break;
				case HttpURLConnection.HTTP_NOT_FOUND:
					String errorBody = objectMapper.readValue((InputStream) connection.getErrorStream(), String.class);
					throw new IOException("Failed to connect with " + requestUrl + " due to " + errorBody);	
				default:
					throw new IOException("Failed to connect with " + requestUrl + " and status code is " + responseCode);
			}			
			
		} catch (Exception e) {		
			throw new RuntimeException(e);			
		} finally {
			connection.disconnect();
	    }
		
		return responseBody;
	}
		
	/* ******************************************************************************************************** */	
	
	protected <T> void post(String requestPath, T payload) {
		
		HttpURLConnection connection = null;
		try {
			
			String requestUrl = providerBaseUrl + requestPath;
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);			
			
			ObjectMapper objectMapper = new ObjectMapper();			
			OutputStream outputStream = connection.getOutputStream();			
			objectMapper.writeValue(outputStream, payload);
			outputStream.flush();
			
			int responseCode = connection.getResponseCode();
			switch (responseCode) {
				case HttpURLConnection.HTTP_BAD_REQUEST:
					String errorBody = objectMapper.readValue((InputStream) connection.getErrorStream(), String.class);
					throw new IOException("Failed to connect with " + requestUrl + " due to " + errorBody);	
				default:
					throw new IOException("Failed to connect with " + requestUrl + " and status code is " + responseCode);
			}
			
		} catch (Exception e) {		
			throw new RuntimeException(e);			
		} finally {
			connection.disconnect();
	    }
	}
	
	/* ******************************************************************************************************** */	
	
	protected void delete(String requestPath) {
		
		HttpURLConnection connection = null;
		try {
			
			String requestUrl = providerBaseUrl + requestPath;
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
						
			ObjectMapper objectMapper = new ObjectMapper();
			
			int responseCode = connection.getResponseCode();			
			switch (responseCode) {
				case HttpURLConnection.HTTP_NOT_FOUND:
					String errorBody = objectMapper.readValue((InputStream) connection.getErrorStream(), String.class);
					throw new IOException("Failed to connect with " + requestUrl + " due to " + errorBody);	
				default:
					throw new IOException("Failed to connect with " + requestUrl + " and status code is " + responseCode);
			}
			
		} catch (Exception e) {		
			throw new RuntimeException(e);			
		} finally {
			connection.disconnect();
	    }		
	}
	
	/* ******************************************************************************************************** */	
	
	protected <T> void put(String requestPath, T payload) {
		
		HttpURLConnection connection = null;
		try {
			
			String requestUrl = providerBaseUrl + requestPath;
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);			
			
			ObjectMapper objectMapper = new ObjectMapper();			
			OutputStream outputStream = connection.getOutputStream();			
			objectMapper.writeValue(outputStream, payload);
			outputStream.flush();
			
			int responseCode = connection.getResponseCode();
			switch (responseCode) {
				case HttpURLConnection.HTTP_NOT_FOUND:
				case HttpURLConnection.HTTP_BAD_REQUEST:
					String errorBody = objectMapper.readValue((InputStream) connection.getErrorStream(), String.class);
					throw new IOException("Failed to connect with " + requestUrl + " due to " + errorBody);
				default:
					throw new IOException("Failed to connect with " + requestUrl + " and status code is " + responseCode);
			}
			
		} catch (Exception e) {		
			throw new RuntimeException(e);			
		} finally {
			connection.disconnect();
	    }
	}
	
}