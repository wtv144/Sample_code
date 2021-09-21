/*
 * CRITTERS CritterWorld.java
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

import java.util.ArrayList;

/** 
 * This class will help with displaying/storing the critter world as a matrix where we can store final positions
 * @author prasl
 *
 */
public class CritterWorld {
	
	public static ArrayList<Critter>[][] critterWorld = new ArrayList[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];
	
	/**
	 * Accesses the critterWorld outside the class
	 * @return
	 */
	public static ArrayList<Critter>[][] getCritterWorld() {
		return critterWorld;
	}
	
	/**
	 * adds a critter to the specified position in the matrix
	 * @param c
	 * @param x
	 * @param y
	 */
	public static void addCritter(Critter c, int x, int y) {
		if(critterWorld[x][y] == null) {
			critterWorld[x][y] = new ArrayList<Critter>();
		}
		critterWorld[x][y].add(c);
	}
	
	/**
	 * Clears the matrix, used in the critter 'clear' command
	 */
	public static void clearCritterWorld() {
		critterWorld = new ArrayList[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];
	}

}
