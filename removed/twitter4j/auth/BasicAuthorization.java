package twitter4j.auth;

import java.io.Serializable;
import twitter4j.internal.http.BASE64Encoder;
import twitter4j.internal.http.HttpRequest;

public class BasicAuthorization implements Authorization, Serializable {
    private static final long serialVersionUID = -5861104407848415060L;
    private String basic = encodeBasicAuthenticationString();
    private String password;
    private String userId;

    public BasicAuthorization(String userId2, String password2) {
        this.userId = userId2;
        this.password = password2;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }

    private String encodeBasicAuthenticationString() {
        if (this.userId == null || this.password == null) {
            return null;
        }
        return "Basic " + BASE64Encoder.encode((this.userId + ":" + this.password).getBytes());
    }

    public String getAuthorizationHeader(HttpRequest req) {
        return this.basic;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasicAuthorization)) {
            return false;
        }
        return this.basic.equals(((BasicAuthorization) o).basic);
    }

    public int hashCode() {
        return this.basic.hashCode();
    }

    public String toString() {
        return "BasicAuthorization{userId='" + this.userId + '\'' + ", password='**********''" + '}';
    }
}
