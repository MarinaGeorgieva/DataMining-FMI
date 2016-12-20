package nqueens;

import java.util.ArrayList;
import java.util.Random;

public class NQueensSolver {
	private static final char QUEEN = '*';
	private static final char EMPTY_CELL = '-';
	
	private int size;
	private int[] board;
	private int maxSteps;
	
	private int[] rowsConflicts;
	private int[] rightDiagonalsConflicts;
	private int[] leftDiagonalsConflicts;
	
	private Random random;
	
	private ArrayList<Integer> candidates;
	
	public NQueensSolver(int size) {
		this.size = size;
		board = new int[size];
		maxSteps = 2 * size;
		random = new Random();
		candidates = new ArrayList<Integer>();
		
		initializeRandomBoard();
	}
	
	public void solve() {
		int steps = 0;
		while(true) {
			calculateConflicts();			
			findMaxConflictsQueens();
			
			if (candidates.isEmpty()) {
				System.out.println("Found solution!");
				printBoard();
				return;
			}
			
			int col = candidates.get(random.nextInt(candidates.size()));
			findMinConflictsRows(col);
			
			if (!candidates.isEmpty()) {
				int row = candidates.get(random.nextInt(candidates.size()));
				board[col] = row;
			}
			
			steps++;
			if (steps > maxSteps) {
				initializeRandomBoard();
				steps = 0;
			}
		}
	}
	
	private void printBoard() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (board[col] == row) {
					System.out.print(QUEEN);
				}
				else {
					System.out.print(EMPTY_CELL);
				}
			}
			
			System.out.println();
		}
	}
	
	private void findMinConflictsRows(int col) {
		int minConflicts = getConflictsForQueen(board[col], col) - 3;
		candidates.clear();
		for (int row = 0; row < size; row++) {
			if (row == board[col]) {
				continue;
			}
			
			int conflicts = getConflictsForQueen(row, col);
			if (conflicts < minConflicts) {
				minConflicts = conflicts;
				candidates.clear();
				candidates.add(row);
			}
			else if (conflicts == minConflicts) {
				candidates.add(row);
			}
		}
	}
	
	private void findMaxConflictsQueens() {
		int maxConflicts = 1;
		candidates.clear();
		for (int col = 0; col < size; col++) {
			int conflicts = getConflictsForQueen(board[col], col) - 3;
			if (conflicts > maxConflicts) {
				maxConflicts = conflicts;
				candidates.clear();
				candidates.add(col);
			}
			else if (conflicts == maxConflicts) {
				candidates.add(col);
			}
		}
	}
	
	private void calculateConflicts() {
		rowsConflicts = new int[size];
		rightDiagonalsConflicts = new int[2 * size - 1];
		leftDiagonalsConflicts = new int[2 * size - 1];
		
		for (int col = 0; col < size; col++) {
			int row = board[col];
			rowsConflicts[row]++;
			rightDiagonalsConflicts[size - 1 + col - row]++;
			leftDiagonalsConflicts[col + row]++;
		}
	}
	
	private int getConflictsForQueen(int row, int col) {
		return rowsConflicts[row] + rightDiagonalsConflicts[size - 1 + col - row] + leftDiagonalsConflicts[col + row];
	}
	
	private void initializeRandomBoard() {
		for (int i = 0; i < size; i++) {
			board[i] = i;
		}
		
		for (int i = 0; i < size; i++) {
			int j = random.nextInt(size);
			int tempValue = board[i];
			board[i] = board[j];
			board[j] = tempValue;
		}
	}
}
