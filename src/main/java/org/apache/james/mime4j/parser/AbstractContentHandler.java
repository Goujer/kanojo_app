package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.descriptor.BodyDescriptor;

public abstract class AbstractContentHandler implements ContentHandler {
    public void endMultipart() throws MimeException {
    }

    public void startMultipart(BodyDescriptor bd) throws MimeException {
    }

    public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {
    }

    public void endBodyPart() throws MimeException {
    }

    public void endHeader() throws MimeException {
    }

    public void endMessage() throws MimeException {
    }

    public void epilogue(InputStream is) throws MimeException, IOException {
    }

    public void field(Field field) throws MimeException {
    }

    public void preamble(InputStream is) throws MimeException, IOException {
    }

    public void startBodyPart() throws MimeException {
    }

    public void startHeader() throws MimeException {
    }

    public void startMessage() throws MimeException {
    }

    public void raw(InputStream is) throws MimeException, IOException {
    }
}
