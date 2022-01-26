public class MyThread extends Thread {

	private Matrix matrixA;
	private Matrix matrixB;
	private int [][] matrixC;
	
	public MyThread(Matrix matrixA, Matrix matrixB) throws Exception  {
		super();
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.matrixC = new int[this.matrixA.getRows()][this.matrixB.getColumns()];
	}
	
	public synchronized void multiply() {
	    for (int i = 0; i < matrixA.getRows(); i++) {
	        for (int j = 0; j < matrixB.getColumns(); j++) {
	        	 matrixC[i][j] = 0;
	            for (int k = 0; k < matrixB.getRows(); k++)
	            	matrixC[i][j] += matrixA.getMatrix()[i][k] * matrixB.getMatrix()[k][j];
	        }
	    }
	}
	
	public int[][] getMatrixC() {
		return matrixC;
	}
	
    public void run() {
       this.multiply();    
    } 
    
}
