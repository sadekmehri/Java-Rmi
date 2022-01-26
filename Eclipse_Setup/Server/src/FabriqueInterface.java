import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FabriqueInterface extends Remote {
    public RMIInterface newMultiplication(Matrix matrixA, Matrix matrixB) throws RemoteException, Exception;
}
