package Neurons;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NeuralNetTest {
	NeuralNet testNeuronNet = new NeuralNet(2,4,1,0.2,0.0,1.0,3.0);
	Neuron testNeuron = new Neuron("test");
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testBinarySigmoid() {
		double x = 1;
		double expectedResult = 0.731058;
		double delta = 0.001;
		double actualResult = testNeuron.unipolarSigmoid(x);
		assertEquals(expectedResult, actualResult, delta);	
	}

	@Test
	public void testBipolarSigmoid() {
		double x = 1;
		double expectedResult = 0.462117;
		double delta = 0.001;
		double actualResult = testNeuron.bipolarSigmoid(x);
		assertEquals(expectedResult, actualResult, delta);
	}
	
	@Test
	public void testBipolarSigmoidDerivative() {
		double x = 0.5;
		double expectedResult = 0.375;
		double delta = 0.001;
		double actualResult = testNeuronNet.bipolarSigmoidDerivative(x);
		assertEquals(expectedResult, actualResult, delta);
		
	}
	@Test
	public void testCustomizedSigmoidDerivative() {
		double x = 0.5;
		double expectedResult = -0.625;
		double delta = 0.001;
		double actualResult = testNeuronNet.customSigmoidDerivative(x);
		assertEquals(expectedResult, actualResult, delta);
		
	}

}
