package rmi;
import tests.ServerClass;
import tests.ClientClass;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.net.*;

/** RMI stub factory.

    <p>
    RMI stubs hide network communication with the remote server and provide a
    simple object-like interface to their users. This class provides methods for
    creating stub objects dynamically, when given pre-defined interfaces.

    <p>
    The network address of the remote server is set when a stub is created, and
    may not be modified afterwards. Two stubs are equal if they implement the
    same interface and carry the same remote server address - and would
    therefore connect to the same skeleton. Stubs are serializable.
 */
public abstract class Stub
{
    /** Creates a stub, given a skeleton with an assigned adress.

        <p>
        The stub is assigned the address of the skeleton. The skeleton must
        either have been created with a fixed address, or else it must have
        already been started.

        <p>
        This method should be used when the stub is created together with the
        skeleton. The stub may then be transmitted over the network to enable
        communication with the skeleton.

        @param c A <code>Class</code> object representing the interface
                 implemented by the remote object.
        @param skeleton The skeleton whose network address is to be used.
        @return The stub created.
        @throws IllegalStateException If the skeleton has not been assigned an
                                      address by the user and has not yet been
                                      started.
        @throws UnknownHostException When the skeleton address is a wildcard and
                                     a port is assigned, but no address can be
                                     found for the local host.
        @throws NullPointerException If any argument is <code>null</code>.
        @throws Error If <code>c</code> does not represent a remote interface
                      - an interface in which each method is marked as throwing
                      <code>RMIException</code>, or if an object implementing
                      this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, Skeleton<T> skeleton)
        throws UnknownHostException
    {
    	if(c == null){
    		throw new NullPointerException("class is null");
    	}
    	
    	if(skeleton == null){
    		throw new NullPointerException("skeleton is null");
    	}
    	
    	// check if the interface is a remote interface
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            Class[] exceptions = method.getExceptionTypes();
            boolean flag = false;
            for (Class exception : exceptions) {
                if (exception.getName().contains("RMIException")) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new Error("It's a non-remote interface");
            }
        }

        // connect address
        InetSocketAddress skeletonAddress = skeleton.getAddress();

        if (skeletonAddress == null) {
            throw new IllegalStateException("Unknown host exception");
        }

        ClassInvocationHandler handler = new ClassInvocationHandler(skeleton.getAddress(), c);
        T instance = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, handler);

        return instance;
    	
    }

    /** Creates a stub, given a skeleton with an assigned address and a hostname
        which overrides the skeleton's hostname.

        <p>
        The stub is assigned the port of the skeleton and the given hostname.
        The skeleton must either have been started with a fixed port, or else
        it must have been started to receive a system-assigned port, for this
        method to succeed.

        <p>
        This method should be used when the stub is created together with the
        skeleton, but firewalls or private networks prevent the system from
        automatically assigning a valid externally-routable address to the
        skeleton. In this case, the creator of the stub has the option of
        obtaining an externally-routable address by other means, and specifying
        this hostname to this method.

        @param c A <code>Class</code> object representing the interface
                 implemented by the remote object.
        @param skeleton The skeleton whose port is to be used.
        @param hostname The hostname with which the stub will be created.
        @return The stub created.
        @throws IllegalStateException If the skeleton has not been assigned a
                                      port.
        @throws NullPointerException If any argument is <code>null</code>.
        @throws Error If <code>c</code> does not represent a remote interface
                      - an interface in which each method is marked as throwing
                      <code>RMIException</code>, or if an object implementing
                      this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, Skeleton<T> skeleton,
                               String hostname)
    {
    	if(c == null){
    		throw new NullPointerException("class is null");
    	}
    	
    	if(skeleton == null){
    		throw new NullPointerException("skeleton is null");
    	}
    	
    	if(hostname == null){
    		throw new NullPointerException("hostname is null");
    	}
    	
    	// check if the interface is a remote interface
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            Class[] exceptions = method.getExceptionTypes();
            boolean flag = false;
            for (Class exception : exceptions) {
                if (exception.getName().contains("RMIException")) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new Error("It's a non-remote interface");
            }
        }

        // connect address
        InetSocketAddress skeletonAddress = skeleton.getAddress();

        if (skeletonAddress == null) {
            throw new IllegalStateException("Unknown host exception");
        }

        ClassInvocationHandler handler = new ClassInvocationHandler(new InetSocketAddress(hostname, skeletonAddress.getPort()), c);
        T instance = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, handler);

        return instance;
    }

    /** Creates a stub, given the address of a remote server.

        <p>
        This method should be used primarily when bootstrapping RMI. In this
        case, the server is already running on a remote host but there is
        not necessarily a direct way to obtain an associated stub.

        @param c A <code>Class</code> object representing the interface
                 implemented by the remote object.
        @param address The network address of the remote skeleton.
        @return The stub created.
        @throws NullPointerException If any argument is <code>null</code>.
        @throws Error If <code>c</code> does not represent a remote interface
                      - an interface in which each method is marked as throwing
                      <code>RMIException</code>, or if an object implementing
                      this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, InetSocketAddress address)
    {
    	if(c == null){
    		throw new NullPointerException("class is null");
    	}
    	
    	if(address == null){
    		throw new NullPointerException("address is null");
    	}
    	
    	// check if the interface is a remote interface
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            Class[] exceptions = method.getExceptionTypes();
            boolean flag = false;
            for (Class exception : exceptions) {
                if (exception.getName().contains("RMIException")) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new Error("It's a non-remote interface");
            }
        }


        ClassInvocationHandler handler = new ClassInvocationHandler(address, c);
        T instance = (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, handler);

        return instance;
    }
}
