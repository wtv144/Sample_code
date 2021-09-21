/*
 * CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Ziyan Prasla
 * zzp64
 * 16185
 * Warren Vu
 * wtv72
 * 16185
 * Slip days used: <0>
 * Fall 2020
 */
package assignment4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main {

    /* Scanner connected to keyboard input, or input file */
    static Scanner kb;

    /* Input file, used instead of keyboard input if specified */
    private static String inputFile;

    /* If test specified, holds all console output */
    static ByteArrayOutputStream testOutputString;

    /* Use it or not, as you wish! */
    private static boolean DEBUG = false;

    /* if you want to restore output to console */
    static PrintStream old = System.out;

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two
     *             parameters -- the first is a file name, and the
     *             second is test (for test output, where all output
     *             to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
      
    	if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
            }
            if (args.length >= 2) {
                /* If the word "test" is the second argument to java */
                if (args[1].equals("test")) {
                    /* Create a stream to hold the output */
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    /* Save the old System.out. */
                    old = System.out;
                    /* Tell Java to use the special stream; all
                     * console output will be redirected here from
                     * now */
                    System.setOut(ps);
                }
            }
        } else { // If no arguments to main
            kb = new Scanner(System.in); // Use keyboard and console
        }
        commandInterpreter(kb);

        System.out.flush();
        
        
    } 

    /* Do not alter the code above for your submission. */
    public static void main2(String[] arg) {
    	Scanner kb = new Scanner(System.in);
    	commandInterpreter(kb);
    }
    
    private static void commandInterpreter(Scanner kb) {
        //TODO Implement this method
    	boolean runFlag = true;
    	while(runFlag) {
    		
    	System.out.println("critters>");
    	String userInput = kb.nextLine();
    	String strInp[] = userInput.split("\\s+");
    	
    	String err = "error processing: "+ userInput;
    	
    	if(strInp[0].equals("show")) {
    		if(strInp.length > 1) {
        		System.out.println("error processing: "+ userInput);

    		}
    		//Not sure if it should still display
    		else {
    			Critter.displayWorld();
    		}
    		
    		
    	}
    	else if(strInp[0].equals("create")) {
    		if(strInp.length ==2){
				try{Critter.createCritter(strInp[1]);}
				catch(InvalidCritterException e) {
					System.out.println("error processing: "+ userInput);
					//e.printStackTrace();
				}
    		}
    		else if (strInp.length == 3) {
    		try {
    			int amt = Integer.parseInt(strInp[2]);
    			for(int i =0;i<amt;i++) {
    				Critter.createCritter(strInp[1]);
    			}
				
			} catch (InvalidCritterException|NumberFormatException e) {
				// TODO Auto-generated catch block
	    		System.out.println("error processing: "+ userInput);
				
				
			}
    		}
    		else {
    			System.out.println(err);
    		}
    		}
    	else if(strInp[0].equals("step")) {
    		if(strInp.length == 1) {
    			Critter.worldTimeStep();
    		}
    		else {
    			try {
    	   			int numSteps = Integer.parseInt(strInp[1]);
        			for(int i=0; i<numSteps; i++) {
        				Critter.worldTimeStep();
        			}
    				
    			}
    			catch(NumberFormatException e) {
    				System.out.println(err);
    			}
 
    		}
    		
    	}
    	else if (strInp[0].equals("seed")) {
    		if(strInp.length!= 2) {
    			System.out.println(err);
    		}
    		try {
    		Critter.setSeed(Long.parseLong(strInp[1]));}
    		catch(NumberFormatException e) {
    			System.out.println(err);
    		}
    	}
    	else if(strInp[0].equals("quit")) {
    		if(strInp.length!= 1) {
    			System.out.println(err);
    		}
    		runFlag = false;
    	}
    	else if (strInp[0].equals("clear")) {
    		if(strInp.length!= 1) {
    			System.out.println(err);
    		}
    		Critter.clearWorld();
    	}
    	else if (strInp[0].equals("stats")) {
    		if(strInp.length!= 2) {
    			System.out.println(err);
    		}
    		try {
    			//No idea if this works 
        		List<Critter> l = Critter.getInstances(strInp[1]);
        		Class<?> cclass = Class.forName(myPackage+"."+strInp[1]);
        		Class<?>[] argTypes =  {List.class};
        		Method runStats = cclass.getMethod("runStats", argTypes);
        		runStats.invoke(cclass, l);

    		
    	}
    		catch(Exception e){
    			System.out.println(err);
    			
    		}
    	}
    	else {
    		System.out.println("invalid command: "+ userInput);
    	}
    	}
    	
    }
}
