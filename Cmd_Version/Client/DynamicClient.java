import java.lang.reflect.Constructor;
import java.rmi.RMISecurityManager;
import java.rmi.server.RMIClassLoader;
import java.util.Properties;

@SuppressWarnings("deprecation")
public class DynamicClient {

    @SuppressWarnings("rawtypes")
	public DynamicClient() throws Exception {
        Properties p = System.getProperties();
        String url = p.getProperty("java.rmi.server.codebase");
        Class<?> ClasseClient = RMIClassLoader.loadClass(url, "Client");
        Constructor[] C = ClasseClient.getConstructors();
        C[0].newInstance();
    }

	@SuppressWarnings("unused")
	public static void main(String[] args) {
    	System.setSecurityManager(new RMISecurityManager());
        try {
        	DynamicClient cl = new DynamicClient();
        } 
		catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
    
}
