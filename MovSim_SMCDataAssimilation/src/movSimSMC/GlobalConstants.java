package movSimSMC;

import java.util.Random;

public interface GlobalConstants
{
	// random generator
	int RANDOM_SEED = 2;
	Random G_RAND = new Random(RANDOM_SEED);
	
	// Transition random
	boolean TRANSITION_BEHAVIOR_RANDOM = false;
	boolean TRANSITION_RANDOM_SHIFT = true;
	double SHIFT_X_SIGMA = 10;
	double SHIFT_Y_THRESHOLD = 0.5;
	
	
	double TRANSITION_ACCIDENT_RATE = 0.2;
	boolean SHOW_FIG = true;
	
}
