package ch.supsi.os.frontend.model;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class PropertiesModel implements PropertiesHandler {

    private static PropertiesModel instance = null;
    private String version;
    private String description;
    private List<Character> symbols;
    private PropertiesModel() {
        final Properties properties = new Properties();

        try{
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
            version = properties.getProperty("version");
            description = properties.getProperty("description");
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static PropertiesModel getInstance() {
        if (instance == null)
            instance = new PropertiesModel();
        return instance;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public List<Character> getSymbols() {
        return symbols;
    }

    @Override
    public boolean isInitialized() {
        return instance != null;
    }
}
