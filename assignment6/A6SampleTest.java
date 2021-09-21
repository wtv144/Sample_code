package assignment6;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class A6SampleTest {

    private static String show = "A6 Movie";
    private static List<Theater.Ticket> concurrencyTestLog;

    private static void joinAllThreads(List<Thread> threads)
            throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }

    /**
     * Initialize tests for concurrency by simulating BookingClient with
     * 1) BX1: 100 clients
     * 2) BX2: 100 clients
     * 3) Theater: 100 rows, 2 seats per row
     * <p>
     * Stores the transactions into concurrencyTestLog
     *
     * @throws InterruptedException
     */
    //@BeforeClass
    public static void setupBeforeClass() throws InterruptedException {
        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX1", 100);
            put("BX2", 100);
        }};

        Theater t = new Theater(100, 2, show);
        BookingClient bc = new BookingClient(offices, t);
        joinAllThreads(bc.simulate());

        concurrencyTestLog = t.getTransactionLog();
        assertEquals(false, t.checkdupseats());
        assertEquals(false,t.checkdupclients());
        assertEquals(200,concurrencyTestLog.size());
        
    }

    /**
     * Tests that bestAvailableSeat() can calculate seats with two letters (ex: AA)
     * <p>
     * Precondition: 30 seats sold
     * Expected: AE1
     *
     * @throws InterruptedException
     */
    @Test(timeout = 120000)
    public void testBestSeatDouble() throws InterruptedException {
    	System.out.println("BestSeatDouble");
    	long t1 = System.nanoTime();

        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX1", 15);
            put("BX2", 15);
        }};

        Theater t = new Theater(50, 1, show);

        BookingClient bc = new BookingClient(offices, t);
        joinAllThreads(bc.simulate());

        Theater.Seat best = t.bestAvailableSeat();
        long t2 = System.nanoTime();
        System.out.println("Time: "+(t2-t1)/1000000);
        /*
        
        assertNotNull(best);
        assertEquals("AE1", best.toString());
        assertEquals(false, t.checkdupseats());
        assertEquals(false,t.checkdupclients());
        concurrencyTestLog = t.getTransactionLog();
        int bx1 = 0;
        int bx2 = 0;
        for(int i =0; i < concurrencyTestLog.size();i++) {
        	if(concurrencyTestLog.get(i).getBoxOfficeId().equals("BX1"))
        		bx1++;
        	else if(concurrencyTestLog.get(i).getBoxOfficeId().equals("BX2"))
        		bx2++;
        	
        	
        }
        System.out.println(bx1 + " "+bx2);*/
    }

    /**
     * Tests that bestAvailableSeat() can handle an empty theater
     * <p>
     * Precondition: Theater has not sold any seats yet
     * Expected: A1
     */
    @Test(timeout = 120000)
    public void testBestSeatEmpty() {
        Theater t = new Theater(1, 1, show);
        Theater.Seat best = t.bestAvailableSeat();
        assertNotNull(best);
        assertTrue(best.toString().equalsIgnoreCase("A1"));
        assertEquals(false, t.checkdupseats());
        assertEquals(false,t.checkdupclients());
    }
    @Test
    public void testNotEnoughSeats() {
    	System.out.println("Start of not enough seats");
    	Theater t = new Theater(1,1,show);
    	  Map<String, Integer> offices = new HashMap<String, Integer>() {{
              put("BX1", 15);
              put("BX2", 15);
          }};
          BookingClient bc = new BookingClient(offices, t);
          try {
          joinAllThreads(bc.simulate());}
          catch(Exception e) {
        	  
          }
          assertEquals(t.getTransactionLog().size(),1);
          assertEquals(false, t.checkdupseats());
          assertEquals(false,t.checkdupclients());
    }
    @Test
    public void testNotEnoughSeats2() {
    	System.out.println("Start of not enough seats");
    	Theater t = new Theater(5,4,show);
    	  Map<String, Integer> offices = new HashMap<String, Integer>() {{
              put("BX1", 15);
              put("BX2", 15);
          }};
          BookingClient bc = new BookingClient(offices, t);
          try {
          joinAllThreads(bc.simulate());}
          catch(Exception e) {
        	  
          }
          assertEquals(t.getTransactionLog().size(),20);
          assertEquals(false, t.checkdupseats());
          assertEquals(false,t.checkdupclients());
    }
    @Test
    public void exactseats() {
    	System.out.println("Exact seats");
    	Theater t = new Theater(6,4,show);
    	  Map<String, Integer> offices = new HashMap<String, Integer>() {{
              put("BX1", 12);
              put("BX2", 12);
              put(null,20);
          }};
          BookingClient bc = new BookingClient(offices, t);
          try {
              joinAllThreads(bc.simulate());}
              catch(Exception e) {
            	  
              }
          assertEquals(t.getTransactionLog().size(),24);
          assertEquals(false, t.checkdupseats());
          assertEquals(false,t.checkdupclients());
          System.out.println(Arrays.toString(t.getSeatLog().toArray()));
          
    }
 //   @Test
    public void onethread() {
    	System.out.println("OneThread");
    	long t1 = System.nanoTime();

        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX1", 30);
         
        }};

        Theater t = new Theater(50, 1, show);

        BookingClient bc = new BookingClient(offices, t);
        try {
            joinAllThreads(bc.simulate());}
            catch(Exception e) {
          	  
            }

        Theater.Seat best = t.bestAvailableSeat();
        long t2 = System.nanoTime();
        System.out.println("Time: "+ (t2-t1)/1000000);
    }
   
}
