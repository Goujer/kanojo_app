package org.apache.james.mime4j.field.address.parser;

public class SimpleNode extends BaseNode implements Node {
    protected Node[] children;
    protected int id;
    protected Node parent;
    protected AddressListParser parser;

    public SimpleNode(int i) {
        this.id = i;
    }

    public SimpleNode(AddressListParser p, int i) {
        this(i);
        this.parser = p;
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(Node n) {
        this.parent = n;
    }

    public Node jjtGetParent() {
        return this.parent;
    }

    public void jjtAddChild(Node n, int i) {
        if (this.children == null) {
            this.children = new Node[(i + 1)];
        } else if (i >= this.children.length) {
            Node[] c = new Node[(i + 1)];
            System.arraycopy(this.children, 0, c, 0, this.children.length);
            this.children = c;
        }
        this.children[i] = n;
    }

    public Node jjtGetChild(int i) {
        return this.children[i];
    }

    public int jjtGetNumChildren() {
        if (this.children == null) {
            return 0;
        }
        return this.children.length;
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public Object childrenAccept(AddressListParserVisitor visitor, Object data) {
        if (this.children != null) {
            for (Node jjtAccept : this.children) {
                jjtAccept.jjtAccept(visitor, data);
            }
        }
        return data;
    }

    public String toString() {
        return AddressListParserTreeConstants.jjtNodeName[this.id];
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        if (this.children != null) {
            for (Node node : this.children) {
                SimpleNode n = (SimpleNode) node;
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }
}
