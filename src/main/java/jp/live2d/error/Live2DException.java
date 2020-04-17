package jp.live2d.error;

public class Live2DException extends Exception {
    static final long serialVersionUID = -1;
    String id;
    String live2d_message;

    public Live2DException() {
    }

    public Live2DException(Exception e) {
        super(e);
    }

    public Live2DException(Exception e, String id2, String live2d_message2) {
        super(e);
        this.id = id2;
        this.live2d_message = live2d_message2;
    }

    public String toString() {
        return String.valueOf(super.toString()) + String.format(" id[%s] %s", new Object[]{this.id, this.live2d_message});
    }
}
