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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    
    private boolean moved;
    private boolean isFighting;

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
        // TODO: Complete this method
    	try {
    		Class<?> c = Class.forName(myPackage + "." +  critter_class_name);
    		Critter crit = (Critter)c.newInstance();
    		crit.energy = Params.START_ENERGY;
    		crit.x_coord = getRandomInt(Params.WORLD_WIDTH);
    		crit.y_coord = getRandomInt(Params.WORLD_HEIGHT);
    		population.add(crit);
    		CritterWorld.addCritter(crit, crit.x_coord, crit.y_coord);
    		//System.out.println(population);
    		
    		
    	}
    	catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) { 
    		throw new InvalidCritterException(critter_class_name);
    		
    	}
   
    	
    	
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
    	try {
		Class<?> c = Class.forName(myPackage + "." +  critter_class_name);
		List<Critter> oftype = new ArrayList<Critter>();
		for(Critter cr: population) {
			if(c.isInstance(cr)) {
				oftype.add(cr);
			}
		}
		return oftype;
		
    	}
    	catch(ClassNotFoundException  e){
    		throw new InvalidCritterException(critter_class_name);
    	}
        // TODO: Complete this method
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        // TODO: Complete this method
    	population.clear(); 
    	babies.clear(); 
    	CritterWorld.clearCritterWorld();
    	
    	
    }

    
    /**
     * Carries out a timestep for all critters currently in the board
     * Removes dead critters and generates more clovers for next turn
     */
    public static void worldTimeStep() {
        // TODO: Complete this method
    	for(Critter c: population) {
    		c.moved = false;  // allow critters to move only at the start of each time step
    	}
    	
    	
    	for(Critter c: population) {
    		c.doTimeStep(); //all critters have moved to their new locations
    	}
    	
    	doEncounters();  // fighting/running away occurs here
    	updateRestEnergy();
    	removeDeadCritters();
    	genClover();
    	population.addAll(babies);
    	babies.clear();
    	//updateWorld();
    	
    	
    	
    	
    }
    
    /**
     * Generates clovers for next timestep
     */
    private static void genClover() {
    	for(int i=0; i<Params.REFRESH_CLOVER_COUNT; i++) {
    		try {
				createCritter("Clover");
			} catch (InvalidCritterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	

    }
    
    
    /**
     * Decreases each critters energy by the rest energy amount
     */
    private static void updateRestEnergy() {
     	for(Critter c: population) {
    		c.energy -= Params.REST_ENERGY_COST;
    	}
    }
    
    /**
     * Removes all dead critters from the population
     */
    private static void removeDeadCritters() {
  
    	for(int i=0; i<population.size(); i++) {
    		Critter curr = population.get(i);
    		if(curr.energy <= 0) {
    			population.remove(curr);
    			i--;
    		}
    	}

    	
    }
    
    /**
     * Checks if a critter is still alive
     * @return
     */
    private boolean isAlive() {
    	return this.energy > 0;
    }
    
    /**
     * Checks all conflicts among critters at the same location
     * Invokes conflicting critters' fight methods
     */
    private static void doEncounters() {
    	
    	//check all conflicts
    	for(int i=0; i<Params.WORLD_WIDTH; i++) {
    		for(int j=0; j<Params.WORLD_HEIGHT; j++) {
    			ArrayList<Critter> locationMatches = new ArrayList<Critter>();
    			for(Critter c: population) {  //check how many critters in the population have the same location
    				if(c.x_coord == i && c.y_coord == j) {
    					locationMatches.add(c);
    				}
    				
    			} 
				//ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();

    			
    			while(locationMatches.size() > 1) {  //This means there are more than two critters at location {i, j}
    				Critter a = locationMatches.get(0);
    				Critter b = locationMatches.get(1);
    				
    				a.isFighting = true;   //this ensures that a or b can no longer move into an occupied position during the fight() method
    				b.isFighting = true;
    				
    				boolean aFight = a.fight(b.toString());
    				boolean bFight = b.fight(a.toString());
    				
    				a.isFighting = false;
    				b.isFighting = false;
    				
    				if(a.isAlive() && b.isAlive() && a.x_coord==b.x_coord && a.y_coord==b.y_coord) {
        				int aRoll = 0;
        				if(aFight) {
        					aRoll = getRandomInt(a.getEnergy());
        					
        				}
        				
        				
        				
        				int bRoll = 0;
        				if(bFight) {
        					bRoll = getRandomInt(b.getEnergy());
        				}
        				
        				if(aRoll >= bRoll) {     //if both are equal, it will choose critter a
        					a.energy += b.energy/2;
        				
        					ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();
        					
        					//critterWorld[i][j].remove(b);
        					critterWorld[i][j].remove(b);
        					population.remove(b);
        					locationMatches.remove(b);
        				}
        				else if(aRoll < bRoll) {
        					b.energy += a.energy/2;
        				
        					ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();
        					//critterWorld[i][j].remove(a);
        					critterWorld[i][j].remove(a);
        					population.remove(a);
        					locationMatches.remove(a);
        				}
    				}
    				else {
    					ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();

    					if(a.energy<=0) {
    						population.remove(a);
    						critterWorld[i][j].remove(a);
    						locationMatches.remove(a);
    						
    					}
    					if(b.energy<=0) {
    						population.remove(b);
    						critterWorld[i][j].remove(b);
    						locationMatches.remove(b);
    						
    					}
    					
    					if(a.x_coord != i || a.y_coord!=j) {
    						locationMatches.remove(a);
    					}
    					if(b.x_coord != i || b.y_coord!=j) {
    						locationMatches.remove(b);
    					}
    					
    					
    					
    					
    				}
    				

    				
    			}
    		}
    	}
    	
    }
    
    /**
     * Updates the critter matrix in CritterWorld to the current state in the population list
     */
    private static void updateWorld() {
       	CritterWorld.clearCritterWorld();
    	for(Critter c: population) {    //adds all critters in population to their positions in the CritterWorld matrix
    		CritterWorld.addCritter(c, c.x_coord, c.y_coord);
    	}
    }

    
    /**
     * displays the matrix of critters with its surrounding borders
     */
    public static void displayWorld() {
        // TODO: Complete this method
    	updateWorld();
    	
    	printTopBottomBorder();  //top border

    	ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();
    	
    	for(int r=0; r<Params.WORLD_HEIGHT; r++) {
    		System.out.print("|"); //left border
    		for(int c=0; c<Params.WORLD_WIDTH; c++) {
    			if(critterWorld[c][r] == null) {
    				System.out.print(" ");
    			}
    			else {
    				System.out.print(critterWorld[c][r].get(0));  
    			}
    			
    		}
    		
    		System.out.println("|"); //right border
    	}
    	
    	
    	printTopBottomBorder();  //bottom border
    	System.out.println();

    	
    }
    
    /**
     * Prints the top and bottom border consisting of + and - as described in the assignment document
     */
    private static void printTopBottomBorder() {
    	for(int i=0; i<Params.WORLD_WIDTH+2; i++) {
    		if(i==0 || i==Params.WORLD_WIDTH+1) {
    			System.out.print("+");
    		}
    		else {
    			System.out.print("-");
    		}
    	}
    	System.out.println();
    }
    
    /**
     * Prints out how many Critters of each type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    /**
     * Moves the critter one step in the specified direction
     * Will revert critter location back to original if it is fighting and the new position is occupied
     * @param direction
     */
    protected final void walk(int direction) {
        // TODO: Complete this method
    	energy -= Params.WALK_ENERGY_COST;
    	if(!moved) {
    		
    		int prevX = this.x_coord;
    		int prevY = this.y_coord;
    		
    		this.move(direction, 1);
    		//System.out.print(prevX +" "+  prevY + " direction: " + direction);
    		//System.out.print("newx: " + this.x_coord + " newY: " + this.y_coord);
    		ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();
    		ArrayList<Critter> newPos = critterWorld[this.x_coord][this.y_coord];
    		//System.out.print(" old positions list: " + critterWorld[prevX][prevY]);
    		//System.out.println(" new positions List: " + newPos);
    		if(newPos!=null && newPos.size() > 0 && isFighting) {  //if new position is occupied and critter is currently fighting, go back to previous location
    			//System.out.print("Inside if");
    			this.x_coord = prevX;
    			this.y_coord = prevY;
    		}
    		
    		
    		//System.out.println("List: " + CritterWorld.getCritterWorld()[prevX][prevY]);
    	//	CritterWorld.critterWorld[prevX][prevY].remove(this);
    		CritterWorld.addCritter(this, this.x_coord, this.y_coord); //moves it in the grid so that another critter trying to move here can't
    		
    		moved = true;
    		
    	}
    	
    
    }
    


    /**
     * Moves the critter two steps in the specified direction
     * Will revert critter location back to original if it is fighting and the new position is occupied
     * @param direction
     */
    protected final void run(int direction) {
        // TODO: Complete this method
    	energy -= Params.RUN_ENERGY_COST;
    	if(!moved) {
    		
    		int prevX = this.x_coord; //store previous position in case it is fighting
    		int prevY = this.y_coord;
    		this.move(direction, 2);
    		
    		ArrayList<Critter>[][] critterWorld = CritterWorld.getCritterWorld();
    		ArrayList<Critter> newPos = critterWorld[this.x_coord][this.y_coord];
    		
    		if(newPos!=null && newPos.size() > 0  && isFighting) {  //if new position is occupied and critter is currently fighting, go back to previous location
    			this.x_coord = prevX;
    			this.y_coord = prevY;
    		}
    		
    		//CritterWorld.critterWorld[prevX][prevY].remove(this);
    		CritterWorld.addCritter(this, this.x_coord, this.y_coord); //moves it in the grid so that another critter trying to move here can't

    		moved = true;
    		
    	}

    }
    

    /**
     * Changes the position of the critter based on direction
     * Implements a torus style grid where opposing ends are adjacent with each other
     * @param direction
     * @param positions
     */
    
    private void move(int direction, int positions) {
    	if(direction == 0) {    
    		this.x_coord += positions;  //right
    	}
    	else if(direction == 1){ 
    		this.x_coord += positions;   //up-right
    		this.y_coord -= positions;
    	}
    	else if(direction == 2) { 
    		this.y_coord -= positions;   //up
    		
    	}
    	else if(direction == 3) {
    		this.x_coord -= positions; //up-left
    		this.y_coord -= positions; 
    	}
    	else if(direction == 4) {
    		this.x_coord -= positions;  //left
    		
    	}
    	else if(direction == 5) {
    		this.x_coord -= positions;   //down-left
    		this.y_coord += positions;
    	
    	}
    	else if(direction == 6) {
    		this.y_coord += positions;  //down
    		
    	}
    	else if(direction == 7) {
    		this.x_coord += positions; //down-right
    		this.y_coord += positions;
    	}
    	
    	
    	//Check wrap around horizontally
    	if(this.x_coord < 0) {
    		this.x_coord += Params.WORLD_WIDTH;
    	}
    	else if(this.x_coord >= Params.WORLD_WIDTH) {
    		this.x_coord -= Params.WORLD_WIDTH;
    	}
    	
    	//Check wrap around vertically
    	if(this.y_coord < 0) {
    		this.y_coord += Params.WORLD_HEIGHT;
    	}
    	else if(this.y_coord >= Params.WORLD_HEIGHT) {
    		this.y_coord -= Params.WORLD_HEIGHT;
    	}
    	
    }

    /**
     * Creates babies if enough energy is available and if parent is alive
     * @param offspring
     * @param direction
     */
    protected final void reproduce(Critter offspring, int direction) {
        // TODO: Complete this method
    	if(this.energy<=0 || this.energy < Params.MIN_REPRODUCE_ENERGY) {
    		return;
    	}
    	
    	offspring.energy = this.energy/2;
    	this.energy = (int)Math.ceil(this.energy/2); //round up
    	
    	offspring.x_coord = this.x_coord;
    	offspring.y_coord = this.y_coord;
    	offspring.walk(direction); //Moves the offspring in the specified direction AND will place the offspring in the grid
    	CritterWorld.getCritterWorld()[offspring.x_coord][offspring.y_coord].remove(offspring); // Since babies can't prevent other critters from moving to their location, we remove it from the grid
    	babies.add(offspring);
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
