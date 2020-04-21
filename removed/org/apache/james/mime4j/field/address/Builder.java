package org.apache.james.mime4j.field.address;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.field.address.parser.ASTaddr_spec;
import org.apache.james.mime4j.field.address.parser.ASTaddress;
import org.apache.james.mime4j.field.address.parser.ASTaddress_list;
import org.apache.james.mime4j.field.address.parser.ASTangle_addr;
import org.apache.james.mime4j.field.address.parser.ASTdomain;
import org.apache.james.mime4j.field.address.parser.ASTgroup_body;
import org.apache.james.mime4j.field.address.parser.ASTlocal_part;
import org.apache.james.mime4j.field.address.parser.ASTmailbox;
import org.apache.james.mime4j.field.address.parser.ASTname_addr;
import org.apache.james.mime4j.field.address.parser.ASTphrase;
import org.apache.james.mime4j.field.address.parser.ASTroute;
import org.apache.james.mime4j.field.address.parser.Node;
import org.apache.james.mime4j.field.address.parser.SimpleNode;
import org.apache.james.mime4j.field.address.parser.Token;

class Builder {
    private static Builder singleton = new Builder();

    Builder() {
    }

    public static Builder getInstance() {
        return singleton;
    }

    public AddressList buildAddressList(ASTaddress_list node) {
        List<Address> list = new ArrayList<>();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            list.add(buildAddress((ASTaddress) node.jjtGetChild(i)));
        }
        return new AddressList(list, true);
    }

    public Address buildAddress(ASTaddress node) {
        ChildNodeIterator it = new ChildNodeIterator(node);
        Node n = it.next();
        if (n instanceof ASTaddr_spec) {
            return buildAddrSpec((ASTaddr_spec) n);
        }
        if (n instanceof ASTangle_addr) {
            return buildAngleAddr((ASTangle_addr) n);
        }
        if (n instanceof ASTphrase) {
            String name = buildString((ASTphrase) n, false);
            Node n2 = it.next();
            if (n2 instanceof ASTgroup_body) {
                return new Group(name, buildGroupBody((ASTgroup_body) n2));
            }
            if (n2 instanceof ASTangle_addr) {
                return new Mailbox(DecoderUtil.decodeEncodedWords(name), buildAngleAddr((ASTangle_addr) n2));
            }
            throw new IllegalStateException();
        }
        throw new IllegalStateException();
    }

    private MailboxList buildGroupBody(ASTgroup_body node) {
        List<Mailbox> results = new ArrayList<>();
        ChildNodeIterator it = new ChildNodeIterator(node);
        while (it.hasNext()) {
            Node n = it.next();
            if (n instanceof ASTmailbox) {
                results.add(buildMailbox((ASTmailbox) n));
            } else {
                throw new IllegalStateException();
            }
        }
        return new MailboxList(results, true);
    }

    public Mailbox buildMailbox(ASTmailbox node) {
        Node n = new ChildNodeIterator(node).next();
        if (n instanceof ASTaddr_spec) {
            return buildAddrSpec((ASTaddr_spec) n);
        }
        if (n instanceof ASTangle_addr) {
            return buildAngleAddr((ASTangle_addr) n);
        }
        if (n instanceof ASTname_addr) {
            return buildNameAddr((ASTname_addr) n);
        }
        throw new IllegalStateException();
    }

    private Mailbox buildNameAddr(ASTname_addr node) {
        ChildNodeIterator it = new ChildNodeIterator(node);
        Node n = it.next();
        if (n instanceof ASTphrase) {
            String name = buildString((ASTphrase) n, false);
            Node n2 = it.next();
            if (n2 instanceof ASTangle_addr) {
                return new Mailbox(DecoderUtil.decodeEncodedWords(name), buildAngleAddr((ASTangle_addr) n2));
            }
            throw new IllegalStateException();
        }
        throw new IllegalStateException();
    }

    private Mailbox buildAngleAddr(ASTangle_addr node) {
        ChildNodeIterator it = new ChildNodeIterator(node);
        DomainList route = null;
        Node n = it.next();
        if (n instanceof ASTroute) {
            route = buildRoute((ASTroute) n);
            n = it.next();
        } else if (!(n instanceof ASTaddr_spec)) {
            throw new IllegalStateException();
        }
        if (n instanceof ASTaddr_spec) {
            return buildAddrSpec(route, (ASTaddr_spec) n);
        }
        throw new IllegalStateException();
    }

    private DomainList buildRoute(ASTroute node) {
        List<String> results = new ArrayList<>(node.jjtGetNumChildren());
        ChildNodeIterator it = new ChildNodeIterator(node);
        while (it.hasNext()) {
            Node n = it.next();
            if (n instanceof ASTdomain) {
                results.add(buildString((ASTdomain) n, true));
            } else {
                throw new IllegalStateException();
            }
        }
        return new DomainList(results, true);
    }

    private Mailbox buildAddrSpec(ASTaddr_spec node) {
        return buildAddrSpec((DomainList) null, node);
    }

    private Mailbox buildAddrSpec(DomainList route, ASTaddr_spec node) {
        ChildNodeIterator it = new ChildNodeIterator(node);
        return new Mailbox(route, buildString((ASTlocal_part) it.next(), true), buildString((ASTdomain) it.next(), true));
    }

    private String buildString(SimpleNode node, boolean stripSpaces) {
        Token head = node.firstToken;
        Token tail = node.lastToken;
        StringBuilder out = new StringBuilder();
        while (head != tail) {
            out.append(head.image);
            head = head.next;
            if (!stripSpaces) {
                addSpecials(out, head.specialToken);
            }
        }
        out.append(tail.image);
        return out.toString();
    }

    private void addSpecials(StringBuilder out, Token specialToken) {
        if (specialToken != null) {
            addSpecials(out, specialToken.specialToken);
            out.append(specialToken.image);
        }
    }

    private static class ChildNodeIterator implements Iterator<Node> {
        private int index = 0;
        private int len;
        private SimpleNode simpleNode;

        public ChildNodeIterator(SimpleNode simpleNode2) {
            this.simpleNode = simpleNode2;
            this.len = simpleNode2.jjtGetNumChildren();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return this.index < this.len;
        }

        public Node next() {
            SimpleNode simpleNode2 = this.simpleNode;
            int i = this.index;
            this.index = i + 1;
            return simpleNode2.jjtGetChild(i);
        }
    }
}
