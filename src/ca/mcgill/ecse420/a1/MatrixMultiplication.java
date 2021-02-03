import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 1;
	private static final int MATRIX_SIZE = 1000;

        public static void main(String[] args) {
		
		// Generate two random matrices, same size
		double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
		long startTime = System.currentTimeMillis();
		sequentialMultiplyMatrix(a, b);
		long runTime = System.currentTimeMillis()- startTime;
		System.out.println("Sequential runtime: " + runTime);
		long parStartTime = System.currentTimeMillis();
		parallelMultiplyMatrix(a, b);	
		long parRunTime = System.currentTimeMillis() - parStartTime;
		System.out.println("Parallel runtime: " + parRunTime);

	}
	
	/**
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
		double[][] out = new double[a.length][a[0].length];
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[0].length; j++){
				for(int k = 0; k < a[0].length; k++){
					out[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return out;
	}
	
	/**
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
    public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		double[][] out = new double[a.length][a[0].length];
		try {
			ExecutorService ex = Executors.newFixedThreadPool(NUMBER_THREADS);

			for(int i = 0; i < a.length; i++){
				for(int j = 0; j < a[0].length; j++){
					ex.execute(new multiply(i,j,a,b,out));
				}
			}
			ex.shutdown();
			while(!ex.isTerminated()){}

		} catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	static class multiply implements Runnable {

		private int i;
		private int j;
		private double[][] a;
		private double[][] b;
		private double[][] out;
	
		multiply(final int i, final int j, final double[][] a, final double[][] b, final double[][] out) {
		  this.i = i;
		  this.j = j;
		  this.a = a;
		  this.b = b;
		  this.out = out;
		}
	
		public void run() {
		  for (int k = 0; k < a[0].length; k++) {
			out[i][j] += a[i][k] * b[k][j];
		  }
		}
	  } 
        /**
         * Populates a matrix of given size with randomly generated integers between 0-10.
         * @param numRows number of rows
         * @param numCols number of cols
         * @return matrix
         */
        private static double[][] generateRandomMatrix (int numRows, int numCols) {
             double matrix[][] = new double[numRows][numCols];
        for (int row = 0 ; row < numRows ; row++ ) {
            for (int col = 0 ; col < numCols ; col++ ) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }
	
}
