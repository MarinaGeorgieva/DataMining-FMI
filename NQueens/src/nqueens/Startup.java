package nqueens;

public class Startup {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("Searching for solution...");
		
		NQueensSolver solver = new NQueensSolver(20000);
		solver.solve();
		
		long end = System.currentTimeMillis();
		System.out.printf("Execution time: %d sec", (end - start) / 1000);
	}

}
