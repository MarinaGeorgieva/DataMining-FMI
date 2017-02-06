package neuralnetwork;

import java.util.ArrayList;

public class Neuron {
	private ArrayList<Connection> inConnections;
	private Connection bias;
	private double activation;
	
	public Neuron() {
		inConnections = new ArrayList<Connection>();
	}

	public double getActivation() {
		return activation;
	}

	public void setActivation(double activation) {
		this.activation = activation;
	}
	
	public Connection getBiasConnection() {
		return bias;
	}

	public ArrayList<Connection> getInConnections() {
		return inConnections;
	}
	
	public void addInConnections(ArrayList<Neuron> inLayer) {
		for(Neuron neuron: inLayer){
            Connection connection = new Connection(neuron, this);
            inConnections.add(connection);
        }
	}
	
	public void addBiasConnection(Neuron biasNeuron) {
		Connection connection = new Connection(biasNeuron, this);
		bias = connection;
		inConnections.add(bias);
	}
	
	public void calculateActivation() {
		double sum = bias.getWeight();
		for (Connection connection : inConnections) {
			Neuron fromNeuron = connection.getFromNeuron();
			double weight = connection.getWeight();
			double x = fromNeuron.getActivation();			
			sum += weight * x;
		}
		
		activation = g(sum);
	}
	
	private double sigmoid(double s) {
		return 1 / (1 + Math.exp(-s));
	}
	
	private double g(double s) {
		return sigmoid(s);
	}	
}
