package MovsimDataAssimilation;

import java.util.Random;

public interface GlobalConstants
{
	final double STEP_LENTH = 15; 
	
	
	// random generator
	int RANDOM_SEED = 100;
	Random G_RAND = new Random(RANDOM_SEED);
	
	// Transition random
	boolean TRANSITION_BEHAVIOR_RANDOM = true;
	boolean TRANSITION_RANDOM_SHIFT = true;
	boolean TRANSITION_OBSERVATION_REAL = false;
	// boolean TRANSITION_REDISTRIBUTION = false; 
	double SHIFT_X_SIGMA = 20;
	double SHIFT_Y_THRESHOLD = 0.5;
	
	
	double TRANSITION_ACCIDENT_RATE = 0.5;//0.2;
	boolean SHOW_FIG = false;
	
}