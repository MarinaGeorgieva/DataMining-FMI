package neuralnetwork;

public class Startup {

	public static void main(String[] args) {
		int[] layers = new int[] {2, 2, 1};
		double[][] inputs = new double[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[] outputs = new double[] {0, 1, 1, 0};
		
		NeuralNetwork network = new NeuralNetwork(layers, inputs, outputs, 100000, 0.1);
		network.train();
	}

}
