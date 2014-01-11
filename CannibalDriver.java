package cannibals;

import java.util.ArrayList;
import java.util.List;
import cannibals.UUSearchProblem.*;

public class CannibalDriver {
	public static void main(String args[]) {
	
//		public static final int MAXDEPTH = 5000;
//
//		// interesting starting state:  
//		//  8, 5, 1  (IDS slow, but uses least memory.)
//
//
//		// set up the "standard" 331 problem:
//		CannibalProblem mcProblem = new CannibalProblem(3, 3, 1, 0, 0, 0);
//	
//		List<UUSearchProblem.UUSearchNode> path;
//		
//		
//		path = mcProblem.breadthFirstSearch();	
//		System.out.println("bfs path length:  " + path.size() + " " + path);
//		mcProblem.printStats();
//		System.out.println("--------");
//		
//	
//		path = mcProblem.depthFirstMemoizingSearch(MAXDEPTH);	
//		System.out.println("dfs memoizing path length:" + path.size());
//		mcProblem.printStats();
//		System.out.println("--------");
//		
//		path = mcProblem.depthFirstPathCheckingSearch(MAXDEPTH);
//		System.out.println("dfs path checking path length:" + path.size());
//		mcProblem.printStats();
//		
//		
//		System.out.println("--------");
//		path = mcProblem.IDSearch(MAXDEPTH);
//		System.out.println("Iterative deepening (path checking) path length:" + path.size());
//		mcProblem.printStats();
		
		CannibalProblem mcProblem = new CannibalProblem(3, 3, 1, 0, 0, 0);
		ArrayList<UUSearchNode> successors = mcProblem.startNode.getSuccessors();
		
//		for (UUSearchNode node: successors) {
//			System.out.println(node);
//		}
		
		ArrayList<UUSearchNode> successors2 = successors.get(0).getSuccessors();
//		for (UUSearchNode node: successors2) {
//			System.out.println(node);
//		}
//		
		ArrayList<UUSearchNode> successors3 = successors2.get(0).getSuccessors();
		System.out.println(successors2.get(0));
		for (UUSearchNode node: successors3) {
			System.out.println(node);
		}
	}
}
