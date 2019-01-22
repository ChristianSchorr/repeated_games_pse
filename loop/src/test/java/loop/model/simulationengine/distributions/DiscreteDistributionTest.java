package loop.model.simulationengine.distributions;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains test cases for the functionality provided by the {@code loop.model.simulationengine.distributions} package,
 * namely discrete distributions such as the poisson distribution or the binomial distribution or finite distributions
 * over arbitrary types of objects.
 * 
 * @author Peter Koepernik
 *
 */
public class DiscreteDistributionTest {
    
    private DiscreteDistribution poissonDistribution;
    private DiscreteDistribution binomialDistribution;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Tests the {@link PoissonDistribution} class.
     */
    @Test
    public void testPoisson() {
        //test different values of lambda
        double minLambda = 1;
        double maxLambda = 1000;
        double stepSize = 1;
        for (double lambda = minLambda; lambda <= maxLambda; lambda += stepSize) {
            poissonDistribution = new PoissonDistribution(lambda);
            
            //test support
            testSupport(poissonDistribution);
            
            //test picker
            Picker<Integer> poissonPicker = poissonDistribution.getPicker();
            assertTrue(poissonPicker.pickOne() >= 0);
            int size = 10;
            assertTrue(poissonPicker.pickMany(size).size() == size);
        }
    }
    
    /**
     * Tests the {@link BinomialDistribution} class
     */
    @Test
    public void testBinomial() {
    	//test different values of the probability parameter p
    	int lowerBound = 0; 
    	int upperBound = 100;
    	double minp = 0.0;
    	double maxp = 1.0;
    	double stepSize = 0.01;
    	for (double p = minp; p <= maxp; p += stepSize) {
    		binomialDistribution = new BinomialDistribution(lowerBound, upperBound, p);

    		//test support   		
            testSupport(binomialDistribution);
            
            //test picker
            Picker<Integer> binomialPicker = binomialDistribution.getPicker();
            assertTrue(binomialPicker.pickOne() >= 0);
            int size = 10;
            assertTrue(binomialPicker.pickMany(size).size() == size);
    	}
    }
    
    
    /**
     * Tests whether the support bounds returned by a given discrete distribution
     * are consistent with the returned probabilities.
     * 
     * @param distribution the distribution that shall be tested
     */
    private void testSupport(DiscreteDistribution distribution) {
        //test different values of q
        double stepSize = 0.01;
        
        for (double q = 0.0; q < 1; q += stepSize) {
            int min = distribution.getSupportMin(q);
            int max = distribution.getSupportMax(q);
            double supportProbability = 0.0;
            for (int i = min; i <= max; i++) {
                supportProbability += distribution.getProbability(i);
            }
            assertTrue(supportProbability >= q);
        }
    }
}
