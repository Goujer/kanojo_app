package org.apache.james.mime4j.message;

public class BodyCopier {
    private BodyCopier() {
    }

    public static Body copy(Body body) {
        if (body == null) {
            throw new IllegalArgumentException("Body is null");
        } else if (body instanceof Message) {
            return new Message((Message) body);
        } else {
            if (body instanceof Multipart) {
                return new Multipart((Multipart) body);
            }
            if (body instanceof SingleBody) {
                return ((SingleBody) body).copy();
            }
            throw new IllegalArgumentException("Unsupported body class");
        }
    }
}
