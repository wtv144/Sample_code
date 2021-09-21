/*
 * CRITTERS Critter1.java
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
/**
 * This Critter fights every critter it meets
 *
 */
public class Critter1 extends Critter{

	@Override
	public void doTimeStep() {
		walk(getRandomInt(8));
	}
	@Override
	public boolean fight(String opponent) {
		
		return true;
	}
	@Override
	public String toString() {
		return "1";
	}
}
