package config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by zzq on 16/5/29.
 */
public class ConfigCenter {
    private static Config config= ConfigFactory.parseResources("blackhole.properties");
    public static String getString(String key){
        return config.getString(key);
    }
}
