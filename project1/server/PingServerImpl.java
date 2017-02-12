package server;

import rmi.RMIException;

public class PingServerImpl implements PingServer {
	
	@Override
	public String ping(int idNumber) throws RMIException {
		return "Pong" + (String)idNumber;
	}
	
}
