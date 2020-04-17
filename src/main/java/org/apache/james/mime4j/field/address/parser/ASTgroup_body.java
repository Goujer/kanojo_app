package org.apache.james.mime4j.field.address.parser;

public class ASTgroup_body extends SimpleNode {
    public ASTgroup_body(int id) {
        super(id);
    }

    public ASTgroup_body(AddressListParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
