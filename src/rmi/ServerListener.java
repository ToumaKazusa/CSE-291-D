package rmi;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public abstract class ServerListener<T> implements Runnable {
	
	private boolean listening = false;
	private InetSocketAddress host;
	private ServerSocket serversocket = null;
	private Socket socket;
	private Class c;
	private T object;
	private Semaphore permit = new Semaphore(3);
	
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
		try {
			permit.acquire();
			listening = true;
			System.out.println("Listening");
			listen();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void stop() {
		try {
			permit.release();
			listening = false;
			System.out.println("Closing");
			if (serversocket != null) {
				serversocket.close();
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public boolean isRunning() {
		return listening;
	}
	
}
