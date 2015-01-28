package movSimSMC;

import java.util.Random;

public interface GlobalConstants
{
	// random generator
	int RANDOM_SEED = 20;
	Random G_RAND = new Random(RANDOM_SEED);
	
	// Transition random
	boolean TRANSITION_BEHAVIOR_RANDOM = true;
	boolean TRANSITION_RANDOM_SHIFT = true;
	// boolean TRANSITION_REDISTRIBUTION = false; 
	double SHIFT_X_SIGMA = 20;
	double SHIFT_Y_THRESHOLD = 0.5;
	
	
	double TRANSITION_ACCIDENT_RATE = 0.4;
	boolean SHOW_FIG = true;
	
}
