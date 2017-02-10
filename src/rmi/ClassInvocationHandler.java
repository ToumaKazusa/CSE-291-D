package rmi;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.lang.reflect.*;
import java.util.*;

public class ClassInvocationHandler implements InvocationHandler, Serializable {
	
	private static final long serialVersionUID = 0;
	public InetSocketAddress host;
	public Object object;
	
	public ClassInvocationHandler(InetSocketAddress inetSocketAddress, Object object) {
        this.host = inetSocketAddress;
        this.object = object;
    }
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		boolean isOverride = false;
		if (!isOverride) {
			@SuppressWarnings("resource")
			Socket socket = new Socket(host.getAddress(), host.getPort());
			ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
			ostream.writeObject(method.getName());
			ostream.writeObject(args);
			ostream.writeObject(method.getParameterTypes());
			
			ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
			return istream.readObject();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
