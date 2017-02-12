package rmi;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerThread implements Runnable {
	
	private ObjectOutputStream ostream = null;
    private Object obj;
    private Method method;
    private Object[] args;
	
    public ServerThread(ObjectOutputStream ostream, Object obj, Method method, Object[] args) {
    	this.ostream = ostream;
    	this.obj = obj;
    	this.method = method;
    	this.args = args;
    }
    
    public Object call() throws Exception {
    	try {
            if (this.method != null) {
                Object result = this.method.invoke(obj, args);
                return result;
            }
        } catch (InvocationTargetException e) {
            throw (Exception)e.getTargetException();
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
    
    public void returnresult (Object result) {
    	try {
			ostream.writeObject(result);
		} catch (IOException e) {
			System.out.println("Return result failed!" + e);
		} finally {
			if (ostream != null) {
				try {
					ostream.close();
				} catch (IOException e) {
					System.out.println("Error when closing outputstream!");
				}
			}
		}
    	
    }
    
    @Override
	public void run() {
    	Object result = null;
    	try {
			result = method.invoke(obj, args);
			ostream.writeObject(result);
			ostream.flush();
		} catch (InvocationTargetException ite) {
			System.out.println("Catch invoke exception: " + (Exception)ite.getTargetException());
			result = (Exception)ite.getTargetException();
		} catch (Exception ex) {
			result = ex;
		}
    	returnresult(result);
		// TODO Auto-generated method stub

	}

}
