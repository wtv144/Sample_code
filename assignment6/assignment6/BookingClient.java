/* MULTITHREADING <Theater.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Warren Vu
 * wtv72
 * 16160
 * Slip days used: 1
 * Fall 2020
 */
package assignment6;

import java.util.Map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.Thread;

public class BookingClient {
	Theater theater;
	Map<String,Integer> office;
    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {
        // TODO: Implement this constructor
    	this.theater = theater;
    	this.office = office;
    	//System.out.println("BC made");
    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        // TODO: Implement this method
    	/*
    	System.out.println("Simulate started");
    	List<Thread> threads = new ArrayList<Thread>();
    	Iterator<Map.Entry<String,Integer>> it = office.entrySet().iterator();
    	
    	while(it.hasNext()) {
    		Map.Entry<String, Integer> p = it.next();
 
    		BoxOffice b = new  BoxOffice(p.getKey(),p.getValue());
    		Thread bthread = new Thread(b);
    		bthread.start();
    		threads.add(bthread);
    	}
    	return threads;*/
    	return theater.getthreads(office);
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
    
        String show = "A6 Movie";
        Map<String, Integer> offices = new HashMap<String, Integer>() {{
            put("BX1", 15);
            put("BX2", 15);
        }};

        Theater t = new Theater(50, 1, show);
        BookingClient bc = new BookingClient(offices, t);
        List<Thread> bo = bc.simulate();
        for(Thread th: bo) {
        	try{th.join();}
        	catch(Exception e) {
        		e.printStackTrace();
        		throw new RuntimeException(e);
        	}
        }

        Theater.Seat best = t.bestAvailableSeat();
        System.out.println(best.toString());
    }
    }

