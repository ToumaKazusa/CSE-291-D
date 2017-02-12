package client;
import rmi.RMIException;

public class PingPongClient {
    public static void main(String[] args) {
    	PingServer server = PingServerFactory.makePingServer();
    	for (int i = 1;i < 5;i ++) {
    		try {
	            System.out.println(server.ping(i));
	        } catch (RMIException e) {
	            System.out.println(e);
	        }
        }
	}
}