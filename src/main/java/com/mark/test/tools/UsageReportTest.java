package com.mark.test.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UsageReportTest {

	private static final String DEFAULT_URL = "https://localhost:8080";

	public static void main(String[] args) throws Exception {
		String baseUrl = args.length > 0 ? args[0] : DEFAULT_URL;

		TrustManager[] trustAll = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
		} };
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAll, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

		System.out.println("===== Token 概览 (/api/report/token-summary) =====");
		doGet(baseUrl + "/api/report/token-summary");

		System.out.println("\n===== 请求记录 (/api/report/request-logs) =====");
		doGet(baseUrl + "/api/report/request-logs");

		System.out.println("\n===== 完成 =====");
	}

	private static void doGet(String urlStr) throws Exception {
		URL url = new URI(urlStr).toURL();
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);

		int status = conn.getResponseCode();
		System.out.println("Status: " + status);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				status >= 400 ? conn.getErrorStream() : conn.getInputStream()));
		StringBuilder body = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			body.append(line).append("\n");
		}
		reader.close();

		System.out.println("Body:");
		System.out.println(formatJson(body.toString()));
		conn.disconnect();
	}

	private static String formatJson(String json) {
		if (json == null || json.isEmpty()) return json;
		StringBuilder sb = new StringBuilder();
		int indent = 0;
		boolean inString = false;
		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
				inString = !inString;
			}
			if (!inString && (c == '{' || c == '[')) {
				sb.append(c);
				sb.append('\n');
				indent++;
				appendIndent(sb, indent);
			} else if (!inString && (c == '}' || c == ']')) {
				sb.append('\n');
				indent--;
				appendIndent(sb, indent);
				sb.append(c);
			} else if (!inString && c == ',') {
				sb.append(c);
				sb.append('\n');
				appendIndent(sb, indent);
			} else if (!inString && c == ':') {
				sb.append(": ");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static void appendIndent(StringBuilder sb, int indent) {
		for (int j = 0; j < indent; j++) {
			sb.append("  ");
		}
	}
}
