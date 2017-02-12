package client;
import rmi.RMIException;

public class PingPongClient {
    public static void main(String[] args) {
    	PingServer server = PingServerFactory.makePingServer();
    	System.out.println(server.ping(1000));
    	System.out.println(server.ping(2000));
    	System.out.println(server.ping(3000));
    }
}