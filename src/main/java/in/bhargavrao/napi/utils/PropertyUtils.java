package in.bhargavrao.napi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by bhargav.h on 10-Mar-17.
 */
public class PropertyUtils {

    public static String getProperty(String propName){
        Properties prop = new Properties();
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream(FilePathUtils.propertiesFile);
            prop.load(input);
            return prop.getProperty(propName);
        }
        catch (IOException e){
            return "Error 2: Properties file not found";
        }
    }
}
