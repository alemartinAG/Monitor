
import com.errors.OutsideWindowException;
import com.petri.PetriNet;
import com.petri.Time;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PetriNetTest {

    private static final String PETRI = "res-test/petri-test.html";
    private static final String TIMED = "res-test/timed-test.txt";

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

    private final static boolean[] enabled = {true, false, false, true, false, false};
    private final static boolean[] enabled_t1 = {false, true, false, false, false, false};
    private final static boolean[] enabled_t4 = {false, false, false, false, true, false};
    private Time[] timed_t1 = new Time[TRANSITIONS];


    private int[][] INHIBITION;

    @Before
    public void setUp() throws Exception {

        pn = new PetriNet(PETRI, TIMED);
        INHIBITION = new int[PLACES][TRANSITIONS];

        for(int i=0; i<PLACES; i++){
            Arrays.fill(INHIBITION[i], 0);
        }

        Arrays.fill(timed_t1, null);
        timed_t1[0] = new Time(0, 200);
        timed_t1[2] = new Time(0, 200);

    }

    @Test
    public void triggerLogicTest() {

        try {

            timed_t1[0].setNewTimeStamp();
            Assert.assertTrue(pn.trigger(0));

            Assert.assertFalse(pn.trigger(3));

            Assert.assertTrue(pn.trigger(1));
            Assert.assertFalse(pn.trigger(0));

            timed_t1[2].setNewTimeStamp();
            Assert.assertTrue(pn.trigger(2));

            Assert.assertTrue(pn.trigger(3));
            Assert.assertTrue(pn.trigger(4));
            Assert.assertTrue(pn.trigger(5));

        } catch (OutsideWindowException e) {
            e.printStackTrace();
        }


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

    @Test
    public void areEnabledTest() {

        Assert.assertArrayEquals(enabled, pn.areEnabled());
        Assert.assertFalse(Arrays.equals(enabled_t1, pn.areEnabled()));

        for(int i=0; i<enabled.length; i++){
            if(timed_t1[i] != null && enabled[i]){
                timed_t1[i].setNewTimeStamp();
            }
        }

        try{
            pn.trigger(0);
            Assert.assertArrayEquals(enabled_t1, pn.areEnabled());
            pn.trigger(1);
            pn.trigger(2);

            pn.trigger(3);
            Assert.assertArrayEquals(enabled_t4, pn.areEnabled());
        } catch (OutsideWindowException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void setTimedTransitionsTest(){
        Time[] timedTransitions = pn.getTimedTransitions();

        for(int i=0; i<TRANSITIONS; i++){

           if(timed_t1[i] == null && timedTransitions[i] != null){
               Assert.fail();
           }


           if(timed_t1[i] != null) {
               Assert.assertEquals(timed_t1[i].getAlpha(), timedTransitions[i].getAlpha());
               Assert.assertEquals(timed_t1[i].getBeta(), timedTransitions[i].getBeta());
           }
        }
    }

}
