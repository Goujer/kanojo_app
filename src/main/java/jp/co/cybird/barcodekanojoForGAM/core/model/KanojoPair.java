package jp.co.cybird.barcodekanojoForGAM.core.model;

public class KanojoPair implements BarcodeKanojoModel {
    private Kanojo left;
    private Kanojo right;

    public KanojoPair(Kanojo left2, Kanojo right2) {
        this.left = left2;
        this.right = right2;
    }

    public Kanojo getLeft() {
        return this.left;
    }

    public void setLeft(Kanojo left2) {
        this.left = left2;
    }

    public Kanojo getRight() {
        return this.right;
    }

    public void setRight(Kanojo right2) {
        this.right = right2;
    }
}
