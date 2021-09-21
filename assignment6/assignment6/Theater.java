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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Theater {
	int rows;
	int spr;
	private String show;
	private Seat ba; //Best available seat
	private Integer ticketno = 1;
	private ArrayList<ArrayList<Integer>> seats;
	private boolean som = false;
	
    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log = new SalesLogs(); // Field in Theater class.

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    // end of class Seat

    /**
     * Represents a paper ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;

        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol + showLine + eol + boxLine + eol + seatLine + eol + clientLine + eol + dashLine;

            return result;
        }
    }
    

    /**
     * SalesLogs are security wrappers around an ArrayList of Seats and one of
     * Tickets that cannot be altered, except for adding to them. getSeatLog returns
     * a copy of the internal ArrayList of Seats. getTicketLog returns a copy of the
     * internal ArrayList of Tickets.
     */
    static class SalesLogs {
        private ArrayList<Seat> seatLog;
        private ArrayList<Ticket> ticketLog;

        private SalesLogs() {
            seatLog = new ArrayList<Seat>();
            ticketLog = new ArrayList<Ticket>();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Seat> getSeatLog() {
            return (ArrayList<Seat>) seatLog.clone();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Ticket> getTicketLog() {
            return (ArrayList<Ticket>) ticketLog.clone();
        }

        public void addSeat(Seat s) { // call when seat is allocated
            seatLog.add(s);
        }

        public void addTicket(Ticket t) { // call when ticket is printed
            ticketLog.add(t);
        }

    } // end of class SeatLog
    public List<Thread> getthreads(Map<String,Integer> office){
    	List<Thread> threads = new ArrayList<Thread>();
    	Iterator<Map.Entry<String,Integer>> it = office.entrySet().iterator();
    	
    	while(it.hasNext()) {
    		Map.Entry<String, Integer> p = it.next();
    		if(p.getKey() !=null) {
    		BoxOffice b = new  BoxOffice(p.getKey(),p.getValue());
    		Thread bthread = new Thread(b);
    		bthread.start();
    		threads.add(bthread);
    		}
    	}
    	return threads;
    	
    }
    public Theater(int numRows, int seatsPerRow, String show) {
        // TODO: Implement this constructor
    	this.rows = numRows;
    	this.spr = seatsPerRow;
    	this.show=show;
    	this.ba = new Seat(0,1); //initiallize it
    	//System.out.println(numRows);
    //	System.out.println(seatsPerRow);
    	seats = new ArrayList<ArrayList<Integer>>();
    	for(int i =0; i < numRows;i++) {
    		seats.add( new ArrayList<Integer>(spr+1));
    		for(int j =0; j < spr+1;j++ ) {
    			seats.get(i).add(0);	//0 is available
    		}
    	}
    	
    //	System.out.println(Arrays.toString(seats.toArray()));
    }
	Object o1 = new Object();
	Object o2 = new Object();
    public class BoxOffice implements Runnable{
    	private String id;
    	private int client;
    	public BoxOffice(String id, int clientn) {
    		this.id = id;
    		this.client = clientn;
    	}
    	public void run() {
    		//System.out.println("Thread started");
    		for(int i = 0; i < client;i++) {
    			int tempid;
    			Seat seat = bestAvailableSeat();
    			synchronized(o1) {
    			if(getTransactionLog().size() == rows*spr) {
    				if(!som) { // som is sold out message
    				System.out.println("Sorry, we are sold out!");
    				som=true;}
    				return;
    			}
    			else {
        			

        			//	System.out.println(ticketno);
        				if(seat==null)return;        				

        				Ticket t = printTicket(this.id,seat, ticketno); 
        				if(t == null ) {
        					//reopen the seat
        					seats.get(seat.rowNum).set(seat.seatNum,0);
        					

        					return; // 
        				}
    					ticketno++;

    			
    			}
    			}
    		}
    		
    	}
    	public String getId() {
    		return this.id;
    	}
    	public String out() {
    		return "Box office " + id + " with "
    				+ client; 
    	}
    }
    

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public synchronized Seat bestAvailableSeat() {
        // TODO: Implement this method
    	/*
    	int row = ba.getRowNum();
    	int seatno = ba.getSeatNum() +1;
    	if(seatno> spr) {
    		row++;
    		seatno = 1;
    	}
    	Seat cur = ba;
    	ba = new Seat(row,seatno);
    	if(cur.getRowNum() >= rows) {
    		return null;
    	}
    	return cur;*/
    	for(int i =0; i < rows;i++) {
    		for(int j =1;j< spr+1;j++) {
    			if(seats.get(i).get(j) == 0) {
    				Seat temp = new Seat(i,j);
    				seats.get(i).set(j,1);
    				return temp;
    				
    			}
    		}
    	}
    	return null;
       
    }

    /**
     * Prints a ticket to the console for the client after they reserve a seat.
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public synchronized Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        // TODO: Implement this method
    	if(boxOfficeId == null|| seat==null) return null;
    	
    	Ticket temp = new Ticket(this.show, boxOfficeId, seat,client );
    	
		System.out.println(temp.toString());
    	log.addTicket(temp);
		log.addSeat(seat);

    	
    	try {
    		Thread.sleep(printDelay);
    	}
    	catch(Exception e) {
    		return temp;
    	}
    	

    	return temp;
    }
    private Comparator<Seat> CompareSeats = (S1,S2)->{
    	return S1.toString().compareTo(S2.toString());
    };

    /**
     * Lists all seats sold for this theater in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        // TODO: Implement this method
    	
    	Collections.sort(log.seatLog,CompareSeats);
    	return log.seatLog;
    }

    /**
     * Lists all tickets sold for this theater in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        // TODO: Implement this method
        return log.getTicketLog();
    }
    //Methods to help with testing/debugging
    /*
    public boolean checkdupclients() {
    	List<Ticket> transactions = getTransactionLog();
    	Set<Integer> c = new HashSet<Integer>();
    	
    	for(int i =0; i < transactions.size();i++) {
    		if(c.contains(transactions.get(i).getClient())){
    			System.out.println(transactions.get(i).getClient());
    			return true;
    		}
    		else {
    			c.add(transactions.get(i).getClient());
    		}
    	}
    	
    	
    	return false;
    }
    public boolean checkdupseats() {
    	List<Seat> s = getSeatLog();
    	Set<String> c = new HashSet<String>();
    	for(int i = 0; i <s.size();i++) {
    		if(c.contains(s.get(i).toString()))
    		{
    			return true;}
    		else {
    			c.add(s.get(i).toString());
    		}
    		}

    	
    	
    	return false;
    }


    */
}
