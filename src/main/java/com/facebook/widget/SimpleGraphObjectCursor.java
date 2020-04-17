package com.facebook.widget;

import android.database.CursorIndexOutOfBoundsException;
import com.facebook.model.GraphObject;
import java.util.ArrayList;
import java.util.Collection;

class SimpleGraphObjectCursor<T extends GraphObject> implements GraphObjectCursor<T> {
    private boolean closed = false;
    private boolean fromCache = false;
    private ArrayList<T> graphObjects = new ArrayList<>();
    private boolean moreObjectsAvailable = false;
    private int pos = -1;

    SimpleGraphObjectCursor() {
    }

    SimpleGraphObjectCursor(SimpleGraphObjectCursor<T> other) {
        this.pos = other.pos;
        this.closed = other.closed;
        this.graphObjects = new ArrayList<>();
        this.graphObjects.addAll(other.graphObjects);
        this.fromCache = other.fromCache;
    }

    public void addGraphObjects(Collection<T> graphObjects2, boolean fromCache2) {
        this.graphObjects.addAll(graphObjects2);
        this.fromCache |= fromCache2;
    }

    public boolean isFromCache() {
        return this.fromCache;
    }

    public void setFromCache(boolean fromCache2) {
        this.fromCache = fromCache2;
    }

    public boolean areMoreObjectsAvailable() {
        return this.moreObjectsAvailable;
    }

    public void setMoreObjectsAvailable(boolean moreObjectsAvailable2) {
        this.moreObjectsAvailable = moreObjectsAvailable2;
    }

    public int getCount() {
        return this.graphObjects.size();
    }

    public int getPosition() {
        return this.pos;
    }

    public boolean move(int offset) {
        return moveToPosition(this.pos + offset);
    }

    public boolean moveToPosition(int position) {
        int count = getCount();
        if (position >= count) {
            this.pos = count;
            return false;
        } else if (position < 0) {
            this.pos = -1;
            return false;
        } else {
            this.pos = position;
            return true;
        }
    }

    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    public boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    public boolean moveToNext() {
        return moveToPosition(this.pos + 1);
    }

    public boolean moveToPrevious() {
        return moveToPosition(this.pos - 1);
    }

    public boolean isFirst() {
        return this.pos == 0 && getCount() != 0;
    }

    public boolean isLast() {
        int count = getCount();
        return this.pos == count + -1 && count != 0;
    }

    public boolean isBeforeFirst() {
        return getCount() == 0 || this.pos == -1;
    }

    public boolean isAfterLast() {
        int count = getCount();
        return count == 0 || this.pos == count;
    }

    public T getGraphObject() {
        if (this.pos < 0) {
            throw new CursorIndexOutOfBoundsException("Before first object.");
        } else if (this.pos < this.graphObjects.size()) {
            return (GraphObject) this.graphObjects.get(this.pos);
        } else {
            throw new CursorIndexOutOfBoundsException("After last object.");
        }
    }

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }
}
