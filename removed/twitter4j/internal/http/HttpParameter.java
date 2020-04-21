package twitter4j.internal.http;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public final class HttpParameter implements Comparable, Serializable {
    private static final String GIF = "image/gif";
    private static final String JPEG = "image/jpeg";
    private static final String OCTET = "application/octet-stream";
    private static final String PNG = "image/png";
    private static final long serialVersionUID = -8708108746980739212L;
    private File file = null;
    private InputStream fileBody = null;
    private String name = null;
    private String value = null;

    public HttpParameter(String name2, String value2) {
        this.name = name2;
        this.value = value2;
    }

    public HttpParameter(String name2, File file2) {
        this.name = name2;
        this.file = file2;
    }

    public HttpParameter(String name2, String fileName, InputStream fileBody2) {
        this.name = name2;
        this.file = new File(fileName);
        this.fileBody = fileBody2;
    }

    public HttpParameter(String name2, int value2) {
        this.name = name2;
        this.value = String.valueOf(value2);
    }

    public HttpParameter(String name2, long value2) {
        this.name = name2;
        this.value = String.valueOf(value2);
    }

    public HttpParameter(String name2, double value2) {
        this.name = name2;
        this.value = String.valueOf(value2);
    }

    public HttpParameter(String name2, boolean value2) {
        this.name = name2;
        this.value = String.valueOf(value2);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public File getFile() {
        return this.file;
    }

    public InputStream getFileBody() {
        return this.fileBody;
    }

    public boolean isFile() {
        return this.file != null;
    }

    public boolean hasFileBody() {
        return this.fileBody != null;
    }

    public String getContentType() {
        if (!isFile()) {
            throw new IllegalStateException("not a file");
        }
        String extensions = this.file.getName();
        if (-1 == extensions.lastIndexOf(".")) {
            return OCTET;
        }
        String extensions2 = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
        if (extensions2.length() == 3) {
            if ("gif".equals(extensions2)) {
                return GIF;
            }
            if ("png".equals(extensions2)) {
                return PNG;
            }
            if ("jpg".equals(extensions2)) {
                return JPEG;
            }
            return OCTET;
        } else if (extensions2.length() != 4 || !"jpeg".equals(extensions2)) {
            return OCTET;
        } else {
            return JPEG;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpParameter)) {
            return false;
        }
        HttpParameter that = (HttpParameter) o;
        if (this.file == null ? that.file != null : !this.file.equals(that.file)) {
            return false;
        }
        if (this.fileBody == null ? that.fileBody != null : !this.fileBody.equals(that.fileBody)) {
            return false;
        }
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(that.value)) {
                return true;
            }
        } else if (that.value == null) {
            return true;
        }
        return false;
    }

    public static boolean containsFile(HttpParameter[] params) {
        boolean containsFile = false;
        if (params == null) {
            return false;
        }
        HttpParameter[] arr$ = params;
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            if (i$ >= len$) {
                break;
            } else if (arr$[i$].isFile()) {
                containsFile = true;
                break;
            } else {
                i$++;
            }
        }
        return containsFile;
    }

    static boolean containsFile(List<HttpParameter> params) {
        for (HttpParameter param : params) {
            if (param.isFile()) {
                return true;
            }
        }
        return false;
    }

    public static HttpParameter[] getParameterArray(String name2, String value2) {
        return new HttpParameter[]{new HttpParameter(name2, value2)};
    }

    public static HttpParameter[] getParameterArray(String name2, int value2) {
        return getParameterArray(name2, String.valueOf(value2));
    }

    public static HttpParameter[] getParameterArray(String name1, String value1, String name2, String value2) {
        return new HttpParameter[]{new HttpParameter(name1, value1), new HttpParameter(name2, value2)};
    }

    public static HttpParameter[] getParameterArray(String name1, int value1, String name2, int value2) {
        return getParameterArray(name1, String.valueOf(value1), name2, String.valueOf(value2));
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int hashCode = this.name.hashCode() * 31;
        if (this.value != null) {
            i = this.value.hashCode();
        } else {
            i = 0;
        }
        int i4 = (hashCode + i) * 31;
        if (this.file != null) {
            i2 = this.file.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 31;
        if (this.fileBody != null) {
            i3 = this.fileBody.hashCode();
        }
        return i5 + i3;
    }

    public String toString() {
        return "PostParameter{name='" + this.name + '\'' + ", value='" + this.value + '\'' + ", file=" + this.file + ", fileBody=" + this.fileBody + '}';
    }

    public int compareTo(Object o) {
        HttpParameter that = (HttpParameter) o;
        int compared = this.name.compareTo(that.name);
        if (compared == 0) {
            return this.value.compareTo(that.value);
        }
        return compared;
    }

    public static String encodeParameters(HttpParameter[] httpParams) {
        if (httpParams == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (int j = 0; j < httpParams.length; j++) {
            if (httpParams[j].isFile()) {
                throw new IllegalArgumentException("parameter [" + httpParams[j].name + "]should be text");
            }
            if (j != 0) {
                buf.append("&");
            }
            buf.append(encode(httpParams[j].name)).append("=").append(encode(httpParams[j].value));
        }
        return buf.toString();
    }

    public static String encode(String value2) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value2, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        int i = 0;
        while (i < encoded.length()) {
            char focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
            i++;
        }
        return buf.toString();
    }
}
