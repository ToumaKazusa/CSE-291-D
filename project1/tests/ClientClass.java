package tests;

import rmi.RMIException;

public interface ClientClass {
	abstract String run() throws RMIException;
}
