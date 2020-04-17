package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class Multipart implements Body {
    private List<BodyPart> bodyParts;
    private ByteSequence epilogue;
    private transient String epilogueStrCache;
    private Entity parent;
    private ByteSequence preamble;
    private transient String preambleStrCache;
    private String subType;

    public Multipart(String subType2) {
        this.bodyParts = new LinkedList();
        this.parent = null;
        this.preamble = ByteSequence.EMPTY;
        this.preambleStrCache = "";
        this.epilogue = ByteSequence.EMPTY;
        this.epilogueStrCache = "";
        this.subType = subType2;
    }

    public Multipart(Multipart other) {
        this.bodyParts = new LinkedList();
        this.parent = null;
        this.preamble = other.preamble;
        this.preambleStrCache = other.preambleStrCache;
        this.epilogue = other.epilogue;
        this.epilogueStrCache = other.epilogueStrCache;
        for (BodyPart otherBodyPart : other.bodyParts) {
            addBodyPart(new BodyPart(otherBodyPart));
        }
        this.subType = other.subType;
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String subType2) {
        this.subType = subType2;
    }

    public Entity getParent() {
        return this.parent;
    }

    public void setParent(Entity parent2) {
        this.parent = parent2;
        for (BodyPart bodyPart : this.bodyParts) {
            bodyPart.setParent(parent2);
        }
    }

    public int getCount() {
        return this.bodyParts.size();
    }

    public List<BodyPart> getBodyParts() {
        return Collections.unmodifiableList(this.bodyParts);
    }

    public void setBodyParts(List<BodyPart> bodyParts2) {
        this.bodyParts = bodyParts2;
        for (BodyPart bodyPart : bodyParts2) {
            bodyPart.setParent(this.parent);
        }
    }

    public void addBodyPart(BodyPart bodyPart) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        this.bodyParts.add(bodyPart);
        bodyPart.setParent(this.parent);
    }

    public void addBodyPart(BodyPart bodyPart, int index) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        this.bodyParts.add(index, bodyPart);
        bodyPart.setParent(this.parent);
    }

    public BodyPart removeBodyPart(int index) {
        BodyPart bodyPart = this.bodyParts.remove(index);
        bodyPart.setParent((Entity) null);
        return bodyPart;
    }

    public BodyPart replaceBodyPart(BodyPart bodyPart, int index) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        BodyPart replacedBodyPart = this.bodyParts.set(index, bodyPart);
        if (bodyPart == replacedBodyPart) {
            throw new IllegalArgumentException("Cannot replace body part with itself");
        }
        bodyPart.setParent(this.parent);
        replacedBodyPart.setParent((Entity) null);
        return replacedBodyPart;
    }

    /* access modifiers changed from: package-private */
    public ByteSequence getPreambleRaw() {
        return this.preamble;
    }

    /* access modifiers changed from: package-private */
    public void setPreambleRaw(ByteSequence preamble2) {
        this.preamble = preamble2;
        this.preambleStrCache = null;
    }

    public String getPreamble() {
        if (this.preambleStrCache == null) {
            this.preambleStrCache = ContentUtil.decode(this.preamble);
        }
        return this.preambleStrCache;
    }

    public void setPreamble(String preamble2) {
        this.preamble = ContentUtil.encode(preamble2);
        this.preambleStrCache = preamble2;
    }

    /* access modifiers changed from: package-private */
    public ByteSequence getEpilogueRaw() {
        return this.epilogue;
    }

    /* access modifiers changed from: package-private */
    public void setEpilogueRaw(ByteSequence epilogue2) {
        this.epilogue = epilogue2;
        this.epilogueStrCache = null;
    }

    public String getEpilogue() {
        if (this.epilogueStrCache == null) {
            this.epilogueStrCache = ContentUtil.decode(this.epilogue);
        }
        return this.epilogueStrCache;
    }

    public void setEpilogue(String epilogue2) {
        this.epilogue = ContentUtil.encode(epilogue2);
        this.epilogueStrCache = epilogue2;
    }

    public void dispose() {
        for (BodyPart bodyPart : this.bodyParts) {
            bodyPart.dispose();
        }
    }
}
