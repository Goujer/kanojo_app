package org.apache.james.mime4j.field.address.parser;

public class ASTmailbox extends SimpleNode {
    public ASTmailbox(int id) {
        super(id);
    }

    public ASTmailbox(AddressListParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
