package org.apache.james.mime4j.field.address.parser;

public class ASTaddress extends SimpleNode {
    public ASTaddress(int id) {
        super(id);
    }

    public ASTaddress(AddressListParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
