import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import sun.misc.Signal;

public class RedisTest
{    
    private static final Logger logger = Logger.getLogger(RedisTest.class);
    private static String filePath = null;
    
    public static void main(String[] args) {
        String path = RedisTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e) {
            return;
        }
        File file = new File(path);
        filePath = file.getParent() + "/";
        String pid=null;
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(filePath + "config/log4j.properties"));
            String name = ManagementFactory.getRuntimeMXBean().getName();
            pid = name.split("@")[0];
            String logPath = props.getProperty("log4j.appender.file.File")+pid+".log";
            props.setProperty("log4j.appender.file.File", logPath);
            PropertyConfigurator.configure(props);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext ctx = new FileSystemXmlApplicationContext("file:"+ filePath +"applicationContext.xml");
        
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(filePath + "config/context.properties"));

        } catch (Exception e) {
            logger.error("Read server configure info error,error code("+ e.getMessage() +")!");
            return;
        }

    }
    
}
