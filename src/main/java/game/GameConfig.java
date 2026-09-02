package game;

import java.util.Properties;

/**
 * Singleton Pattern: A globally unique game configuration manager
 * The same object can be obtained anywhere by calling
 * GameConfig.getInstance()
 */
public class GameConfig {

    // The only exception is that the program is null when it starts.
    private static GameConfig instance = null;

    // Internally holds configuration data
    private final Properties props;

    // The constructor is private and cannot
    // be used to perform "new" operations from outside.
    private GameConfig(Properties props) {
        this.props = props;
    }

    /**
     * Initialization method - Called only once in the main() function
     * If it has already been initialized,
     * it will be ignored (and will not be overwritten)
     */
    public static void init(Properties props) {
        if (instance == null) {
            instance = new GameConfig(props);
        }
    }

    /**
     * Obtain the unique instance
     * If the initialization has not been performed before calling,
     * throw an exception to alert the developer
     */
    public static GameConfig getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "GameConfig has not been initialized. Call init() first."
            );
        }
        return instance;
    }

    // The method for obtaining configuration values is
    // directly passed to Properties
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public Properties getProperties() {
        return props;
    }
}