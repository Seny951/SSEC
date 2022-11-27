package SSEC;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Authentication:
* For Authentication I would opt for a TLS approach.
*   Client Certificates could be generated using user credentials or a license key linked with a user account and
* valid serial / device ids in order to ensure the certificate is unique to a user account or specific device
* and cannot be transferred to be used elsewhere.
*
*   A client would initially request a key via an unsecure connection (i.e. an http interface),
* providing information about the user account and/or device
* which would be used by the server to generate a certificate unique to that user or device.
*
*   Once a certificate is obtained further api requests will be made on a secure connection (i.e. HTTPS) that
* validates certificates on each request via a TLS handshake and certification transfer.
*
*   An added benefit of TLS is that as well as the caller being validated the data transfer is also encrypted
 */


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
