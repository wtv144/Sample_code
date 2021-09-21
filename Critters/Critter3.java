/*
 * CRITTERS Critter3.java
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
 *This type of critter only fights if it has more than 10 energy. Randomly runs or walks
 */
public class Critter3 extends Critter {
	@Override
	public void doTimeStep() {
		if(getRandomInt(12)% 3 == 0) {
			run(getRandomInt(8));
		}
		else {
			walk(getRandomInt(8));
		}
	}
	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 10) return true;
        return false;
	}
	@Override
	public String toString() {
		return "3";
	}
}
