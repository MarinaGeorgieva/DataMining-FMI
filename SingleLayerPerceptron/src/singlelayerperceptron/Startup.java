package singlelayerperceptron;

public class Startup {

	public static void main(String[] args) {
		int[][] inputs = new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double bias = 0.5;
		double learningRate = 0.1;
		int epochs = 100;
		
		// AND
		int[] outputs = new int[] {0, 0, 0, 1};
		
		System.out.println("Training perceptron for logical AND...");
		
		Perceptron perceptronA = new Perceptron(inputs, outputs, bias, learningRate, epochs);
		perceptronA.train();
		
		for (int i = 0; i < inputs.length; i++) {
			System.out.println(perceptronA.getPredictedOutput(inputs[i]));
		}
		
		System.out.println(new String(new char[40]).replace('\0', '*'));
		
		// OR
		outputs = new int[] {0, 1, 1, 1};
		
		System.out.println("Training perceptron for logical OR...");
		
		Perceptron perceptronO = new Perceptron(inputs, outputs, bias, learningRate, epochs);
		perceptronO.train();
		
		for (int i = 0; i < inputs.length; i++) {
			System.out.println(perceptronO.getPredictedOutput(inputs[i]));
		}
	}

}
