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
				try {
					socket = serversocket.accept();
				} catch (Exception ex) {
					System.out.println(ex);
					break;
				}
				
				if (socket.isClosed()) {
					continue;
				}
				
				System.out.println("Accept...");
				ObjectInputStream istream = null;
				try{
					istream = new ObjectInputStream(socket.getInputStream());
				}catch(Exception e){
					throw new RMIException(e.getCause());
				}
				String methodname = (String) istream.readObject();
				Object[] args = (Object[]) istream.readObject();
				Class[] para = (Class[]) istream.readObject();
				
				System.out.println("Method name is: " + methodname);
				
				Method method = c.getMethod(methodname, para);
				
				try {
					Object result = method.invoke(object, args);
					ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
					ostream.writeObject(result);
					ostream.flush();
				} catch (InvocationTargetException ite) {
					System.out.println("Catch invoke exception: " + (Exception)ite.getTargetException());
					ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
					ostream.writeObject((Exception)ite.getTargetException());
				} catch (Exception ex) {
					ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
					ostream.writeObject(ex);
				}
				
//				Method method = this.c.getMethod(methodname, para);
//				Object result = null;
//				try{
//					result = method.invoke(object, args);
//				}catch(InvocationTargetException e){
//					return;
//				}catch(IllegalAccessException e){
//					e.printStackTrace();
//				}
//				ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
//				try{
//					ostream.writeObject(result);
//				}catch(IOException e){
//
//				}finally{
//					if(ostream!=null){
//						try{
//							ostream.close();
//						}catch(IOException e){
//
//						}
//					}
//				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("listen exception:" + e);
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
		} catch (InterruptedException ex) {
			System.out.println(ex);
		}
	}
	
	public void stop() {
		try {
			permit.release();
			listening = false;
			System.out.println("Closing");
			if (serversocket != null && !serversocket.isClosed()) {
				serversocket.close();
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	public boolean isRunning() {
		return listening;
	}
	
}
