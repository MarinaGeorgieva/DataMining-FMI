package singlelayerperceptron;

import java.util.Random;

public class Perceptron {
	private int[][] inputs;
	private int[] outputs;
	private double[] weights;
	private double bias;
	private double treshold;
	private double learningRate;
	private int epochs;
	
	private Random random;
	
	public Perceptron(int[][] inputs, int[] outputs, double bias, double learningRate, int epochs) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.weights = new double[inputs[0].length];
		this.bias = bias;
		this.treshold = 1;
		this.learningRate = learningRate;
		this.epochs = epochs;
		
		random = new Random();
		
		initializeRandomWeights();
	}
	
	public void train() {
		for (int i = 0; i < epochs; i++) {
			int totalError = 0;
			for (int j = 0; j < outputs.length; j++) {
				int[] input = inputs[j];
				int predicted = getPredictedOutput(input);
				int error = outputs[j] - predicted;
				
				bias = bias + learningRate * error;
				
				for (int k = 0; k < inputs[0].length; k++) {
					double delta = learningRate * error * inputs[j][k];
					weights[k] += delta;
				}
				
				totalError += error;
			}
			
			if (totalError == 0) {
				break;
			}
		}
	}
	
	public int getPredictedOutput(int[] input) {
		double activation = getActivation(input);
		if (activation >= treshold) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	private double getActivation(int[] input) {
		double sum = bias;
		for (int i = 0; i < input.length; i++) {
			sum += weights[i] * input[i];
		}
		
		return sum;
	}
	
	private void initializeRandomWeights() {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = -0.05 + (0.05 - (-0.05)) * random.nextDouble();
		}
	}
}
