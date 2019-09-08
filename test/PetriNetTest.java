
import com.monitor.PetriNet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PetriNetTest {

    private PetriNet pn;
    private final static int TRANSITIONS = 6;
    private final static int PLACES = 7;

    private final static int[][] COMBINED = {
            {-1,0,1,0,0,0}, //P1
            {1,-1,0,0,0,0}, //P2
            {0,1,-1,0,0,0}, //P3
            {0,0,0,-1,0,1}, //P4
            {0,0,0,1,-1,0}, //P5
            {0,0,0,0,1,-1}, //P6
            {-1,1,0,-1,1,0} //P7
    };

    private int[][] INHIBITION;

    @Before
    public void setUp() throws Exception {
        pn = new PetriNet("res/petri-test.html");
        INHIBITION = new int[PLACES][TRANSITIONS];

        for(int i=0; i<PLACES; i++){
            Arrays.fill(INHIBITION[i], 0);
        }

    }

    @Test
    public void triggerLogicTest() {

        Assert.assertTrue(pn.trigger(0));
        Assert.assertFalse(pn.trigger(3));

        Assert.assertTrue(pn.trigger(1));
        Assert.assertFalse(pn.trigger(0));

        Assert.assertTrue(pn.trigger(2));

        Assert.assertTrue(pn.trigger(3));
        Assert.assertTrue(pn.trigger(4));
        Assert.assertTrue(pn.trigger(5));

    }

    @Test
    public void countTest() {

        Assert.assertEquals(TRANSITIONS, pn.getTransitionsCount());
        Assert.assertEquals(PLACES, pn.getPlacesCount());

    }

    @Test
    public void matricesTest(){

        Assert.assertEquals(COMBINED, pn.getMatrix(PetriNet.CIM));
        Assert.assertEquals(INHIBITION, pn.getMatrix(PetriNet.INM));

    }



}
