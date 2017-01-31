package tests;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.junit.Test;

import rmi.ClassInvocationHandler;
import rmi.ServerListener;

public class ServerTest {

	@Test
	public void test() throws NoSuchMethodException, SecurityException, Throwable {
		InetSocketAddress addr = new InetSocketAddress("localhost", 32769);
		
		ServerListener<TestClass> listener = new ServerListener<TestClass>(addr, TestClass.class, new ServerClass()) {
		};
		listener.run();
		
	}

}
