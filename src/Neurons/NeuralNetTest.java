package Neurons;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

public class NeuralNetTest {
	private double inputData[][] = {{0,0},{1,0},{0,1},{1,1}};	
	private double expectedOutput[][] = {{0},{1},{1},{0}};
	private int numInput = 2;
	private int numHidden = 4;
	private int numOutput = 1;
	private double learningRate = 0.2;
	private double momentumRate = 0.0;
	private double lowerBound = 0.0;
	private double upperBound = 1.0;
	
	NeuralNet testNeuronNet = new NeuralNet(numInput,numHidden,numOutput,learningRate,momentumRate,lowerBound,upperBound,inputData,expectedOutput);
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
		double x = 0.5;
		double expectedResult = 0.375;
		double delta = 0.001;
		double actualResult = testNeuronNet.bipolarSigmoidDerivative(x);
		assertEquals(expectedResult, actualResult, delta);
		
	}
	@Ignore("Ignored")
	@Test
	public void testCustomizedSigmoidDerivative() {
		double x = 0.5;
		double expectedResult = -0.625;
		double delta = 0.001;
		double actualResult = testNeuronNet.customSigmoidDerivative(x);
		assertEquals(expectedResult, actualResult, delta);
		
	}
	@Test
	public void testRandomWeightGenerator() {
		
		double actualResult = testNeuronNet.getRandom(-0.5, 0.5);
		System.out.println(actualResult);
		assertTrue("The output is out of range:"+actualResult, -0.5<=actualResult&&actualResult<=0.5);
		
	}
	
	public void runMultipleEpoches(NeuralNet theNet, double min) {
		
	}

}
