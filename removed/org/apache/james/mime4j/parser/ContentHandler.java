package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.descriptor.BodyDescriptor;

public interface ContentHandler {
    void body(BodyDescriptor bodyDescriptor, InputStream inputStream) throws MimeException, IOException;

    void endBodyPart() throws MimeException;

    void endHeader() throws MimeException;

    void endMessage() throws MimeException;

    void endMultipart() throws MimeException;

    void epilogue(InputStream inputStream) throws MimeException, IOException;

    void field(Field field) throws MimeException;

    void preamble(InputStream inputStream) throws MimeException, IOException;

    void raw(InputStream inputStream) throws MimeException, IOException;

    void startBodyPart() throws MimeException;

    void startHeader() throws MimeException;

    void startMessage() throws MimeException;

    void startMultipart(BodyDescriptor bodyDescriptor) throws MimeException;
}
