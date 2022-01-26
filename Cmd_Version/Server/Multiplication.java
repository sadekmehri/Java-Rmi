
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Multiplication extends UnicastRemoteObject implements RMIInterface, Serializable {

	private static final long serialVersionUID = 1L;
	private static int nbrInstance = 0;
	private Matrix matrixA;
	private Matrix matrixB;
	private Matrix matrixC;
	
	public Multiplication(Matrix matrixA, Matrix matrixB) throws RemoteException, Exception {
        super();
        
        nbrInstance++;
    	if(nbrInstance > 3) throw new Exception("You are allowed to create only 3 objects !");
    		
    	if(matrixA.getColumns() != matrixB.getRows())
			throw new Exception("You are violationg matrix multiplications rule -> A[n][p] * B[p][m] = C[n][m] -_-  !");
    	
    	this.matrixA = matrixA;
    	this.matrixB = matrixB;
    	this.matrixC = new Matrix(this.matrixA.getRows(), this.matrixB.getColumns());
    }
	
	@Override
	public Matrix multiply(Matrix matrixA, Matrix matrixB) throws RemoteException, Exception {
		MyThread myThread = new MyThread(matrixA, matrixB);
		myThread.start();
		myThread.join();
		matrixC.setMatrix(myThread.getMatrixC());
		return matrixC;
	}
	
    public void finalize() {
    	nbrInstance--;
    	System.gc();
    }
    
	public Matrix getMatrixA() {
		return matrixA;
	}

	public void setMatrixA(Matrix matrixA) {
		this.matrixA = matrixA;
	}

	public Matrix getMatrixB() {
		return matrixB;
	}

	public void setMatrixB(Matrix matrixB) {
		this.matrixB = matrixB;
	}
	
	public Matrix getMatrixC() {
		return matrixC;
	}

	public void setMatrixC(Matrix matrixC) {
		this.matrixC = matrixC;
	}

}
