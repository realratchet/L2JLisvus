package net.sf.l2j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class EnvProperties extends Properties
{
    private static final long serialVersionUID = 1L;
    private String groupName;
    
    public EnvProperties(String pathProperties)
    {
        String fname = Paths.get(pathProperties).getFileName().toString();
        
        if (!fname.endsWith(".properties"))
            throw new Error("Properties must end with .properties");
        
        this.groupName = fname.replace(".properties", "");
        
        try (InputStream is = new FileInputStream(new File(pathProperties)))
        {
            this.load(is);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Error("Failed to Load " + pathProperties + " File.");
        }
    }
    
    public String getEnvVarname(String propName)
    {
        return "props." + groupName + "." + propName;
    }
    
    @Override
    public String getProperty(String propName)
    {
        String envVar = System.getenv(getEnvVarname(propName));
        
        if (envVar != null)
            return envVar;
        
        return super.getProperty(propName);
    }

    @Override
    public String getProperty(String propName, String defaultValue)
    {
        String envVar = System.getenv(getEnvVarname(propName));
        
        if (envVar != null)
            return envVar;
        
        return super.getProperty(propName, defaultValue);
    }
}
