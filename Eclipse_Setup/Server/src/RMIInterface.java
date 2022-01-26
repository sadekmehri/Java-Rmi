
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
	public Matrix multiply(Matrix matrixA, Matrix matrixB) throws RemoteException, Exception;
}
