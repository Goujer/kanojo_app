package org.apache.james.mime4j.field.address.parser;

public class ASTdomain extends SimpleNode {
    public ASTdomain(int id) {
        super(id);
    }

    public ASTdomain(AddressListParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
