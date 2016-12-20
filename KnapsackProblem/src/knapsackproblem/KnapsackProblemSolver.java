package knapsackproblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class KnapsackProblemSolver {
	private static final int GENERATIONS_LIMIT = 10000;
	private static final int POPULATION_SIZE = 100;
	private static final double MUTATION_PROBABILITY = 0.05;
	
	private ArrayList<String> population;
	
	private int capacity;
	private int maxNumberOfItems;	
	private ArrayList<Item> items;
	
	private Random random;
	
	public KnapsackProblemSolver(int M, int N) {	
		capacity = M;
		maxNumberOfItems = N;		
		items = new ArrayList<Item>();
		population = new ArrayList<String>();
		random = new Random();
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public void solve() {
		int generations = 0;
		generatePopulation();
		sortByFitnessFunction();
		while(true) {
			if (generations >= GENERATIONS_LIMIT) {
				if (getBestFitness() == 0) {
					generations = 0;
				}
				else {
					System.out.println(getBestSolution());
					return;
				}
			}
			
			if (generations == 10 || generations == 1000 || generations == 2500 || generations == 5000) {
				if (getBestFitness() == 0) {
					System.out.println("No good solution yet!");
				}
				else {
					System.out.println(getBestSolution());
				}
			}
			
			applySelection();
			applyCrossover();
			sortByFitnessFunction();
			
			generations++;
		}
	}
	
	private int getBestFitness() {
		String bestChromosome = population.get(population.size() - 1);
		int bestFitnessValue = calculateFitnessForChromosome(bestChromosome);
		return bestFitnessValue;
	}
	
	private int getBestSolution() {
		int bestCost = 0;
		String bestChromosome = population.get(population.size() - 1);
		
		for (int i = 0; i < items.size(); i++) {
			char gene = bestChromosome.charAt(i);
			if (gene == '1') {
				bestCost += items.get(i).getCost();
			}
		}
		
		return bestCost;
	}
	
	private int calculateFitnessForChromosome(String chromosome) {
		int totalWeight = 0;
		int totalCost = 0;
		int totalCount = 0;
		int fitnessValue = 0;
		for (int i = 0; i < items.size(); i++) {
			char gene = chromosome.charAt(i);
			
			if (gene == '0') {
				continue;
			}
			
			totalCount++;
			totalWeight += items.get(i).getWeight();
			totalCost += items.get(i).getCost();
		}
		
		if (totalWeight <= capacity && totalCount <= maxNumberOfItems) {
			fitnessValue = totalCost;
		}
		
		return fitnessValue;
	}
	
	private void sortByFitnessFunction() {
		Collections.sort(population, new Comparator<String>() {

			@Override
			public int compare(String firstChromosome, String secondChromosome) {
				Integer firstChromosomeFitnessValue = new Integer(calculateFitnessForChromosome(firstChromosome));
				Integer secondChromosomeFitnessValue = new Integer(calculateFitnessForChromosome(secondChromosome));
				return firstChromosomeFitnessValue.compareTo(secondChromosomeFitnessValue);
			}

		});
	}
	
	private void applySelection() {
		for (int i = 0; i < (int) (0.2 * POPULATION_SIZE); i++) {
			population.remove(i);
		}
	}
	
	private void applyCrossover() {
		int randomIndex = random.nextInt(items.size() - 1) + 1;
		
		while (population.size() < POPULATION_SIZE) {
			String firstParent = selectChromosome();
			String secondParent = selectChromosome();
			
			while(secondParent.equals(firstParent)) {
				secondParent = selectChromosome();
			}
			
			String firstChild = firstParent.substring(0, randomIndex) + secondParent.substring(randomIndex);
			firstChild = mutate(firstChild);
			population.add(firstChild);
			String secondChild = secondParent.substring(0, randomIndex) + firstParent.substring(randomIndex);
			secondChild = mutate(secondChild);
			if (population.size() < POPULATION_SIZE) {
				population.add(secondChild);
			}
		}
	}
	
	private String selectChromosome() {
		return population.get(random.nextInt(population.size())); 
	}
	
	private String mutate(String chromosome) {
		String newChromosome;
		double probability = random.nextDouble();
		
		if (probability <= MUTATION_PROBABILITY) {
			int randomIndex = random.nextInt(items.size());
			char gene = chromosome.charAt(randomIndex);
			char newGene = gene == '0' ? '1' : '0';
			newChromosome = chromosome.substring(0, randomIndex) + newGene + chromosome.substring(randomIndex + 1);
			return newChromosome;
		}
		
		return chromosome;
	}
	
	private void generatePopulation() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			String chromosome = generateChromosome();
			population.add(chromosome);
		}
	}
	
	private String generateChromosome() {
		StringBuilder chromosome = new StringBuilder(items.size());
		
		char gene;
		for (int i = 0; i < items.size(); i++) {
			double r = random.nextDouble();
			gene = r <= 0.5 ? '0' : '1';	
			chromosome.append(gene);
		}
		
		return chromosome.toString();
	}
}
