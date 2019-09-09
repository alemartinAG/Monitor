import com.monitor.PetriNet;
import com.util.ThreadDistribution;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ThreadDistributionTest {

    private static final int N_THREADS = 2;
    private static final int[] T0 = {1,2,3};
    private static final int[] T1 = {4,5,6};

    private ThreadDistribution td;
    private ArrayList<Integer> thread0;
    private ArrayList<Integer> thread1;


    @Before
    public void setUp() throws Exception {
        td = new ThreadDistribution("res/threads-test.txt");

        thread0 = new ArrayList<>();
        thread1 = new ArrayList<>();

        for (int i=0; i<T1.length; i++){
            thread0.add(T0[i]);
            thread1.add(T1[i]);
        }

    }

    @Test
    public void threadDistributionTest(){
        Assert.assertEquals(N_THREADS, td.getNumberOfThreads());
        Assert.assertEquals(thread0, td.getTransitionsOfThread(0));
        Assert.assertEquals(thread1, td.getTransitionsOfThread(1));
    }

}
