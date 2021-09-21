/*
 * CRITTERS Critter4.java
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

import java.util.List;

/**
 * This type of critter is wise and only fights when it has more than half its start energy
 *
 */
public class Critter4 extends Critter{
	@Override
	public void doTimeStep() {
	       walk(getRandomInt(2));
	    }

	    @Override
	    public boolean fight(String opponent) {
	        if (getEnergy() > Params.START_ENERGY/2) return true;
	        return false;
	    }
	    @Override
	    public String toString() {
	        return "4";
	    }

	   
}
