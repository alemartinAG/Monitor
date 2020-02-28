import com.monitor.Politica;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PoliticaTest {

    private static final String PATH = "res-test/politica_test.txt";
    private static final int[][] prioridades = {{1,2,3},{4,5,6},{7}};

    //Should return T1
    private static final boolean[] andV_1 = {true, false, false, true, true, true, true};
    //Should return T5
    private static final boolean[] andV_2 = {false, false, false, false, true, false, true};
    //Should return T7
    private static final boolean[] andV_3 = {false, false, false, false, false, false, true};

    private Politica politica;

    @Before
    public void setUp() throws Exception {
         politica = new Politica(PATH);
    }

    @Test
    public void testPriorities(){

        ArrayList<ArrayList<Integer>> priorities = politica.getPriorities();
        for (ArrayList<Integer> p : priorities){
            for (Integer transition : p){
                Assert.assertEquals((int) transition, prioridades[priorities.indexOf(p)][p.indexOf(transition)]);
            }
        }
    }

    @Test
    public void getNextTest(){

        Assert.assertEquals(0, politica.getNext(andV_1));
        Assert.assertEquals(4, politica.getNext(andV_2));
        Assert.assertEquals(6, politica.getNext(andV_3));

    }
}
