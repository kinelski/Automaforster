import java.util.ArrayList;

public class Auto {
	
	final int MAX_STATE = 100;
	
	private ArrayList<String>[][] auto;
	
	private boolean isAFD = false;
	private boolean noEps = false;
	
	public Auto(){
		auto = new ArrayList[MAX_STATE][MAX_STATE];
	}
	
	private void addEdge (int s1, int s2, String w){
		if (auto[s1][s2] == null)
			auto[s1][s2] = new ArrayList<String>();
		
		auto[s1][s2].add(w);
	}
	
}
