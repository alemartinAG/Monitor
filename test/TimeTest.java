import com.petri.Time;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class TimeTest {

    private Time time1;

    @Before
    public void setUp () {
        time1 = new Time(1, 3);
    }

    @Test
    public void testInsideWindow() throws Exception {
        // Verifica que el tiempo actual se encuentre dentro de la ventana de tiempo

        Assert.assertFalse(time1.testTimeWindow());

        // Duermo el hilo por 2 segundos
        Thread.sleep( 2000);
        Assert.assertTrue(time1.testTimeWindow());

        Thread.sleep( 3000);
        Assert.assertFalse(time1.testTimeWindow());
    }

    @Test
    public void testNewTimestamp () throws Exception {
        // Verifica que un nuevo timestamp sea mayor al anterior

        int oldTs = time1.getTimestamp().getNanos();
        Thread.sleep( 1);
        time1.setNewTimeStamp();
        int newTs = time1.getTimestamp().getNanos();

        Assert.assertTrue(newTs > oldTs);
    }

    @Test
    public void sleepTimeInsideWindow () throws Exception{
        /*
        Verifica si el tiempo para dormir el hilo
        permitirá que luego ingrese a al ventana.
        */

        long sleepTime = time1.getSleepTime();
        Thread.sleep(sleepTime);
        Assert.assertTrue(time1.testTimeWindow());
    }


}
