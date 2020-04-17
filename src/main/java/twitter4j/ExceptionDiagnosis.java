package twitter4j;

import java.io.Serializable;

final class ExceptionDiagnosis implements Serializable {
    private static final long serialVersionUID = 453958937114285988L;
    String hexString;
    int lineNumberHash;
    int stackLineHash;
    Throwable th;

    ExceptionDiagnosis(Throwable th2) {
        this(th2, new String[0]);
    }

    ExceptionDiagnosis(Throwable th2, String[] inclusionFilter) {
        this.hexString = "";
        this.th = th2;
        StackTraceElement[] stackTrace = th2.getStackTrace();
        this.stackLineHash = 0;
        this.lineNumberHash = 0;
        for (int i = stackTrace.length - 1; i >= 0; i--) {
            StackTraceElement line = stackTrace[i];
            String[] arr$ = inclusionFilter;
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                if (i$ >= len$) {
                    break;
                }
                if (line.getClassName().startsWith(arr$[i$])) {
                    this.stackLineHash = (this.stackLineHash * 31) + line.getClassName().hashCode() + line.getMethodName().hashCode();
                    this.lineNumberHash = (this.lineNumberHash * 31) + line.getLineNumber();
                    break;
                }
                i$++;
            }
        }
        this.hexString += toHexString(this.stackLineHash) + "-" + toHexString(this.lineNumberHash);
        if (th2.getCause() != null) {
            this.hexString += " " + new ExceptionDiagnosis(th2.getCause(), inclusionFilter).asHexString();
        }
    }

    /* access modifiers changed from: package-private */
    public int getStackLineHash() {
        return this.stackLineHash;
    }

    /* access modifiers changed from: package-private */
    public String getStackLineHashAsHex() {
        return toHexString(this.stackLineHash);
    }

    /* access modifiers changed from: package-private */
    public int getLineNumberHash() {
        return this.lineNumberHash;
    }

    /* access modifiers changed from: package-private */
    public String getLineNumberHashAsHex() {
        return toHexString(this.lineNumberHash);
    }

    /* access modifiers changed from: package-private */
    public String asHexString() {
        return this.hexString;
    }

    private String toHexString(int value) {
        String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length() - 8, str.length());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExceptionDiagnosis that = (ExceptionDiagnosis) o;
        if (this.lineNumberHash != that.lineNumberHash) {
            return false;
        }
        if (this.stackLineHash != that.stackLineHash) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.stackLineHash * 31) + this.lineNumberHash;
    }

    public String toString() {
        return "ExceptionDiagnosis{stackLineHash=" + this.stackLineHash + ", lineNumberHash=" + this.lineNumberHash + '}';
    }
}
