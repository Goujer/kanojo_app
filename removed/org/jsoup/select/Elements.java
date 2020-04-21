package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;

public class Elements implements List<Element>, Cloneable {
    private List<Element> contents;

    public Elements() {
        this.contents = new ArrayList();
    }

    public Elements(int initialCapacity) {
        this.contents = new ArrayList(initialCapacity);
    }

    public Elements(Collection<Element> elements) {
        this.contents = new ArrayList(elements);
    }

    public Elements(List<Element> elements) {
        this.contents = elements;
    }

    public Elements(Element... elements) {
        this((List<Element>) Arrays.asList(elements));
    }

    public Elements clone() {
        List<Element> elements = new ArrayList<>();
        for (Element e : this.contents) {
            elements.add(e.clone());
        }
        return new Elements(elements);
    }

    public String attr(String attributeKey) {
        for (Element element : this.contents) {
            if (element.hasAttr(attributeKey)) {
                return element.attr(attributeKey);
            }
        }
        return "";
    }

    public boolean hasAttr(String attributeKey) {
        for (Element element : this.contents) {
            if (element.hasAttr(attributeKey)) {
                return true;
            }
        }
        return false;
    }

    public Elements attr(String attributeKey, String attributeValue) {
        for (Element element : this.contents) {
            element.attr(attributeKey, attributeValue);
        }
        return this;
    }

    public Elements removeAttr(String attributeKey) {
        for (Element element : this.contents) {
            element.removeAttr(attributeKey);
        }
        return this;
    }

    public Elements addClass(String className) {
        for (Element element : this.contents) {
            element.addClass(className);
        }
        return this;
    }

    public Elements removeClass(String className) {
        for (Element element : this.contents) {
            element.removeClass(className);
        }
        return this;
    }

    public Elements toggleClass(String className) {
        for (Element element : this.contents) {
            element.toggleClass(className);
        }
        return this;
    }

    public boolean hasClass(String className) {
        for (Element element : this.contents) {
            if (element.hasClass(className)) {
                return true;
            }
        }
        return false;
    }

    public String val() {
        if (size() > 0) {
            return first().val();
        }
        return "";
    }

    public Elements val(String value) {
        for (Element element : this.contents) {
            element.val(value);
        }
        return this;
    }

    public String text() {
        StringBuilder sb = new StringBuilder();
        for (Element element : this.contents) {
            if (sb.length() != 0) {
                sb.append(" ");
            }
            sb.append(element.text());
        }
        return sb.toString();
    }

    public boolean hasText() {
        for (Element element : this.contents) {
            if (element.hasText()) {
                return true;
            }
        }
        return false;
    }

    public String html() {
        StringBuilder sb = new StringBuilder();
        for (Element element : this.contents) {
            if (sb.length() != 0) {
                sb.append("\n");
            }
            sb.append(element.html());
        }
        return sb.toString();
    }

    public String outerHtml() {
        StringBuilder sb = new StringBuilder();
        for (Element element : this.contents) {
            if (sb.length() != 0) {
                sb.append("\n");
            }
            sb.append(element.outerHtml());
        }
        return sb.toString();
    }

    public String toString() {
        return outerHtml();
    }

    public Elements tagName(String tagName) {
        for (Element element : this.contents) {
            element.tagName(tagName);
        }
        return this;
    }

    public Elements html(String html) {
        for (Element element : this.contents) {
            element.html(html);
        }
        return this;
    }

    public Elements prepend(String html) {
        for (Element element : this.contents) {
            element.prepend(html);
        }
        return this;
    }

    public Elements append(String html) {
        for (Element element : this.contents) {
            element.append(html);
        }
        return this;
    }

    public Elements before(String html) {
        for (Element element : this.contents) {
            element.before(html);
        }
        return this;
    }

    public Elements after(String html) {
        for (Element element : this.contents) {
            element.after(html);
        }
        return this;
    }

    public Elements wrap(String html) {
        Validate.notEmpty(html);
        for (Element element : this.contents) {
            element.wrap(html);
        }
        return this;
    }

    public Elements unwrap() {
        for (Element element : this.contents) {
            element.unwrap();
        }
        return this;
    }

    public Elements empty() {
        for (Element element : this.contents) {
            element.empty();
        }
        return this;
    }

    public Elements remove() {
        for (Element element : this.contents) {
            element.remove();
        }
        return this;
    }

    public Elements select(String query) {
        return Selector.select(query, (Iterable<Element>) this);
    }

    public Elements not(String query) {
        return Selector.filterOut(this, Selector.select(query, (Iterable<Element>) this));
    }

    public Elements eq(int index) {
        if (this.contents.size() <= index) {
            return new Elements();
        }
        return new Elements(get(index));
    }

    public boolean is(String query) {
        return !select(query).isEmpty();
    }

    public Elements parents() {
        HashSet<Element> combo = new LinkedHashSet<>();
        for (Element e : this.contents) {
            combo.addAll(e.parents());
        }
        return new Elements((Collection<Element>) combo);
    }

    public Element first() {
        if (this.contents.isEmpty()) {
            return null;
        }
        return this.contents.get(0);
    }

    public Element last() {
        if (this.contents.isEmpty()) {
            return null;
        }
        return this.contents.get(this.contents.size() - 1);
    }

    public Elements traverse(NodeVisitor nodeVisitor) {
        Validate.notNull(nodeVisitor);
        NodeTraversor traversor = new NodeTraversor(nodeVisitor);
        for (Element el : this.contents) {
            traversor.traverse(el);
        }
        return this;
    }

    public int size() {
        return this.contents.size();
    }

    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    public boolean contains(Object o) {
        return this.contents.contains(o);
    }

    public Iterator<Element> iterator() {
        return this.contents.iterator();
    }

    public Object[] toArray() {
        return this.contents.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.contents.toArray(a);
    }

    public boolean add(Element element) {
        return this.contents.add(element);
    }

    public boolean remove(Object o) {
        return this.contents.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.contents.containsAll(c);
    }

    public boolean addAll(Collection<? extends Element> c) {
        return this.contents.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Element> c) {
        return this.contents.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return this.contents.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.contents.retainAll(c);
    }

    public void clear() {
        this.contents.clear();
    }

    public boolean equals(Object o) {
        return this.contents.equals(o);
    }

    public int hashCode() {
        return this.contents.hashCode();
    }

    public Element get(int index) {
        return this.contents.get(index);
    }

    public Element set(int index, Element element) {
        return this.contents.set(index, element);
    }

    public void add(int index, Element element) {
        this.contents.add(index, element);
    }

    public Element remove(int index) {
        return this.contents.remove(index);
    }

    public int indexOf(Object o) {
        return this.contents.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.contents.lastIndexOf(o);
    }

    public ListIterator<Element> listIterator() {
        return this.contents.listIterator();
    }

    public ListIterator<Element> listIterator(int index) {
        return this.contents.listIterator(index);
    }

    public List<Element> subList(int fromIndex, int toIndex) {
        return this.contents.subList(fromIndex, toIndex);
    }
}
