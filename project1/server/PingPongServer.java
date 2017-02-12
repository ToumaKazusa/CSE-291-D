package server;

import rmi.*;
import client.*;
import java.net.InetSocketAddress;

public class PingPongServer {
	
    public static void main(String[] args) throws RMIException {
        
    	PingServer pingServer = new PingServerImpl();
    	InetSocketAddress address = new InetSocketAddress("localhost", 9898);
        Skeleton<PingServer> skeleton = new Skeleton<PingServer>(PingServer.class, pingServer, address);
        
        skeleton.start();
        
    }
    
}
