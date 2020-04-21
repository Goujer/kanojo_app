package twitter4j.internal.json;

import java.io.Serializable;

abstract class EntityIndex implements Comparable<EntityIndex>, Serializable {
    private static final long serialVersionUID = 3864336402689899384L;
    private int end = -1;
    private int start = -1;

    EntityIndex() {
    }

    public int compareTo(EntityIndex that) {
        long delta = (long) (this.start - that.start);
        if (delta < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (delta > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    /* access modifiers changed from: package-private */
    public void setStart(int start2) {
        this.start = start2;
    }

    /* access modifiers changed from: package-private */
    public void setEnd(int end2) {
        this.end = end2;
    }

    /* access modifiers changed from: package-private */
    public int getStart() {
        return this.start;
    }

    /* access modifiers changed from: package-private */
    public int getEnd() {
        return this.end;
    }
}
