/*
 * Copyright 2011 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;

public final class HttpHelper {
	private static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType;
	private static final Collection<String> REDIRECTOR_DOMAINS = new HashSet(Arrays.asList(new String[]{"amzn.to", "bit.ly", "bitly.com", "fb.me", "goo.gl", "is.gd", "j.mp", "lnkd.in", "ow.ly", "R.BEETAGG.COM", "r.beetagg.com", "SCN.BY", "su.pr", "t.co", "tinyurl.com", "tr.im"}));
	private static final String TAG = HttpHelper.class.getSimpleName();

	public enum ContentType {
		HTML,
		JSON,
		XML,
		TEXT
	}

	static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType() {
		int[] iArr = $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType;
		if (iArr == null) {
			iArr = new int[ContentType.values().length];
			try {
				iArr[ContentType.HTML.ordinal()] = 1;
			} catch (NoSuchFieldError e) {
			}
			try {
				iArr[ContentType.JSON.ordinal()] = 2;
			} catch (NoSuchFieldError e2) {
			}
			try {
				iArr[ContentType.TEXT.ordinal()] = 4;
			} catch (NoSuchFieldError e3) {
			}
			try {
				iArr[ContentType.XML.ordinal()] = 3;
			} catch (NoSuchFieldError e4) {
			}
			$SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType = iArr;
		}
		return iArr;
	}

	private HttpHelper() {
	}

	public static CharSequence downloadViaHttp(String uri, ContentType type) throws IOException {
		return downloadViaHttp(uri, type, Integer.MAX_VALUE);
	}

	public static CharSequence downloadViaHttp(String uri, ContentType type, int maxChars) throws IOException {
		String contentTypes;
		switch ($SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType()[type.ordinal()]) {
			case 1:
				contentTypes = "application/xhtml+xml,text/html,text/*,*/*";
				break;
			case 2:
				contentTypes = "application/json,text/*,*/*";
				break;
			case 3:
				contentTypes = "application/xml,text/*,*/*";
				break;
			default:
				contentTypes = "text/*,*/*";
				break;
		}
		return downloadViaHttp(uri, contentTypes, maxChars);
	}

	private static CharSequence downloadViaHttp(String uri, String contentTypes, int maxChars) throws IOException {
		int redirects = 0;
		while (redirects < 5) {
			URL url = new URL(uri);
			HttpURLConnection connection = safelyOpenConnection(url);
			connection.setInstanceFollowRedirects(true); // Won't work HTTP -> HTTPS or vice versa
			connection.setRequestProperty("Accept", contentTypes);
			connection.setRequestProperty("Accept-Charset", "utf-8,*");
			connection.setRequestProperty("User-Agent", "ZXing (Android)");
			try {
				int responseCode = safelyConnect(connection);
				switch (responseCode) {
					case HttpURLConnection.HTTP_OK:
						return consume(connection, maxChars);
					case HttpURLConnection.HTTP_MOVED_TEMP:
						String location = connection.getHeaderField("Location");
						if (location != null) {
							uri = location;
							redirects++;
							continue;
						}
						throw new IOException("No Location");
					default:
						throw new IOException("Bad HTTP response: " + responseCode);
				}
			} finally {
				connection.disconnect();
			}
		}
		throw new IOException("Too many redirects");
	}

	private static String getEncoding(URLConnection connection) {
		int charsetStart;
		String contentTypeHeader = connection.getHeaderField("Content-Type");
		if (contentTypeHeader == null || (charsetStart = contentTypeHeader.indexOf("charset=")) < 0) {
			return "UTF-8";
		}
		return contentTypeHeader.substring("charset=".length() + charsetStart);
	}

	private static CharSequence consume(URLConnection connection, int maxChars) throws IOException {
		String encoding = getEncoding(connection);
		StringBuilder out = new StringBuilder();
		try {
			Reader in = new InputStreamReader(connection.getInputStream(), encoding);
			char[] buffer = new char[1024];
			int charsRead;
			while (out.length() < maxChars && (charsRead = in.read(buffer)) > 0) {
				out.append(buffer, 0, charsRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public static URI unredirect(URI uri) throws IOException {
		if (!REDIRECTOR_DOMAINS.contains(uri.getHost())) {
			return uri;
		}
		URL url = uri.toURL();
		HttpURLConnection connection = safelyOpenConnection(url);
		connection.setInstanceFollowRedirects(false);
		connection.setDoInput(false);
		connection.setRequestMethod("HEAD");
		connection.setRequestProperty("User-Agent", "ZXing (Android)");
		try {
			int responseCode = safelyConnect(connection);
			switch (responseCode) {
				case HttpURLConnection.HTTP_MULT_CHOICE:
				case HttpURLConnection.HTTP_MOVED_PERM:
				case HttpURLConnection.HTTP_MOVED_TEMP:
				case HttpURLConnection.HTTP_SEE_OTHER:
				case 307: // No constant for 307 Temporary Redirect ?
					String location = connection.getHeaderField("Location");
					if (location != null) {
						try {
							return new URI(location);
						} catch (URISyntaxException e) {
							// nevermind
						}
					}
			}
			return uri;
		} finally {
			connection.disconnect();
		}
	}

	private static HttpURLConnection safelyOpenConnection(URL url) throws IOException {
		try {
			URLConnection conn = url.openConnection();
			if (conn instanceof HttpURLConnection) {
				return (HttpURLConnection) conn;
			}
			throw new IOException();
		} catch (NullPointerException npe) {
			Log.w(TAG, "Bad URI? " + url);
			throw new IOException(npe.toString());
		}
	}

	private static int safelyConnect(HttpURLConnection connection) throws IOException {
		try {
			connection.connect();
		} catch (RuntimeException e) {
			// These are, generally, Android bugs
			throw new IOException(e);
		}
		try {
			return connection.getResponseCode();
		} catch (NullPointerException | StringIndexOutOfBoundsException | IllegalArgumentException e) {
			// this is maybe this Android bug: http://code.google.com/p/android/issues/detail?id=15554
			throw new IOException(e);
		}
	}
}
