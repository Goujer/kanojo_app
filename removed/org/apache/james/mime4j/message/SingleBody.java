package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.OutputStream;

public abstract class SingleBody implements Body {
    private Entity parent = null;

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    protected SingleBody() {
    }

    public Entity getParent() {
        return this.parent;
    }

    public void setParent(Entity parent2) {
        this.parent = parent2;
    }

    public SingleBody copy() {
        throw new UnsupportedOperationException();
    }

    public void dispose() {
    }
}
