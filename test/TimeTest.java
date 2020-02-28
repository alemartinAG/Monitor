import com.petri.Time;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeTest {

    private Time time1;
    private int alpha = 2;
    private int beta = 5;

    @Before
    public void setUp () {
        time1 = new Time(alpha, beta);
    }

    @Test
    public void testWindow() throws Exception {

        //Simulo el sensibilizado
        time1.setNewTimeStamp();

        Assert.assertFalse(time1.testTimeWindow());

        // Duermo el hilo para que se encuentre dentro de la ventana
        Thread.sleep( alpha*Time.RATIO);
        Assert.assertTrue(time1.testTimeWindow());

        Thread.sleep(beta*Time.RATIO);
        Assert.assertFalse(time1.testTimeWindow());
    }

    @Test
    public void testNewTimestamp () throws Exception {
        // Verifica que un nuevo timestamp sea mayor al anterior

        time1.setNewTimeStamp();
        long oldTs = time1.getTimestamp().getTime();
        Thread.sleep( alpha*Time.RATIO);
        time1.setNewTimeStamp();
        long newTs = time1.getTimestamp().getTime();

        Assert.assertTrue(newTs > oldTs);
    }

    @Test
    public void sleepTimeInsideWindow () throws Exception{

        /*
            Verifica si el tiempo para dormir el hilo
            permitirá que luego ingrese a al ventana.
        */

        //simulo la sensibilizacion
        time1.setNewTimeStamp();

        long sleepTime = time1.getSleepTime();
        Thread.sleep(sleepTime);
        Assert.assertTrue(time1.testTimeWindow());
    }


}
