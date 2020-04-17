package twitter4j.internal.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.org.json.JSONTokener;

public abstract class HttpResponse {
    private static final Logger logger = Logger.getLogger(HttpResponseImpl.class);
    protected final HttpClientConfiguration CONF;
    protected InputStream is;
    private JSONObject json;
    private JSONArray jsonArray;
    protected String responseAsString;
    protected int statusCode;
    private boolean streamConsumed;

    public abstract void disconnect() throws IOException;

    public abstract String getResponseHeader(String str);

    public abstract Map<String, List<String>> getResponseHeaderFields();

    HttpResponse() {
        this.responseAsString = null;
        this.streamConsumed = false;
        this.json = null;
        this.jsonArray = null;
        this.CONF = ConfigurationContext.getInstance();
    }

    public HttpResponse(HttpClientConfiguration conf) {
        this.responseAsString = null;
        this.streamConsumed = false;
        this.json = null;
        this.jsonArray = null;
        this.CONF = conf;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public InputStream asStream() {
        if (!this.streamConsumed) {
            return this.is;
        }
        throw new IllegalStateException("Stream has already been consumed.");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x004b A[SYNTHETIC, Splitter:B:28:0x004b] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0050 A[SYNTHETIC, Splitter:B:31:0x0050] */
    public String asString() throws TwitterException {
        if (this.responseAsString == null) {
            BufferedReader br = null;
            InputStream stream = null;
            try {
                stream = asStream();
                if (stream == null) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                        }
                    }
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e2) {
                        }
                    }
                    disconnectForcibly();
                    return null;
                }
                BufferedReader br2 = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                try {
                    StringBuilder buf = new StringBuilder();
                    while (true) {
                        String line = br2.readLine();
                        if (line == null) {
                            break;
                        }
                        buf.append(line).append("\n");
                    }
                    this.responseAsString = buf.toString();
                    logger.debug(this.responseAsString);
                    stream.close();
                    this.streamConsumed = true;
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e3) {
                        }
                    }
                    if (br2 != null) {
                        try {
                            br2.close();
                        } catch (IOException e4) {
                        }
                    }
                    disconnectForcibly();
                } catch (IOException e5) {
                    ioe = e5;
                    br = br2;
                    try {
                        throw new TwitterException(ioe.getMessage(), (Throwable) ioe);
                    } catch (Throwable th) {
                        th = th;
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e6) {
                            }
                        }
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e7) {
                            }
                        }
                        disconnectForcibly();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                    if (stream != null) {
                    }
                    if (br != null) {
                    }
                    disconnectForcibly();
                    throw th;
                }
            } catch (IOException e8) {
                ioe = e8;
            }
        }
        return this.responseAsString;
    }

    public JSONObject asJSONObject() throws TwitterException {
        if (this.json == null) {
            Reader reader = null;
            try {
                if (this.responseAsString == null) {
                    reader = asReader();
                    this.json = new JSONObject(new JSONTokener(reader));
                } else {
                    this.json = new JSONObject(this.responseAsString);
                }
                if (this.CONF.isPrettyDebugEnabled()) {
                    logger.debug(this.json.toString(1));
                } else {
                    logger.debug(this.responseAsString != null ? this.responseAsString : this.json.toString());
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
                disconnectForcibly();
            } catch (JSONException jsone) {
                if (this.responseAsString == null) {
                    throw new TwitterException(jsone.getMessage(), (Throwable) jsone);
                }
                throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, (Throwable) jsone);
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                    }
                }
                disconnectForcibly();
                throw th;
            }
        }
        return this.json;
    }

    public JSONArray asJSONArray() throws TwitterException {
        if (this.jsonArray == null) {
            Reader reader = null;
            try {
                if (this.responseAsString == null) {
                    reader = asReader();
                    this.jsonArray = new JSONArray(new JSONTokener(reader));
                } else {
                    this.jsonArray = new JSONArray(this.responseAsString);
                }
                if (this.CONF.isPrettyDebugEnabled()) {
                    logger.debug(this.jsonArray.toString(1));
                } else {
                    logger.debug(this.responseAsString != null ? this.responseAsString : this.jsonArray.toString());
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
                disconnectForcibly();
            } catch (JSONException jsone) {
                if (logger.isDebugEnabled()) {
                    throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, (Throwable) jsone);
                }
                throw new TwitterException(jsone.getMessage(), (Throwable) jsone);
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                    }
                }
                disconnectForcibly();
                throw th;
            }
        }
        return this.jsonArray;
    }

    public Reader asReader() {
        try {
            return new BufferedReader(new InputStreamReader(this.is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return new InputStreamReader(this.is);
        }
    }

    private void disconnectForcibly() {
        try {
            disconnect();
        } catch (Exception e) {
        }
    }

    public String toString() {
        return "HttpResponse{statusCode=" + this.statusCode + ", responseAsString='" + this.responseAsString + '\'' + ", is=" + this.is + ", streamConsumed=" + this.streamConsumed + '}';
    }
}
