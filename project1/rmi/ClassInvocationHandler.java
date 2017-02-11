package rmi;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

import org.hamcrest.core.IsInstanceOf;

public class ClassInvocationHandler implements InvocationHandler, Serializable {
	
	private static final long serialVersionUID = 0;
	public InetSocketAddress host;
	public Object object;
	
	public ClassInvocationHandler(){
		
	}
	
	public ClassInvocationHandler(InetSocketAddress inetSocketAddress, Object object) {
        this.host = inetSocketAddress;
        this.object = object;
    }
	
    public InetSocketAddress getInetSocketAddress(){
    	return this.host;
    }

    public Object getObject(){
    	return this.object;
    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		boolean isOverride = false;

		try {
            Class class1 = (Class) object;
            Method[] methods = class1.getMethods();

            for (Method m : methods) {
                if (m.getName().equals("equals")) {
                    isOverride = true;
                    break;
                } else if (m.getName().equals("toString")) {
                    isOverride = true;
                    break;
                } else if (m.getName().equals("hashCode")) {
                    isOverride = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		if(!isOverride && method.getName().equals("equals")){
			if(args == null || args.length == 0){// no argument for class to perform "equals"
				return false;
			}

			Class class1 = (Class) object;
			Proxy class2proxy = null;

			try{
				class2proxy = (Proxy) args[0];
			}catch(Exception e){
				return e;
			}

			if(class2proxy == null)	return false;
			ClassInvocationHandler class2Handler = (ClassInvocationHandler)Proxy.getInvocationHandler(class2proxy);
			Class class2 = (Class) class2Handler.getObject();

			if(this.host.equals(class2Handler.getInetSocketAddress())){
				if(class1.equals(class2))	return true;
				else	return false;
			}else{
				return false;
			}
		}else if(!isOverride && method.getName().equals("toString")){
			if (object == null || host == null) {
                return null;
            }else{
                return object.toString() + host.toString();
            }
		}else if(!isOverride && method.getName().equals("hashCode")){
			if (object == null || host == null) {
                return -1;
            } else {
                return (object.toString() + host.toString()).hashCode();
            }
		}

		if (!isOverride) {
			@SuppressWarnings("resource")
			Socket socket = new Socket(host.getAddress(), host.getPort());
			if(!socket.isClosed()){
				try{
					ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
					if(ostream !=null){
						ostream.writeObject(method.getName());
						ostream.writeObject(args);
						ostream.writeObject(method.getParameterTypes());
						ostream.flush();
					}
				}catch(Exception e){
					throw new RMIException("skeleton error");
				}
				ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
				if(istream!=null){
					Object result = istream.readObject();
					if (result IsInstanceOf Exception) {
						System.out.println("Get Returen Execption " + result);
						throw result;
					}
					else {
						return result;
					}
				}
			}
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
