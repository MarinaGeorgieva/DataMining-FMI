package knapsackproblem;

import java.util.Scanner;

public class Startup {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter M and N: ");
		String[] knapsackParts = scanner.nextLine().split(" ");
		int M = Integer.parseInt(knapsackParts[0]);
		int N = Integer.parseInt(knapsackParts[1]);
		
		KnapsackProblemSolver solver = new KnapsackProblemSolver(M, N);
		
		for (int i = 0; i < N; i++) {
			String[] itemParts = scanner.nextLine().split(" ");
			int cost = Integer.parseInt(itemParts[0]);
			int weight = Integer.parseInt(itemParts[1]);
			Item item = new Item(cost, weight);
			solver.addItem(item);
		}
		
		solver.solve();
		
		scanner.close();
	}

}
