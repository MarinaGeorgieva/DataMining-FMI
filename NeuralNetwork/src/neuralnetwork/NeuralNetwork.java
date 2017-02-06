package neuralnetwork;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {
	private static final double MIN_ERROR = 0.001;
	
	private ArrayList<Neuron> inputLayer;
	private ArrayList<Neuron> hiddenLayer;
	private ArrayList<Neuron> outputLayer;
	
	private int[] layers;
	
	private Neuron biasNeuron;
	
	private Random random;
	
	private double[][] inputs;
	private double[] outputs;
	private int epochs;
	private double learningRate;
	private double[] predictedOutputs;
	
	public NeuralNetwork(int[] layers, double[][] inputs, double[] outputs, int epochs, double learningRate) {
		this.layers = layers;
		this.inputs = inputs;
		this.outputs = outputs;
		this.epochs = epochs;
		this.learningRate = learningRate;
		this.predictedOutputs = new double[outputs.length];
		
		inputLayer = new ArrayList<Neuron>();
		hiddenLayer = new ArrayList<Neuron>();
		outputLayer = new ArrayList<Neuron>();
		
		random = new Random();
		
		biasNeuron = new Neuron();
		biasNeuron.setActivation(1);
		addInputLayer();
		addHiddenLayer();
		addOutputLayer();
		initializeRandomWeights();
	}
	
	public void train() {
		for (int i = 0; i < epochs; i++) {
			double totalError = 0;
			for (int j = 0; j < outputs.length; j++) {
				double[] input = inputs[j];
				setInput(input);
				
				calculateActivation();
				
				double predictedOutput = getPredictedOutput();
				double expectedOutput = outputs[j];
				predictedOutputs[j] = predictedOutput;
				
				double error = (expectedOutput - predictedOutput) * (expectedOutput - predictedOutput);
				totalError += error;
				
				applyBackpropagation(expectedOutput);
			}
			
			if (totalError <= MIN_ERROR) {
				System.out.println("Reached min error!");
				
				for (int j = 0; j < predictedOutputs.length; j++) {
					System.out.printf("Predicted output: %f \n", predictedOutputs[j]);
					System.out.printf("Expected output: %f \n", outputs[j]);
				}
				
				break;
			}
		}
	}
	
	private void applyBackpropagation(double expectedOutput) {
		for (Neuron neuron : outputLayer) {
			ArrayList<Connection> connections = neuron.getInConnections();
			for (Connection connection : connections) {
				double ok = neuron.getActivation();
				double tk = expectedOutput;
				double deltak = ok * (1 - ok) * (tk - ok);
				double x = connection.getFromNeuron().getActivation();
				double weight = connection.getWeight() + learningRate * deltak * x;
				connection.setWeight(weight);
			}
		}
		
		int k = 0;
		for (Neuron neuron : hiddenLayer) { 
			ArrayList<Connection> connections = neuron.getInConnections();
			for (Connection connection : connections) {
				double oh = neuron.getActivation();
				double sum = 0;
				for (Neuron outputNeuron : outputLayer) {
					double ok = outputNeuron.getActivation();
					double tk = expectedOutput;
					double deltak = ok * (1 - ok) * (tk - ok);
					double wkh = outputNeuron.getInConnections().get(k).getWeight();
					sum += deltak * wkh;
				}
				double deltah = oh * (1 - oh) * sum;
				double x = connection.getFromNeuron().getActivation();
				double weight = connection.getWeight() + learningRate * deltah * x;
				connection.setWeight(weight);
			}
			
			k++;
		}
	}
	
	private double getPredictedOutput() {
		double predictedOutput = outputLayer.get(0).getActivation();
		return predictedOutput;
	}
	
	private void calculateActivation() {
		for (Neuron neuron : hiddenLayer) {
			neuron.calculateActivation();
		}
		
		for (Neuron neuron : outputLayer) {
			neuron.calculateActivation();
		}
	}
	
	private void setInput(double[] input) {
		for (int i = 0; i < input.length; i++) {
			inputLayer.get(i).setActivation(input[i]);
		}
	}
	
	private void addInputLayer() {
		for (int i = 0; i < layers[0]; i++) {
			Neuron neuron = new Neuron();
			inputLayer.add(neuron);
		}
	}
	
	private void addHiddenLayer() {
		for (int i = 0; i < layers[1]; i++) {
			Neuron neuron = new Neuron();
			neuron.addInConnections(inputLayer);
			neuron.addBiasConnection(biasNeuron);
			hiddenLayer.add(neuron);
		}
	}
	
	private void addOutputLayer() {
		for (int i = 0; i < layers[2]; i++) {
			Neuron neuron = new Neuron();
			neuron.addInConnections(hiddenLayer);
			neuron.addBiasConnection(biasNeuron);
			outputLayer.add(neuron);
		}
	}
	
	private void initializeRandomWeights() {
		for (Neuron neuron : hiddenLayer) {
			ArrayList<Connection> connections = neuron.getInConnections();
			for (int i = 0; i < connections.size(); i++) {
				double weight = 0;
				if (i == connections.size() - 1) {
					weight = 0.5;
				}
				else {
					weight = -0.05 + (0.05 - (-0.05)) * random.nextDouble();
				}
				
				connections.get(i).setWeight(weight);
			}
		}
		
		for (Neuron neuron : outputLayer) {
			ArrayList<Connection> connections = neuron.getInConnections();
			for (int i = 0; i < connections.size(); i++) {
				double weight = 0;
				if (i == connections.size() - 1) {
					weight = 0.5;
				}
				else {
					weight = -0.05 + (0.05 - (-0.05)) * random.nextDouble();
				}
				connections.get(i).setWeight(weight);
			}
		}
	}
}
