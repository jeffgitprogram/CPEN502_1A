package Neurons;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	private double inputData[][] = {{0,0},{1,0},{0,1},{1,1}};	
	private double expectedOutput[][] = {{0},{1},{1},{0}};
	private double epochOutput[][] = {{-1},{-1}, {-1}, {-1}};//Initial value -1 for each output
	private ArrayList<Double> errorInEachEpoch = new ArrayList<>();
	
	
	public NeuralNet(
					int numInputs, int numHiddens, 
					int numOutputs, double learningRate, 
					double momentumRate, double a, double b,
					double [][] inputData,
					double [][] expectedOutput
					) {
		this.argNumInputs = numInputs;
		this.argNumHiddens = numHiddens;
		this.argNumOutputs = numOutputs;
		this.argLearningRate = learningRate;
		this.argMomentumRate = momentumRate;
		this.argA = a;
		this.argB = b;
		this.inputData = inputData;
		this.expectedOutput = expectedOutput;
		this.setUpNetwork();
		this.initializeWeights();
	}
	public void setUpNetwork() {
		/*Set up Input layer first*/
		for(int i = 0; i < this.argNumInputs;i++) {
			String index = "Input"+Integer.toString(i);
			//System.out.println(index);
			Neuron neuron = new Neuron(index);
			inputLayerNeurons.add(neuron);
		}
		
		for(int j = 0; j < this.argNumHiddens;j++) {
			String index = "Hidden"+Integer.toString(j);
			//System.out.println(index);
			Neuron neuron = new Neuron(index,"Customized",inputLayerNeurons,biasNeuron);
			hiddenLayerNeurons.add(neuron);
		}
		
		for(int k = 0; k < this.argNumOutputs;k++) {
			String index = "Output"+Integer.toString(k);
			//System.out.println(index);
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
		biasNeuron.setOutput(1.0);
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
	
	public ArrayList <Double> getErrorArray(){
		return errorInEachEpoch;
	}
	
	public void setErrorArray(ArrayList<Double> errors) {
		errorInEachEpoch = errors;
	}
	/*
	 * This method calculates the output of the NN based on the input 
	 * vector using forward propagation, calculation is done layer by layer 
	 */
	public void forwardPropagation() {
		for(Neuron hidden: hiddenLayerNeurons) {
			hidden.calculateOutput(argA,argB);
		}
		
		for (Neuron output: outputLayerNeurons) {
			output.calculateOutput(argA, argB);
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
		return epochOutput;
	}
	
	public void setEpochResults(double[][] results){
		for(int i = 0; i < results.length;i++) {
			for(int j = 0; j < results[i].length;j++)
			{
				epochOutput[i][j] = results[i][j];
			}
		}
	}
	//TODO
	private void applyBackpropagation(double expectedOutput[]) {
		int i = 0;
		for(Neuron output : outputLayerNeurons) {
			double yi = output.getOutput();
			double ci = expectedOutput[i];
			
			ArrayList<NeuronConnection> connections = output.getInputConnectionList();
			for(NeuronConnection link : connections) {
				double xi = link.getInput();
				double error = customSigmoidDerivative(yi)*(ci-yi);
				link.setError(error);
				double deltaWeight = argLearningRate*error*xi + argMomentumRate*link.getDeltaWeight();//current link's deltaweight has not be updated yet, so it is previous delta w
				double newWeight = link.getWeight() + deltaWeight;
				link.setDeltaWeight(deltaWeight);
				link.setWeight(newWeight);			
			}
			i++;
		}
		
		for(Neuron hidden: hiddenLayerNeurons) {
			ArrayList<NeuronConnection> connections = hidden.getInputConnectionList();
			double yi =hidden.getOutput();
			for(NeuronConnection link : connections) {
				double xi = link.getInput();
				double sumWeightedError= 0;
				for(Neuron output: outputLayerNeurons) {
					double wjh = output.getInputConnection(hidden.getId()).getWeight();
					double errorFromAbove = output.getInputConnection(hidden.getId()).getError();
					sumWeightedError = sumWeightedError + wjh *errorFromAbove;
				}
				
				double error = customSigmoidDerivative(yi)*sumWeightedError;
				link.setError(error);
				double deltaWeight = argLearningRate*error*xi + argMomentumRate * link.getDeltaWeight();
				double newWeight = link.getWeight() + deltaWeight;
				link.setDeltaWeight(deltaWeight);
				link.setWeight(newWeight);							
			}
		}		
	}
	
	@Override
	public double [] outputFor(double[] inputData) {
		setInputData(inputData);
		forwardPropagation();
		double outputs[] = getOutputResults();
		return outputs;
	}

	@Override
	/**
	 * This method run train function for the times that equals to the amount of input samples which is one epoch
	 * @return accumulate squared error generated in one epoch.
	 */
	public double train() {
		double totalError = 0;
		for(int p = 0; p < inputData.length; p++) {
			double error = 0;
			double output[] = outputFor(inputData[p]);
			epochOutput[p] = output;
			for (int j = 0; j < expectedOutput[p].length; j++) {
				double deltaErr = Math.pow((output[j]-expectedOutput[p][j]),2);
				error = error + deltaErr;//sum of error for all  output neurons
			}		
			this.applyBackpropagation(expectedOutput[p]);
			totalError = totalError + error;//Accumulated errors in one epoch
		}
		errorInEachEpoch.add(totalError);
		return totalError;
	}
	
		
	public void tryConverge(int maxStep, double minError) {
		int i;
		double error = 1;
		for(i = 0; i < maxStep && error > minError; i++) {
			error = train();
		}
		System.out.println("Sum of squared error in last epoch = " + error);
		System.out.println("Number of epoch: "+ i + "\n");
		if(i == maxStep) {
			System.out.println("Error in training, try again!");
		}
	}
	
	public void printRunResults(ArrayList<Double> errors, String fileName) throws IOException {
		int epoch;
		PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
		printWriter.printf("Epoch Number, Total Squared Error, \n");
		for(epoch = 0; epoch < errors.size(); epoch++) {
			printWriter.printf("%d, %f, \n", epoch, errors.get(epoch));
		}
		printWriter.flush();
		printWriter.close();
	}
	

	@Override
	public void save(File argFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(String argFileName) throws IOException {
		// TODO Auto-generated method stub

	}
	
	public double sigmoidDerivative(double yi) {
		double result = yi*(1 - yi);
		return result;
	}
	
	public double bipolarSigmoidDerivative(double yi) {
		double result = 1.0/2.0 * (1-yi) * (1+yi);
		return result;
	}
	 /**
     * This method implements the first derivative of the customized sigmoid
     * @param x The input
     * @return f'(x) = -(1 / (b - a))(customSigmoid - a)(customSigmoid - b)
     */	
	public double customSigmoidDerivative(double yi) {
		double result = -(1.0/(argB-argA)) * (yi-argA) * (yi-argB);
		return result;
	}

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
	
	public double getRandom(double lowerbound, double upperbound) {
		double random = Math.random();	
		double result = lowerbound+(upperbound-lowerbound)*random;
		return result;
	}
	
	

}
