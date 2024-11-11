import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args) {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
    }
}
