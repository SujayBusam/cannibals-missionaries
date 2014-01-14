
// CLEARLY INDICATE THE AUTHOR OF THE FILE HERE (YOU),
//  AND ATTRIBUTE ANY SOURCES USED (INCLUDING THIS STUB, BY
//  DEVIN BALKCOM).

package cannibals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class UUSearchProblem {

	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;

	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
	}

	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.

	public List<UUSearchNode> breadthFirstSearch() {
		resetStats();

		Queue<UUSearchNode> fringe = new LinkedList<UUSearchNode>(); // The queue

		// Hashmap that keeps track of visited nodes (states) and maps them to respective backpointer
		HashMap<UUSearchNode, UUSearchNode> visitedNodes = new HashMap<UUSearchNode, UUSearchNode>(); 

		UUSearchNode currentNode;

		// Add start node with no backpointer to visited map
		visitedNodes.put(startNode, null);

		// Add start node to queue
		fringe.add(startNode);
		nodesExplored = 1;

		// Implementation of BFS algorithm
		while (!fringe.isEmpty()) {
			currentNode = fringe.poll();

			// If goal is reached, return path
			if (currentNode.goalTest()) {
				return backchain(currentNode, visitedNodes);
			}

			// Goal state not reached
			else {
				for (UUSearchNode successor: currentNode.getSuccessors()) {
					// If successor has not already been visited, add to visited hashmap
					// with current backpointer
					if (!visitedNodes.containsKey(successor)) {
						visitedNodes.put(successor, currentNode);

						// Enqueue successor
						fringe.add(successor);

						nodesExplored++;
					}
				}
			}
		}

		// Returns null if goal state doesn't exist
		return null;
	}

	// backchain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {

		List<UUSearchNode> goalPath = new LinkedList<UUSearchNode>(); // Path to return
		UUSearchNode currentNode = node;

		// If current is null, we have reached backpointer of start node
		while (currentNode != null) {
			// Add to beginning of linked list
			((LinkedList<UUSearchNode>) goalPath).addFirst(currentNode);
			// Update current to reference the backpointer
			currentNode = visited.get(currentNode);
		}

		return goalPath;
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats();

		// Hashmap that keeps track of visited nodes (states) and their respective depths
		HashMap<UUSearchNode, Integer> visitedNodes = new HashMap<UUSearchNode, Integer>();

		List<UUSearchNode> path = dfsrm(startNode, visitedNodes, 0, maxDepth);
		return path;
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {

		// keep track of stats; these calls charge for the current node
		updateMemory(visited.size());
		incrementNodeCount();

		// you write this method.  Comments *must* clearly show the 
		//  "base case" and "recursive case" that any recursive function has.	

		// add current node to visited hashmap
		visited.put(currentNode, depth);

		// base case
		if (currentNode.goalTest()) {
			// when goal node is reached, return a list with goal node
			// so that its parents can be added
			List<UUSearchNode> path = new LinkedList<UUSearchNode>();
			path.add(currentNode);
			return path;
		}

		// recursive case
		else {
			// Get the successors and make sure it is not a null list
			// Note if it is a null set, we have reached a leaf node that is not the goal
			List<UUSearchNode> successors = currentNode.getSuccessors();
			if (successors != null) {

				// For all successors with max depth not exceeded
				for (UUSearchNode successor: successors) {
					if (depth + 1 <= maxDepth) {

						// And for all successors that are not in hashmap, or are in hashmap 
						// but were visited at a greater previous depth
						if (!visited.containsKey(successor) || visited.get(successor) > depth + 1) {

							// If path exists, add current node to path that has been passed up through recursion
							List<UUSearchNode> currentList = dfsrm(successor, visited, depth + 1, maxDepth);
							if(currentList != null) {
								((LinkedList<UUSearchNode>) currentList).addFirst(currentNode);
								return currentList;
							}

							// Otherwise, return a null path
							else {
								return null;
							}
						}
					}
				}
				// No valid successors if it reaches this point
			}

			// Reached a leaf that is not a goal state
			else {
				return null;
			}
		}

		return null;
	}


	// set up the iterative deepening search, and make use of dfspc
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();

		// Hashmap that keeps track of visited nodes (states) and their respective depths
		HashSet<UUSearchNode> visitedNodes = new HashSet<UUSearchNode>();

		for (int i = 0; i <= maxDepth; i++) {
			List<UUSearchNode> path = dfsrpc(startNode, visitedNodes, 0, i);

			// If path found, return it
			if (path != null) {
				return path;
			}
		}

		return null;
	}

	// set up the depth-first-search (path-checking version), 
	// but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();

		// I wrote this method for you.  Nothing to do.

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();

		return dfsrpc(startNode, currentPath, 0, maxDepth);

	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {

		// Add current node to current path set
		currentPath.add(currentNode);

		// Base case - goal node has been found
		if (currentNode.goalTest()) {
			List<UUSearchNode> path = new LinkedList<UUSearchNode>();
			path.add(currentNode);
			return path;
		}

		// Recursive case
		else {
			// Get the successors and make sure it is not a null set
			// Note if it is a null set, we have reached a leaf node that is not the goal
			List<UUSearchNode> successors = currentNode.getSuccessors();
			if (successors != null) {

				for (UUSearchNode successor: successors) {
					// Make sure successor isn't one that was already on current path (a parent)
					// and doesn't exceed max depth
					if (!currentPath.contains(successor) && depth + 1 <= maxDepth) {
						List<UUSearchNode> path = dfsrpc(successor, currentPath, depth + 1, maxDepth);

						// If path exists, add current node to path that has been passed up through recursion
						if (path != null) {
							((LinkedList<UUSearchNode>) path).addFirst(currentNode);
							return path;
						}

						// Otherwise, remove current node from current path and return a null path
						else {
							currentPath.remove(currentNode);
							return null;
						}
					}
				}

				// If it reaches this point, current node has no valid successors
				// Remove it from the current path and return null
				currentPath.remove(currentNode);
				return null;
			}

			// Reached a leaf that is not the goal
			else {
				currentPath.remove(currentNode);
				return null;
			}
		}
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}

	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}

	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}

	protected void incrementNodeCount() {
		nodesExplored++;
	}

	// main method for testing
	public static void main(String[] args) {

		CannibalProblem mcProblem = new CannibalProblem(3, 3, 1, 0, 0, 0);
		//		List<UUSearchNode> path = mcProblem.breadthFirstSearch();
		//
		//		for (UUSearchNode node: path) {
		//			System.out.println(node);
		//		}

		//		List<UUSearchNode> path = mcProblem.depthFirstMemoizingSearch(12);
		//
		//		for (UUSearchNode node: path) {
		//			System.out.println(node);
		//		}

		List<UUSearchNode> path = mcProblem.IDSearch(100);
		for (UUSearchNode node: path) {
			System.out.println(node);
		}

	}
}

