package server;

import rmi.RMIException;
import java.lang.*;

public class PingServerImpl implements PingServer {
	
	@Override
	public String ping(int idNumber) throws RMIException {
		return "Pong " + idNumber;
	}
	
}
