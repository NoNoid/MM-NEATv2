package edu.utexas.cs.nn.networks.hyperneat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.utexas.cs.nn.MMNEAT.MMNEAT;
import edu.utexas.cs.nn.graphics.DrawingPanel;
import edu.utexas.cs.nn.networks.TWEANN.Node;
import edu.utexas.cs.nn.util.datastructures.Pair;



//need a method that takes an array list of nodes from tweann
//uses mMNEAT.task and casts it to hyperNeattask so it can get out specific substrate info about task
//then, creates a drawing panel for each substrate task uses
//fills in activations into rectangles of said substrate drawing panels using activation function from
//corresponding node in array list
//draw grid on substrate
//every time refreshed, draw new rectangles on top of old rectangles
//need a variable that scales rectangles down just a bit so it doesn't erase grid
/**
 * Util class containing methods used by hyperNEAT and its tasks
 * 
 * @author Lauren Gillespie
 *
 */
public class HyperNEATUtil {

	//size of grid in substrate drawing. Can be changed/turned into a param if need be
	public final static int SUBS_GRID_SIZE = 30;

	public static DrawingPanel[] substratePanels = null;

	private static HyperNEATTask hyperNEATTask;

	private static List<Substrate> substrates;
	
	// Schrum: This method isn't tested yet, but it should create all of the substrates
	// and update them based on neuron activations.
	public static void drawSubstrates(ArrayList<Node> nodes) {
		if(substratePanels == null) {
			hyperNEATTask = (HyperNEATTask) MMNEAT.task;
			substrates = hyperNEATTask.getSubstrateInformation();
			substratePanels = new DrawingPanel[substrates.size()];
			int nodeIndexStart = 0;
			for(int i = 0; i < substrates.size(); i++) {
				Substrate s = substrates.get(i);
				substratePanels[i] = drawSubstrate(s, nodes, nodeIndexStart);
				nodeIndexStart += s.size.t1 * s.size.t2;
			}
		} else {
			int nodeIndexStart = 0;
			for(int i = 0; i < substrates.size(); i++) {
				Substrate s = substrates.get(i);
				drawSubstrate(substratePanels[i], s, nodes, nodeIndexStart);
				nodeIndexStart += s.size.t1 * s.size.t2;
			}
		}
	}
	
	/**
	 * Draws substrate as a grid with squares representing each substrate coordinate
	 * @param s substrate
	 * @param nodes list of substrate nodes TODO: use to extract color of square
	 * @param c color of squares
	 * @return drawing panel containing substrate drawing
	 */
	public static DrawingPanel drawSubstrate(Substrate s, ArrayList<Node> nodes, int nodeIndexStart) { 
		DrawingPanel p = new DrawingPanel(s.size.t1 * SUBS_GRID_SIZE, s.size.t2 * SUBS_GRID_SIZE, s.name);
		return drawSubstrate(p, s, nodes, nodeIndexStart); // updates existing panel
	}

	/**
	 * Overloaded. Draws substrate as a grid with squares representing each substrate coordinate
	 *  
	 * @param dp drawing panel which substrate is going to be drawn onto
	 * @param s substate in question
	 * @param nodes list of substrate nodes TODO: use to extract color of square
	 * @param c color
	 * @return drawing panel containing drawing of substrate
	 */
	// Call inside of TWEANN.process at end
	public static DrawingPanel drawSubstrate(DrawingPanel dp, Substrate s, ArrayList<Node> nodes, int nodeIndexStart) { 
		for(int i = 0; i < s.size.t1; i ++) {
			for(int j = 0; j < s.size.t2; j++) {
				drawCoord(dp, s.size, nodes, nodeIndexStart);
				drawGrid(dp, s.size);
			}
		}
		return dp;
	}
	
	/**
	 * Draws grid around squares so they are more easily distinguishable
	 * @param p drawingPanel
	 * @param size size of substrate
	 */
	private static void drawGrid(DrawingPanel p, Pair<Integer, Integer> size) {
		// Loop through columns and rows to draw black lines
		p.getGraphics().setBackground(Color.white);
		p.getGraphics().setColor(Color.black);
		for(int i = 0; i <= size.t1; i++) {
			for(int j = 0; j <= size.t2; j++) {
				p.getGraphics().drawRect(i * SUBS_GRID_SIZE, j * SUBS_GRID_SIZE, (i + 1) * SUBS_GRID_SIZE, SUBS_GRID_SIZE * (j + 1));
			}
		}	
	}

	/**
	 * Draws squares representing a coordinate location from substrate
	 * @param p drawing panel
	 * @param size size of substrate
	 * @param c color of square
	 */
	private static void drawCoord(DrawingPanel p, Pair<Integer, Integer> size, ArrayList<Node> nodes, int nodeIndex) { 
		for(int i = 0; i < size.t1; i++) {
			for(int j = 0; j < size.t2; j++) {
				double activation = nodes.get(nodeIndex++).output();
				Color c = new Color(activation > 0 ? (int)(activation*255) : 0, 0, activation < 0 ? (int)(-activation*255) : 0);
				p.getGraphics().setColor(c);
				p.getGraphics().fillRect(i*SUBS_GRID_SIZE, j*SUBS_GRID_SIZE, SUBS_GRID_SIZE, SUBS_GRID_SIZE);
			}
		}

	}
}
