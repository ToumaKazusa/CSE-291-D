package rmi;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public abstract class ServerListener<T> implements Runnable {
	
	private boolean listening = false;
	private InetSocketAddress host;
	private ServerSocket serversocket = null;
	private Socket socket;
	private Class c;
	private T object;
	
	public ServerListener (InetSocketAddress address, Class c, T obj) {
		this.host = address;
		this.c = c;
		this.object = obj;
	}
	
	@SuppressWarnings("rawtypes")
	public void listen() {
		try {
			if (serversocket == null) {
				serversocket = new ServerSocket(host.getPort());
			}
			while (listening) {
				System.out.println("Listening...");
				socket = serversocket.accept();	
				System.out.println("Accept...");
				ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
				
				String methodname = (String) istream.readObject();
				Object[] args = (Object[]) istream.readObject();
				Class[] para = (Class[]) istream.readObject();
				
				Method method = c.getMethod(methodname, para);
				Object result = method.invoke(object, args);
				
				ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
				ostream.writeObject(result);
				ostream.flush();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!listening) {
			System.out.println("Listening");
			listening = true;
			listen();
		}
	}
	
}
