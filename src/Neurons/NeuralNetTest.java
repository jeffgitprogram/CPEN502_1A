package Neurons;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

public class NeuralNetTest {
	private double u_inputData[][] = {{0,0},{1,0},{0,1},{1,1}};	
	private double u_expectedOutput[][] = {{0},{1},{1},{0}};
	private int numInput = 2;
	private int numHidden = 4;
	private int numOutput = 1;
	private double learningRate = 0.2;
	private double momentumRate_1 = 0.0;
	private double u_lowerBound = 0.0;
	private double u_upperBound = 1.0;
	private double b_inputData[][] = {{-1,-1},{1,-1},{-1,1},{1,1}};	
	private double b_expectedOutput[][] = {{-1},{1},{1},{-1}};
	private double momentumRate_2 = 0.9;
	private double b_lowerBound = -1.0;
	private double b_upperBound = 1.0;
	
	Neuron testNeuron = new Neuron("test");
	@Before
	public void setUp() throws Exception {

	}
	@Ignore("Ignored")
	@Test
	public void testBinarySigmoid() {
		double x = 1;
		double expectedResult = 0.731058;
		double delta = 0.001;
		double actualResult = testNeuron.unipolarSigmoid(x);
		assertEquals(expectedResult, actualResult, delta);	
	}
	@Ignore("Ignored")
	@Test
	public void testBipolarSigmoid() {
		double x = 1;
		double expectedResult = 0.462117;
		double delta = 0.001;
		double actualResult = testNeuron.bipolarSigmoid(x);
		assertEquals(expectedResult, actualResult, delta);
	}
	@Ignore("Ignored")
	@Test
	public void testBipolarSigmoidDerivative() {
		NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate_1,b_lowerBound,b_upperBound,b_inputData,b_expectedOutput);

		double x = 0.5;
		double expectedResult = 0.375;
		double delta = 0.001;
		double actualResult = testNeuronNet.bipolarSigmoidDerivative(x);
		assertEquals(expectedResult, actualResult, delta);
		
	}
	@Ignore("Ignored")
	@Test
	public void testCustomizedSigmoidDerivative() {
		NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate_1,u_lowerBound,u_upperBound,u_inputData,u_expectedOutput);
		double x = 0.5;
		double expectedResult = 0.25;
		double delta = 0.001;
		double actualResult = testNeuronNet.customSigmoidDerivative(x);
		assertEquals(expectedResult, actualResult, delta);
		
	}
	@Ignore("Ignored")
	@Test
	public void testRandomWeightGenerator() {
		NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate_1,u_lowerBound,u_upperBound,u_inputData,u_expectedOutput);
		double actualResult = testNeuronNet.getRandom(-0.5, 0.5);
		System.out.println(actualResult);
		assertTrue("The output is out of range:"+actualResult, -0.5<=actualResult&&actualResult<=0.5);		
	}
	@Ignore("Ignored")
	@Test
	public void testUnipolarConverge() {
		//testNeuronNet.zeroWeights();
		NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate_1,u_lowerBound,u_upperBound,u_inputData,u_expectedOutput);
		try {
		testNeuronNet.tryConverge(10000, 0.05);
		testNeuronNet.printRunResults(testNeuronNet.getErrorArray(), "unipolar.csv");
		}
		catch(IOException e){
			System.out.println(e);
		}
		
	}
	@Ignore("Ignored")
	@Test
	public void testBipolarConverge() {
		//testNeuronNet.zeroWeights();
		NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate_1,b_lowerBound,b_upperBound,b_inputData,b_expectedOutput);
		try {
		testNeuronNet.tryConverge(10000, 0.05);
		testNeuronNet.printRunResults(testNeuronNet.getErrorArray(), "bipolar.csv");
		}
		catch(IOException e){
			System.out.println(e);
		}		
	}
	@Ignore("Ignored")
	@Test
	public void testBipolarWithMomentumConverge() {
		//testNeuronNet.zeroWeights();
		NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate_2,b_lowerBound,b_upperBound,b_inputData,b_expectedOutput);
		try {
		testNeuronNet.tryConverge(10000, 0.05);
		testNeuronNet.printRunResults(testNeuronNet.getErrorArray(), "bipolarMomentum.csv");
		}
		catch(IOException e){
			System.out.println(e);
		}		
	}
	@Ignore("Ignored")
	@Test
	public void testUnipolarAverage(){
		int average = EpochAverage(momentumRate_1,u_lowerBound,u_upperBound,u_inputData,u_expectedOutput,0.05,10000,500);
		System.out.println("The average of number of epoches to converge is: "+average+"\n");
	}
	
	//@Ignore("Ignored")
	@Test
	public void testBipolarAverage(){
		int average = EpochAverage(momentumRate_1,b_lowerBound,b_upperBound,b_inputData,b_expectedOutput,0.05,10000,500);
		System.out.println("The average of number of epoches to converge is: "+average+"\n");
	}
	@Ignore("Ignored")
	@Test
	public void testBipolarWithMomentumAverage(){
		int average = EpochAverage(momentumRate_2,b_lowerBound,b_upperBound,b_inputData,b_expectedOutput,0.05,10000,1000);
		System.out.println("The average of number of epoches to converge is: "+average+"\n");
	}
	
	
	public int EpochAverage(double momentum, double lowerbound, double upperbound, double[][] input, double[][] expected,double minError, int maxSteps, int numTrials) {
		int epochNumber, failure,success;
		double average = 0f;
		epochNumber = 0;
		failure = 0;
		success = 0;
		for(int i = 0; i < numTrials; i++) {
			NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentum,lowerbound,upperbound,input,expected);
			testNeuronNet.tryConverge(maxSteps, minError);
			epochNumber = testNeuronNet.getErrorArray().size();
			if( epochNumber < maxSteps) {
				average = average +  testNeuronNet.getErrorArray().size();
				success ++; 
			}
			else {
				failure++;
			}			
		}
		average = average/success;
		return (int)average;		
	}	

}
