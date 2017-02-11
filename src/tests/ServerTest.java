package tests;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.net.Socket;
import rmi.Skeleton;

import org.junit.Test;

public class ServerTest {

	@Test
	public void test() throws NoSuchMethodException, SecurityException, Throwable {
		InetSocketAddress addr = new InetSocketAddress("localhost", 32769);
		
		Skeleton<ServerClass> skeleton = new Skeleton<ServerClass>(ServerClass.class, new ServerClass(), addr);
		skeleton.start();
		
	}

}
