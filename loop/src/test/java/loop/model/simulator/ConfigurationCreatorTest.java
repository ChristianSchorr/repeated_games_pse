package loop.model.simulator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import loop.model.Group;
import loop.model.Population;
import loop.model.Segment;

/**
 * This class holds test for the {@link ConfigurationCreator}.
 * 
 * @author Peter Koepernik
 *
 */
public class ConfigurationCreatorTest {
    
    private Population population;
    private Group group1; //2 segments
    private Group group2; //3 segments
    
    @Before
    public void setUp() throws Exception {
        //private Segment seg = new Segment("PoissonDistribution", new ArrayList<Double>(), new ArrayList<String>())
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNoMulti() {
        
        
    }

}
