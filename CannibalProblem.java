package cannibals;

import java.util.ArrayList;
import java.util.Arrays;
import cannibals.UUSearchProblem.*;


// for the first part of the assignment, you might not extend UUSearchProblem,
//  since UUSearchProblem is incomplete until you finish it.

public class CannibalProblem extends UUSearchProblem{

	// the following are the only instance variables you should need.
	//  (some others might be inherited from UUSearchProblem, but worry
	//  about that later.)

	private int goalm, goalc, goalb;
	private int totalMissionaries, totalCannibals; 

	public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
		// I (djb) wrote the constructor; nothing for you to do here.

		startNode = new CannibalNode(sm, sc, 1, 0);
		goalm = gm;
		goalc = gc;
		goalb = gb;
		totalMissionaries = sm;
		totalCannibals = sc;

	}

	// node class used by searches.  Searches themselves are implemented
	//  in UUSearchProblem.
	private class CannibalNode implements UUSearchNode {

		// do not change BOAT_SIZE without considering how it affect
		// getSuccessors. 

		private final static int BOAT_SIZE = 2;

		// how many missionaries, cannibals, and boats
		// are on the starting shore
		private int[] state; 

		// how far the current node is from the start.  Not strictly required
		//  for search, but useful information for debugging, and for comparing paths
		private int depth;  

		public CannibalNode(int m, int c, int b, int d) {
			state = new int[3];
			this.state[0] = m;
			this.state[1] = c;
			this.state[2] = b;

			depth = d;
		}

		public ArrayList<UUSearchNode> getSuccessors() {

			// add actions (denoted by how many missionaries and cannibals to put
			// in the boat) to current state. 

			// You write this method.  Factoring is usually worthwhile.  In my
			//  implementation, I wrote an additional private method 'isSafeState',
			//  that I made use of in getSuccessors.  You may write any method
			//  you like in support of getSuccessors.

			ArrayList<UUSearchNode> successorList = new ArrayList<UUSearchNode>();


			for (int i = 0; i <= totalMissionaries; i++) {
				for (int j = 0; j <= totalCannibals; j++) {
					CannibalNode successor = new CannibalNode(i, j, Math.abs(state[2] - 1), depth + 1);

					// if state is safe, add node to the successor list
					if (isSafeState(successor) && isValidState(successor)) {
						successorList.add(successor);
					}
				}
			}

			return successorList;
		}

		// is the state safe? In other words, are there equal or
		// more missionaries than cannibals on both sides of the river
		public boolean isSafeState(CannibalNode successor) {

			int missionariesStart = successor.state[0]; // missionaries at start bank
			int missionariesEnd = totalMissionaries - successor.state[0]; // missionaries at destination bank
			int cannibalsStart = successor.state[1]; // cannibals at start bank
			int cannibalsEnd = totalCannibals - successor.state[1]; // cannibals at destination bank

			// Make sure missionaries are not outnumbered
			if (missionariesStart >= cannibalsStart && missionariesStart != 0) {
				if (missionariesEnd >= cannibalsEnd && missionariesEnd != 0) {
					return true;
				}
			}
			
			if (missionariesEnd == 0) {
				return (missionariesStart >= cannibalsStart);
			}
			
			if (missionariesStart == 0) {
				return (missionariesEnd >= cannibalsEnd);
			}
			
			// At this point state must be unsafe
			return false;
		}

		// is state valid - has an action been taken and did not exceed boat size
		public boolean isValidState(CannibalNode successor) {

			int missionariesStart = successor.state[0]; // missionaries at start bank
			int cannibalsStart = successor.state[1]; // cannibals at start bank

			// Case where passengers go to other side of river
			if (state[2] == 1) {
				// Make sure some action has been taken
				return (!(missionariesStart == state[0] && cannibalsStart == state[1]) &&
						// No more than boat size have crossed river
						Math.abs(state[0] - missionariesStart) + Math.abs(state[1] - cannibalsStart) <= BOAT_SIZE &&
						// Number of people hasn't increased
						missionariesStart <= state[0] && cannibalsStart <= state[1]);
			}

			// Case where passengers return to bank
			else {
				// Make sure some action has been taken
				return (!(missionariesStart == state[0] && cannibalsStart == state[1]) &&
						// No more than boat size have crossed river
						Math.abs(state[0] - missionariesStart) + Math.abs(state[1] - cannibalsStart) <= BOAT_SIZE &&
						// Number of people hasn't decreased
						missionariesStart >= state[0] && cannibalsStart >= state[1]);
			}
		}

		@Override
		public boolean goalTest() {
			// you write this method.  (It should be only one line long.)
			return (state[0] == goalm && state[1] == goalc && state[2] == goalb);
		}



		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(state, ((CannibalNode) other).state);
		}

		@Override
		public int hashCode() {
			return state[0] * 100 + state[1] * 10 + state[0];
		}

		@Override
		public String toString() {
			// you write this method

			String returnString = ("Missionaries left on bank: " + state[0]);
			returnString += ("\nCannibals left on bank: " + state[1]);

			// Determine which side boat is on
			String boatSide = new String();
			if (state[2] == 1) {
				boatSide = "starting bank";
			}
			else if (state[2] == 0) {
				boatSide = "destination bank";
			}

			returnString += ("\nBoat is on " + boatSide) + "\n";

			returnString += ("State: " + state[0] + "," + state[1] + "," + state[2] + "\n");
			return returnString;
		}

		/*
        You might need this method when you start writing 
        (and debugging) UUSearchProblem.

		@Override
		public int getDepth() {
			return depth;
		}
		 */

	}


}
