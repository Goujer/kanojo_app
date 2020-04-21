package twitter4j.conf;

public interface ConfigurationFactory {
    void dispose();

    Configuration getInstance();

    Configuration getInstance(String str);
}
