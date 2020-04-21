package org.apache.james.mime4j.field.address.parser;

public interface AddressListParserVisitor {
    Object visit(ASTaddr_spec aSTaddr_spec, Object obj);

    Object visit(ASTaddress aSTaddress, Object obj);

    Object visit(ASTaddress_list aSTaddress_list, Object obj);

    Object visit(ASTangle_addr aSTangle_addr, Object obj);

    Object visit(ASTdomain aSTdomain, Object obj);

    Object visit(ASTgroup_body aSTgroup_body, Object obj);

    Object visit(ASTlocal_part aSTlocal_part, Object obj);

    Object visit(ASTmailbox aSTmailbox, Object obj);

    Object visit(ASTname_addr aSTname_addr, Object obj);

    Object visit(ASTphrase aSTphrase, Object obj);

    Object visit(ASTroute aSTroute, Object obj);

    Object visit(SimpleNode simpleNode, Object obj);
}
