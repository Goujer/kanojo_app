package org.apache.james.mime4j.field.address.parser;

public class ASTname_addr extends SimpleNode {
    public ASTname_addr(int id) {
        super(id);
    }

    public ASTname_addr(AddressListParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
