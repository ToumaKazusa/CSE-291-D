//PingServerFactory.java
package client;
import java.net.InetSocketAddress;
import rmi.Stub;


public class PingServerFactory {
    public static PingServer makePingServer() {
        InetSocketAddress address = new InetSocketAddress("localhost", 9898);
        return Stub.create(PingServer.class, address);
    }
}