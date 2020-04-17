package twitter4j.conf;

class PropertyConfigurationFactory implements ConfigurationFactory {
    private static final PropertyConfiguration ROOT_CONFIGURATION = new PropertyConfiguration();

    PropertyConfigurationFactory() {
    }

    public Configuration getInstance() {
        return ROOT_CONFIGURATION;
    }

    public Configuration getInstance(String configTreePath) {
        PropertyConfiguration conf = new PropertyConfiguration(configTreePath);
        conf.dumpConfiguration();
        return conf;
    }

    public void dispose() {
    }
}
