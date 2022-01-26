import java.io.Serializable;
import java.util.Random;

public class Matrix implements Serializable {

    private static final long serialVersionUID = 1L;
    private int columns;
    private int rows;
    private int matrix[][];

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new int[rows][columns];
    }
    
    private int generateRandomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
  
    public void fill(int min, int max) {	
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                matrix[i][j] = generateRandomInt(min, max);
        }
    }

    public void display() {
    	System.out.println("Display Matrix");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                System.out.print(matrix[i][j] + " ");

            System.out.println();
        }
    }
    
    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
    	this.rows = matrix.length;
    	this.columns = matrix[0].length;
        this.matrix = matrix;
    }
}