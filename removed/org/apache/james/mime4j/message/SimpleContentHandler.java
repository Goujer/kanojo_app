package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.descriptor.BodyDescriptor;
import org.apache.james.mime4j.field.AbstractField;
import org.apache.james.mime4j.parser.AbstractContentHandler;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.MimeUtil;

public abstract class SimpleContentHandler extends AbstractContentHandler {
    private Header currHeader;

    public abstract void bodyDecoded(BodyDescriptor bodyDescriptor, InputStream inputStream) throws IOException;

    public abstract void headers(Header header);

    public final void startHeader() {
        this.currHeader = new Header();
    }

    public final void field(Field field) throws MimeException {
        this.currHeader.addField(AbstractField.parse(field.getRaw()));
    }

    public final void endHeader() {
        Header tmp = this.currHeader;
        this.currHeader = null;
        headers(tmp);
    }

    public final void body(BodyDescriptor bd, InputStream is) throws IOException {
        if (MimeUtil.isBase64Encoding(bd.getTransferEncoding())) {
            bodyDecoded(bd, new Base64InputStream(is));
        } else if (MimeUtil.isQuotedPrintableEncoded(bd.getTransferEncoding())) {
            bodyDecoded(bd, new QuotedPrintableInputStream(is));
        } else {
            bodyDecoded(bd, is);
        }
    }
}
