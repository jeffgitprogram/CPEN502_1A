package Neurons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Random;

import ClassInterfaces.NeuralNetInterface;

public class NeuralNet implements NeuralNetInterface {
	
	int argNumInputs;
	int argNumHiddens;
	int argNumOutputs;
	double argLearningRate;
	double argMomentumRate;
	double argA;
	double argB;
	/*Keep inputNeuron, hiddenNeuron, outputNeuron separately in arraylists*/
	private ArrayList<Neuron> inputLayerNeurons = new ArrayList<Neuron>();	
	private ArrayList<Neuron> hiddenLayerNeurons = new ArrayList<Neuron>();
	private ArrayList<Neuron> outputLayerNeurons = new ArrayList<Neuron>();
	
	/* Need to keep in mind that, all neurons can connect to the same bias neuron, since the output of bias neuron is not evaluated*/
	private Neuron biasNeuron = new Neuron("bias"); // Neuron id 0 is reserved for bias neuron
	
	private final double inputData[][] = {{0,0},{1,0},{0,1},{1,1}};
	private double EpochOutput[][] = {{-1},{-1}, {-1}, {-1}};//Initial value -1 for each output
	public NeuralNet(
					int numInputs, int numHiddens, 
					int numOutputs, int learningRate, 
					int momentumRate, int a, int b) {
		this.argNumInputs = numInputs;
		this.argNumHiddens = numHiddens;
		this.argNumOutputs = numOutputs;
		this.argLearningRate = learningRate;
		this.argMomentumRate = momentumRate;
		this.argA = a;
		this.argB = b;
		this.setUpNetwork();
		this.initializeWeights();
	}
	public void setUpNetwork() {
		/*Set up Input layer first*/
		for(int i = 0; i < this.argNumInputs;i++) {
			String index = "Input"+Integer.toString(i);
			System.out.println(index);
			Neuron neuron = new Neuron(index);
			inputLayerNeurons.add(neuron);
		}
		
		for(int j = 0; j < this.argNumHiddens;j++) {
			String index = "Hidden"+Integer.toString(j);
			System.out.println(index);
			Neuron neuron = new Neuron(index,"bipolar",inputLayerNeurons,biasNeuron);
			hiddenLayerNeurons.add(neuron);
		}
		
		for(int k = 0; k < this.argNumOutputs;k++) {
			String index = "Output"+Integer.toString(k);
			System.out.println(index);
			Neuron neuron = new Neuron(index,"Customized",hiddenLayerNeurons,biasNeuron);
			outputLayerNeurons.add(neuron);
		}
	}
	/**
	 * This method sets input value in each forwarding.
	 * @param X The input vector. An array of doubles.
	 */
	public void setInputData(double [] inputs) {
		for(int i = 0; i < inputLayerNeurons.size(); i++) {
			inputLayerNeurons.get(i).setOutput(inputs[i]);//Input Layer Neurons only have output values.
		}
	}
	
	/**
	 * Get the output values from all output neurons, only one output in our problem
	 * @return a double output[]
	 */
	public double[] getOutputResults() {
		double [] outputs = new double[outputLayerNeurons.size()];
		for(int i = 0; i < outputLayerNeurons.size(); i++) {
			outputs[i] = outputLayerNeurons.get(i).getOutput();
		}
		return outputs;
	}
	/*
	 * This method calculates the output of the NN based on the input 
	 * vector using forward propagation, calculation is done layer by layer 
	 */
	public void forwardPropagation() {
		for(Neuron hidden: hiddenLayerNeurons) {
			hidden.calculateOutput();
		}
		
		for (Neuron output: outputLayerNeurons) {
			output.calculateOutput(this.argA, this.argB);
		}
	}
	
	public ArrayList<Neuron> getInputNeurons(){
		return this.inputLayerNeurons;
	}
	
	public ArrayList<Neuron> getHiddenNeurons(){
		return this.hiddenLayerNeurons;
	}
	
	public ArrayList<Neuron> getOutputNeurons(){
		return this.outputLayerNeurons;
	}
	/**
	 * 
	 * @return an array of results for each forwarding in a single epoch
	 */
	public double [][] getEpochResults() {
		return this.EpochOutput;
	}
	
	public void setEpochResults(double[][] results){
		for(int i = 0; i < results.length;i++) {
			for(int j = 0; j < results[0].length;j++)
			{
				this.EpochOutput[i][j] = results[i][j];
			}
		}
	}
	//TODO
	private void applyBackpropagation(double expectedOutput[]) {}
	
	@Override
	public double [] outputFor(double[] inputData) {
		setInputData(inputData);
		forwardPropagation();
		double outputs[] = getOutputResults();
		return outputs;
	}

	@Override
	public double train(double[] X, double argValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void save(File argFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(String argFileName) throws IOException {
		// TODO Auto-generated method stub

	}

	/*@Override
	public double sigmoid(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double customSigmoid(double x) {
		// TODO Auto-generated method stub
		return 0;
	}*/

	@Override
	public void initializeWeights() {
		// TODO Auto-generated method stub
		double upperbound = 0.5;
		double lowerbound = -0.5;
		for(Neuron neuron: hiddenLayerNeurons) {
			ArrayList <NeuronConnection> connections = neuron.getInputConnectionList();
			for(NeuronConnection connect: connections) {
				connect.setWeight(getRandom(lowerbound,upperbound));
			}
		}
		for(Neuron neuron: outputLayerNeurons) {
			ArrayList <NeuronConnection> connections = neuron.getInputConnectionList();
			for(NeuronConnection connect: connections) {
				connect.setWeight(getRandom(lowerbound,upperbound));
			}
		}
	}

	@Override
	public void zeroWeights() {
		for(Neuron neuron: hiddenLayerNeurons) {
			ArrayList <NeuronConnection> connections = neuron.getInputConnectionList();
			for(NeuronConnection connect: connections) {
				connect.setWeight(0);
			}
		}
		for(Neuron neuron:outputLayerNeurons) {
			ArrayList <NeuronConnection> connections = neuron.getInputConnectionList();
			for(NeuronConnection connect: connections) {
				connect.setWeight(0);
			}
		}

	}
	
	private double getRandom(double lowerbound, double upperbound) {
		double random = new Random().nextDouble();
		
		double result = lowerbound+(upperbound-lowerbound)*random;
		return result;
	}

}
