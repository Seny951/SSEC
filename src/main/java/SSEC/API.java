package SSEC;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class API {
    static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    static Logger logger = Logger.getLogger(API.class.getName());
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8001;

    public static void main(String[] args) throws IOException {
        logger.log(Level.INFO, " SSEC API Starting...");

        final Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("SSEC.properties"));
        logger.log(Level.INFO, " Version: " + properties.getProperty("version"));

        HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), 0);

        server.createContext("/test", new  HTTPHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        logger.log(Level.INFO, " Server started on port 8001");

        Tests.PostInvalidDateTest();
        Tests.PostValidDataTest();
        Tests.PostEmptyRequestTest();
        Tests.PostInvalidNameTest();
    }
}
