package tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import org.junit.Test;

import tests.ClientClass;
import rmi.Skeleton;
import rmi.Stub;

public class ClientTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws NoSuchMethodException, SecurityException, Throwable {
		InetSocketAddress addr = new InetSocketAddress("localhost", 32769);
		ClientClass client = Stub.create(ClientClass.class, addr);
		System.out.println(client.run());
		
//		ClassInvocationHandler handler = new ClassInvocationHandler();
//		handler.host = addr;
//		Class<TestClass> c = TestClass.class;
//		Class[] args = {};
//		
//		TestClass obj = (TestClass) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, handler);
//		System.out.println(obj.run());
	}

}
