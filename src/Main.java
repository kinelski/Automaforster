public class Main {
	public static void main (String args[]){
		
		Auto auto = new Auto();
		
		auto.addEdge(0, 1, "a");
		auto.addEdge(0, 2, "a");
		auto.addEdge(1, 1, "a");
		auto.addEdge(1, 3, "b");
		auto.addEdge(2, 3, "a");
		auto.addEdge(3, 2, "b");
		
		auto.print();
		System.out.println("");
		
		auto.getRegex();
		
	}
}
