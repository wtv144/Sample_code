/*
 * CRITTERS Critter2.java
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
 * This type of Critter fights only when the opposing Critter is of a different type
 *
 */
public class Critter2 extends Critter {
	@Override
	public void doTimeStep() {
		walk(getRandomInt(8));
	}
	@Override
	//fights if the opponent is of a different type
	public boolean fight(String opponent) {
		if(opponent.equals("Critter2"))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "2";
	}
}
