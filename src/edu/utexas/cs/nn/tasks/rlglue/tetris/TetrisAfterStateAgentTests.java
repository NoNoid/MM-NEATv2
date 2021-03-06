package edu.utexas.cs.nn.tasks.rlglue.tetris;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rlcommunity.environments.tetris.TetrisState;
import org.rlcommunity.rlglue.codec.types.Action;
import org.rlcommunity.rlglue.codec.types.Observation;

import edu.utexas.cs.nn.MMNEAT.MMNEAT;
import edu.utexas.cs.nn.evolution.EvolutionaryHistory;
import edu.utexas.cs.nn.parameters.Parameters;
import edu.utexas.cs.nn.tasks.rlglue.featureextractors.tetris.BertsekasTsitsiklisTetrisExtractor;

public class TetrisAfterStateAgentTests {

	/**
	 * Instantiates parameters
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		MMNEAT.clearClasses();
		EvolutionaryHistory.setInnovation(0);
		EvolutionaryHistory.setHighestGenotypeId(0);
		Parameters.initializeParameterCollections(new String[] { "io:false", "netio:false", "recurrency:false",
				"rlGlueExtractor:edu.utexas.cs.nn.tasks.rlglue.featureextractors.tetris.BertsekasTsitsiklisTetrisExtractor" });
		MMNEAT.loadClasses();
	}

	/**
	 * Tests that the outputs scale correctly
	 */
	@Test
	public void does_scale() {
		TetrisState testState = new TetrisState();
		BertsekasTsitsiklisTetrisExtractor BTTE = new BertsekasTsitsiklisTetrisExtractor();
		// line piece
		testState.worldState[166] = 1;
		testState.worldState[167] = 1;
		testState.worldState[168] = 1;
		testState.worldState[169] = 1;
		testState.currentX += 2;
		testState.currentY += 14;
		// S piece
		testState.worldState[171] = 1;
		testState.worldState[172] = 1;
		testState.worldState[180] = 1;
		testState.worldState[181] = 1;
		// J piece 1
		testState.worldState[192] = 1;
		testState.worldState[193] = 1;
		testState.worldState[194] = 1;
		testState.worldState[182] = 1;
		// J piece 2
		testState.worldState[197] = 1;
		testState.worldState[187] = 1;
		testState.worldState[177] = 1;
		testState.worldState[178] = 1;
		// tri piece
		testState.worldState[195] = 1;
		testState.worldState[185] = 1;
		testState.worldState[175] = 1;
		testState.worldState[186] = 1;

		Observation o = testState.get_observation();
		double[] inputs = BTTE.scaleInputs(BTTE.extract(o));

		double[] expected = new double[] { 0.1, 0.15, 0.15, 0.05, 0.05, 0.15, 0.2, 0.2, 0.2, 0.2, 0.05, 0, 0.1, 0, 0.1,
				0.05, 0, 0, 0, 0.2, 0.045, 1 };
		for (int i = 0; i < inputs.length; i++) {
			assertEquals(inputs[i], expected[i], 0.0);
		}
	}

	@Test
	public void action_sequence() {
		TetrisAfterStateAgent afterStateAgent = new TetrisAfterStateAgent();
		TetrisState testState = new TetrisState();
		BertsekasTsitsiklisTetrisExtractor BTTE = new BertsekasTsitsiklisTetrisExtractor();
		// line piece
		testState.worldState[166] = 1;
		testState.worldState[167] = 1;
		testState.worldState[168] = 1;
		testState.worldState[169] = 1;
		testState.currentX += 2;
		testState.currentY += 14;
		// S piece
		testState.worldState[171] = 1;
		testState.worldState[172] = 1;
		testState.worldState[180] = 1;
		testState.worldState[181] = 1;
		// J piece 1
		testState.worldState[192] = 1;
		testState.worldState[193] = 1;
		testState.worldState[194] = 1;
		testState.worldState[182] = 1;
		// J piece 2
		testState.worldState[197] = 1;
		testState.worldState[187] = 1;
		testState.worldState[177] = 1;
		testState.worldState[178] = 1;
		// tri piece
		testState.worldState[195] = 1;
		testState.worldState[185] = 1;
		testState.worldState[175] = 1;
		testState.worldState[186] = 1;

		Observation o = testState.get_observation();
		assertTrue(afterStateAgent.currentActionList.isEmpty());
		Action a = afterStateAgent.getAction(o);
		assertFalse(afterStateAgent.currentActionList.isEmpty());

	}
}
