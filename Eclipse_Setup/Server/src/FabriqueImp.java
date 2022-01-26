import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FabriqueImp extends UnicastRemoteObject implements FabriqueInterface {

	private static final long serialVersionUID = 1L;

	public FabriqueImp() throws RemoteException {
        super();
    }
    
	@Override
	public RMIInterface newMultiplication(Matrix matrixA, Matrix matrixB) throws RemoteException, Exception {
		return new Multiplication(matrixA, matrixB);
	}

}
