package jp.co.cybird.barcodekanojoForGAM.core.http;

import java.io.File;
import org.apache.http.message.BasicNameValuePair;

public class NameValueOrFilePair extends BasicNameValuePair {
    private File file;

    public NameValueOrFilePair(String name, String value) {
        super(name, value);
    }

    public NameValueOrFilePair(String name, File file2) {
        super(name, null);
        this.file = file2;
    }

    public File getFile() {
        return this.file;
    }
}
