package Neurons;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Neuron {
	/*Neuron's identifier*/
	final String id;
	/*This arraylist saves all the income connections that are input to this neuron, which can be iterated through*/
	private ArrayList <NeuronConnection> inputConnections = new ArrayList <NeuronConnection> ();;
	/*This hashmap saves all the income connections for lookup*/ 
	private HashMap<String,NeuronConnection> inputconnectionMap = new HashMap<String,NeuronConnection>();
	
	protected NeuronConnection biasConnection;	
	final double bias = 1;
	public double NeuronOutput = 0;
	//private int inputConnectionCount = 0;
	
	/*Class Constructors*/
	public Neuron(String id) {
		this.id = id;
	}
	
	public Neuron(String id, String ActivationFunctionType, List<Neuron> inputNeurons) {
		this.id = id;
		addInputConnections(inputNeurons);
	}
	
	public Neuron(String id, String ActivationFunctionType, List<Neuron> inputNeurons, Neuron bias) {
		this.id = id;
		addInputConnections(inputNeurons);
		addBiasConnection(bias);
	}
	
	/*Utility Functions*/
	public String getId() {
		return this.id;
	}

	/* Ouput Calculation Functions*/
	public double getOutput() {
		return this.NeuronOutput;
	}
	
	public void setOutput(double output) {
		this.NeuronOutput = output;
	}
	
	public void calculateOutput(String ActivationFunctionType) {
		double weightedSum = inputSummingFunction(this.inputConnections);
		if(ActivationFunctionType == "bipolar") {
			this.NeuronOutput = bipolarSigmoid(weightedSum);
		}else if(ActivationFunctionType == "Unipolar") {
			this.NeuronOutput = unipolarSigmoid(weightedSum);
		}
	}
	
	public void calculateOutput(String ActivationFunctionType, int ArgA, int ArgB) {
		double weightedSum = inputSummingFunction(this.inputConnections);
		if(ActivationFunctionType == "Customized") {
			this.NeuronOutput = customizedSigmoid(weightedSum,ArgA,ArgB);
		}
	}
	
	
	private double inputSummingFunction(ArrayList<NeuronConnection> inputConnections) {
		double weightedSum = 0;
		for(NeuronConnection connection : inputConnections)
		{
			double weight = connection.getWeight();
			double input = connection.getInput();
			weightedSum += weight*input;
		}
		
		//Add bias to the weighted sum
		if (biasConnection != null) {
			weightedSum += (this.biasConnection.getWeight()*this.bias);
		}
		
		return weightedSum;
	}
	
	/* Connection Constructing Functions*/
	/*Add multiple connections*/
	private void addInputConnections(List<Neuron> inputNeurons) {
		for(Neuron neuron : inputNeurons) {
			NeuronConnection connection = new NeuronConnection(neuron,this);//create new connection that connects to this neuron
			inputConnections.add(connection);//Put created connection into the connection array
			inputconnectionMap.put(neuron.getId(), connection);//Put the created connection into the look up map
		}
	}
	/* Add a connection for bias to this neuron, which bias neuron is a fake neuron should always output 1*/
	private void addBiasConnection(Neuron neuron) {
		NeuronConnection connection = new NeuronConnection(neuron,this);
		this.biasConnection = connection;
		inputConnections.add(connection); //Add bias connection to the list for the ease of weight updating, it should always output 0
	}
	
	public NeuronConnection getInputConnection(Neuron neuron) {
		return inputconnectionMap.get(neuron.getId());
	}
	
	public ArrayList<NeuronConnection> getInputConnectionList() {
		return this.inputConnections;
	}
	
	
	/*Mathematics Functions*/
	public double unipolarSigmoid(double weightedSum){
		// compute the output signal of the binary sigmoid function with input x
		return 1/(1 + Math.exp(-weightedSum));
	}
	private double bipolarSigmoid(double weightedSum) {
		return 2/(1 + Math.exp(-weightedSum))-1;
	}
	
	private double customizedSigmoid(double weightedSum,int a, int b){
		return (b-a)/(1+Math.exp(-weightedSum))+a;
	}
	

}
