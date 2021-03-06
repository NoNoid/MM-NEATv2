package edu.utexas.cs.nn.tasks.rlglue.featureextractors.tetris;

import org.rlcommunity.environments.tetris.TetrisState;
import org.rlcommunity.rlglue.codec.types.Observation;

import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.tasks.rlglue.featureextractors.FeatureExtractor;
import edu.utexas.cs.nn.tasks.rlglue.tetris.TetrisAfterStateAgent;

/**
 * Primarily designed to be used by HyperNEAT.
 * Simple provides raw information about the world state as features.
 * 
 * @author Lauren Gillespie
 */
public class RawTetrisStateExtractor implements FeatureExtractor {

        /**
         * One feature for each block in the world state
         * @return 
         */
	@Override
	public int numFeatures() {
		return TetrisState.worldHeight * TetrisState.worldWidth;
	}

        /**
         * An array containing a 1 if a block is present, and a 0 otherwise.
         * @param o
         * @return 
         */
	@Override
	public double[] extract(Observation o) {
		boolean negative = Parameters.parameters.booleanParameter("absenceNegative");
		TetrisState state = TetrisAfterStateAgent.observationToTetrisState(o);
		double[] result = new double[state.worldState.length];
		for (int i = 0; i < result.length; i++) {
			if(Math.signum(state.worldState[i]) == 0){
				int temp = negative ? -1 : 0;
				result[i] = temp;
			} else {
				result[i] = Math.signum(state.worldState[i]);
			}
		}
		return result;
	}

        /**
         * Features are simply named after their coordinates on the screen
         * @return array of feature labels
         */
	@Override
	public String[] featureLabels() {
		String[] labels = new String[numFeatures()];
		int in = 0;
		for (int i = 0; i < TetrisState.worldHeight; i++) {
			for (int j = 0; j < TetrisState.worldWidth; j++) {
				labels[in++] = "(" + j + ", " + i + ") occupied?";
			}
		}
		return labels;
	}

        /**
         * No scaling needed since all values are 0 or 1
         * @param inputs original inputs
         * @return original inputs (identity function)
         */
	@Override
	public double[] scaleInputs(double[] inputs) {
		return inputs;
	}

}
