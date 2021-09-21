package assignment6;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class Atest {
    private static String show1 = "Andy Movie";
    private static String show2 = "Andy Movie 2";
    private static List<Theater.Ticket> concurrencyTestLog;

    private static void joinAllThreads(List<Thread> threads)
            throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }

    @Test(timeout = 120000)
    public void testMultipleTheatres() throws InterruptedException {
        System.out.println("Multiple Theatres");
        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX1", 15);
            put("BX2", 15);
        }};

        Theater t1 = new Theater(10, 1, show1);
        Theater t2 = new Theater(1, 100, show2);
        BookingClient bc1 = new BookingClient(offices, t1);
        BookingClient bc2 = new BookingClient(offices, t2);

        List<Thread> threads1 = bc1.simulate();
        List<Thread> threads2 = bc2.simulate();
        joinAllThreads(threads1);
        joinAllThreads(threads2);

        assertEquals(false, t1.checkdupseats());
        assertEquals(false,t1.checkdupclients());
        assertEquals(false, t2.checkdupseats());
        assertEquals(false,t2.checkdupclients());

        assertEquals(10, t1.getSeatLog().size());
        assertEquals(10, t1.getTransactionLog().size());
        assertEquals(30, t2.getSeatLog().size());
        assertEquals(30, t2.getTransactionLog().size());
    }

    @Test(timeout = 120000)
    public void testLong() throws InterruptedException {
        System.out.println("Long test");
        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX1", 100);
            put("BX2", 500);
            put("BX3", 100);
            put("BX4", 500);
        }};

        Theater t1 = new Theater(24, 50, show1);
        BookingClient bc1 = new BookingClient(offices, t1);

        List<Thread> threads1 = bc1.simulate();
        joinAllThreads(threads1);

        assertEquals(false, t1.checkdupseats());
        assertEquals(false,t1.checkdupclients());

        assertEquals(1200, t1.getSeatLog().size());
        assertEquals(1200, t1.getTransactionLog().size());
    }

    @Test(timeout = 120000)
    public void testInvalid() throws InterruptedException {
        System.out.println("Invalid test");
        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put(null, 50000);
            put("BX2", 4);
            put("BX3", 10);
        }};

        Theater t1 = new Theater(24, 50, show1);
        BookingClient bc1 = new BookingClient(offices, t1);

        List<Thread> threads1 = bc1.simulate();
        joinAllThreads(threads1);

        assertEquals(false, t1.checkdupseats());
        assertEquals(false,t1.checkdupclients());

        assertEquals(14, t1.getSeatLog().size());
        assertEquals(14, t1.getTransactionLog().size());
    }

    @Test(timeout = 120000)
    public void testOrder() throws InterruptedException {
        System.out.println("Order test");
        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX2", 4);
            put("BX3", 10);
        }};

        Theater t1 = new Theater(10, 14, show1);
        BookingClient bc1 = new BookingClient(offices, t1);

        List<Thread> threads1 = bc1.simulate();
        joinAllThreads(threads1);

        assertEquals(false, t1.checkdupseats());
        assertEquals(false,t1.checkdupclients());

        assertEquals(14, t1.getSeatLog().size());
        assertEquals(14, t1.getTransactionLog().size());

        for(int i=0; i<t1.getTransactionLog().size(); i++){
            assertTrue(t1.getTransactionLog().get(i).getSeat().toString().compareTo("B1") < 0);
        }


        t1 = new Theater(14, 1, show1);
        bc1 = new BookingClient(offices, t1);

        threads1 = bc1.simulate();
        joinAllThreads(threads1);

        assertEquals(false, t1.checkdupseats());
        assertEquals(false,t1.checkdupclients());

        assertEquals(14, t1.getSeatLog().size());
        assertEquals(14, t1.getTransactionLog().size());

        for(int i=0; i<t1.getTransactionLog().size(); i++){
            assertTrue(t1.getTransactionLog().get(i).getSeat().getRowNum() < 14);
            assertTrue(t1.getTransactionLog().get(i).getSeat().getSeatNum() == 1);
        }
    }
}
