
public class Main {
	public static void main (String args[]){
		Auto teste =  new Auto();
		
		teste.addEdge(0, 3, "&");
		teste.addEdge(3, 3, "a");
		teste.addEdge(3, 2, "&");
		teste.addEdge(2, 5, "&");
		teste.addEdge(5, 5, "b");
		teste.addEdge(5, 4, "&");
		teste.addEdge(4, 6, "&");
		teste.addEdge(6, 6, "c");
		teste.addEdge(6, 1, "&");
		
		System.out.println("ANTES");
		for (int i = 0; i < teste.num_states; i++) {
			//System.out.println(i);
			for (int j = 0; j < teste.num_states; j++) {
				//System.out.println(j);
				if (teste.auto[i][j] != null) {
					for (int k = 0; k < teste.auto[i][j].size(); k++) {
						System.out.println(i+" "+j+": "+teste.auto[i][j].get(k));
					}
				} //if
			} //for j
		} //for i
		
		teste.removeEps();
		
		System.out.println("DEPOIS");
		for (int i = 0; i < teste.num_states; i++) {
			//System.out.println(i);
			for (int j = 0; j < teste.num_states; j++) {
				//System.out.println(j);
				if (teste.auto[i][j] != null) {
					for (int k = 0; k < teste.auto[i][j].size(); k++) {
						System.out.println(i+" "+j+": "+teste.auto[i][j].get(k));
					}
				} //if
			} //for j
		} //for i 
		
		
		System.out.println("ESTADOS FINAIS");
		for (int i = 0; i < teste.num_states; i++)
			if (teste.finalStates.contains(i))
				System.out.println(i);
	}
}
