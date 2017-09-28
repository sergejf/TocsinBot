package fr.tocsin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class Properties {

    private HashMap<String, String> configProperties = new HashMap<>();

    private String loadProperty(String p) {
        java.util.Properties prop = new java.util.Properties();
        String fileName = "config.properties";
        try {
            ClassLoader classLoader = Runner.class.getClassLoader();
            URL res = Objects.requireNonNull(classLoader.getResource(fileName), "Can't find config.properties");
            InputStream is = new FileInputStream(res.getFile());
            prop.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(p);
    }

    public String getProperty(String p) {
        String prop;
        if (!configProperties.containsKey(p)) {
            prop = loadProperty(p);
            configProperties.put(p, prop);
        }
        return configProperties.get(p);
    }
}
