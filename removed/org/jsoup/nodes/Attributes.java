package org.jsoup.nodes;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

public class Attributes implements Iterable<Attribute>, Cloneable {
    protected static final String dataPrefix = "data-";
    /* access modifiers changed from: private */
    public LinkedHashMap<String, Attribute> attributes = null;

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000a, code lost:
        r0 = r3.attributes.get(r4.toLowerCase());
     */
    public String get(String key) {
        Attribute attr;
        Validate.notEmpty(key);
        return (this.attributes == null || attr == null) ? "" : attr.getValue();
    }

    public void put(String key, String value) {
        put(new Attribute(key, value));
    }

    public void put(Attribute attribute) {
        Validate.notNull(attribute);
        if (this.attributes == null) {
            this.attributes = new LinkedHashMap<>(2);
        }
        this.attributes.put(attribute.getKey(), attribute);
    }

    public void remove(String key) {
        Validate.notEmpty(key);
        if (this.attributes != null) {
            this.attributes.remove(key.toLowerCase());
        }
    }

    public boolean hasKey(String key) {
        return this.attributes != null && this.attributes.containsKey(key.toLowerCase());
    }

    public int size() {
        if (this.attributes == null) {
            return 0;
        }
        return this.attributes.size();
    }

    public void addAll(Attributes incoming) {
        if (incoming.size() != 0) {
            if (this.attributes == null) {
                this.attributes = new LinkedHashMap<>(incoming.size());
            }
            this.attributes.putAll(incoming.attributes);
        }
    }

    public Iterator<Attribute> iterator() {
        return asList().iterator();
    }

    public List<Attribute> asList() {
        if (this.attributes == null) {
            return Collections.emptyList();
        }
        List<Attribute> list = new ArrayList<>(this.attributes.size());
        for (Map.Entry<String, Attribute> entry : this.attributes.entrySet()) {
            list.add(entry.getValue());
        }
        return Collections.unmodifiableList(list);
    }

    public Map<String, String> dataset() {
        return new Dataset();
    }

    public String html() {
        StringBuilder accum = new StringBuilder();
        html(accum, new Document("").outputSettings());
        return accum.toString();
    }

    /* access modifiers changed from: package-private */
    public void html(StringBuilder accum, Document.OutputSettings out) {
        if (this.attributes != null) {
            for (Map.Entry<String, Attribute> entry : this.attributes.entrySet()) {
                accum.append(" ");
                entry.getValue().html(accum, out);
            }
        }
    }

    public String toString() {
        return html();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attributes)) {
            return false;
        }
        Attributes that = (Attributes) o;
        if (this.attributes != null) {
            if (this.attributes.equals(that.attributes)) {
                return true;
            }
        } else if (that.attributes == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.attributes != null) {
            return this.attributes.hashCode();
        }
        return 0;
    }

    public Attributes clone() {
        if (this.attributes == null) {
            return new Attributes();
        }
        try {
            Attributes clone = (Attributes) super.clone();
            clone.attributes = new LinkedHashMap<>(this.attributes.size());
            Iterator i$ = iterator();
            while (i$.hasNext()) {
                Attribute attribute = i$.next();
                clone.attributes.put(attribute.getKey(), attribute.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private class Dataset extends AbstractMap<String, String> {
        private Dataset() {
            if (Attributes.this.attributes == null) {
                LinkedHashMap unused = Attributes.this.attributes = new LinkedHashMap(2);
            }
        }

        public Set<Map.Entry<String, String>> entrySet() {
            return new EntrySet();
        }

        public String put(String key, String value) {
            String dataKey = Attributes.dataKey(key);
            String oldValue = Attributes.this.hasKey(dataKey) ? ((Attribute) Attributes.this.attributes.get(dataKey)).getValue() : null;
            Attributes.this.attributes.put(dataKey, new Attribute(dataKey, value));
            return oldValue;
        }

        private class EntrySet extends AbstractSet<Map.Entry<String, String>> {
            private EntrySet() {
            }

            public Iterator<Map.Entry<String, String>> iterator() {
                return new DatasetIterator();
            }

            public int size() {
                int count = 0;
                while (new DatasetIterator().hasNext()) {
                    count++;
                }
                return count;
            }
        }

        private class DatasetIterator implements Iterator<Map.Entry<String, String>> {
            private Attribute attr;
            private Iterator<Attribute> attrIter;

            private DatasetIterator() {
                this.attrIter = Attributes.this.attributes.values().iterator();
            }

            public boolean hasNext() {
                while (this.attrIter.hasNext()) {
                    this.attr = this.attrIter.next();
                    if (this.attr.isDataAttribute()) {
                        return true;
                    }
                }
                return false;
            }

            public Map.Entry<String, String> next() {
                return new Attribute(this.attr.getKey().substring(Attributes.dataPrefix.length()), this.attr.getValue());
            }

            public void remove() {
                Attributes.this.attributes.remove(this.attr.getKey());
            }
        }
    }

    /* access modifiers changed from: private */
    public static String dataKey(String key) {
        return dataPrefix + key;
    }
}
