package org.apache.james.mime4j.field.address.parser;

public class ASTroute extends SimpleNode {
    public ASTroute(int id) {
        super(id);
    }

    public ASTroute(AddressListParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
