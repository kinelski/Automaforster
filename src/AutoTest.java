import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class AutoTest {
	Auto auto;
	String regex;
	
	@Before
	public void setup(){
		 auto = new Auto();
	}
	
	@Test
	public void whenUnionExpectTrue() {
		regex = "aab+(cvs)*";
		assertTrue(auto.isUnion(regex));
		regex = "(aab+(cvs)*)+asd";
		assertTrue(auto.isUnion(regex));
		regex = "(aab+(cvs))*";
		assertFalse(auto.isUnion(regex));
		regex = "b*";
		assertFalse(auto.isUnion(regex));
		regex = "ab*";
		assertFalse(auto.isUnion(regex));
	}
	
	@Test
	public void whenStarExpectTrue() {
		regex = "aab+(cvs)*";
		assertFalse(auto.isStar(regex));
		regex = "(aab+(cvs)*)+asd";
		assertFalse(auto.isStar(regex));
		regex = "(aab+(cvs))*";
		assertTrue(auto.isStar(regex));
		regex = "b*";
		assertTrue(auto.isStar(regex));
		regex = "ab*";
		assertFalse(auto.isStar(regex));
	}
	@Test
	public void processaRegex() {
		//regex = "ab+(b+c)*";
		//regex = "a(b+c)d";
		//regex = "(a(b+c))*";
		//regex = "b*";
		//regex = "(a+b)*bb(b+a)*";
		//regex = "a*b+b*a";
		regex = "a*b*c*";
		//auto.processaRegex(regex,0);
		//auto.generateGraphFile();
		
		auto.createNFAFromRE(regex);
	}
	
	//TESTES ITEM 2 -----------------------------
	
	@Test
	public void item2ex1(){
		auto.setNumOfStates(7);
		
		auto.addEdge(0, 3, "&");
		auto.addEdge(3, 3, "a");
		auto.addEdge(3, 3, "b");
		auto.addEdge(3, 2, "&");
		auto.addEdge(2, 4, "b");
		auto.addEdge(4, 5, "b");
		auto.addEdge(5, 6, "&");
		auto.addEdge(6, 6, "a");
		auto.addEdge(6, 6, "b");
		auto.addEdge(6, 1, "&");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		Set<Integer> set = new TreeSet<Integer>();
		
		assertFalse(auto.getOutput("ab", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
		assertTrue(set.contains(4));
		assertFalse(set.contains(5));
		assertFalse(set.contains(6));
		
		assertTrue(auto.getOutput("abb", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
		assertTrue(set.contains(4));
		assertTrue(set.contains(5));
		assertTrue(set.contains(6));
		
		assertTrue(auto.getOutput("bba", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		assertTrue(set.contains(6));
		
		assertTrue(auto.getOutput("abba", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		assertTrue(set.contains(6));
	}
	
	@Test
	public void item2ex2(){
		auto.setNumOfStates(4);
		
		auto.addEdge(0, 2, "&");
		auto.addEdge(2, 1, "&");
		auto.addEdge(2, 3, "a");
		auto.addEdge(3, 2, "b");
		auto.addEdge(3, 2, "c");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		Set<Integer> set = new TreeSet<Integer>();
		
		assertTrue(auto.getOutput("ab", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertFalse(set.contains(3));
		
		assertFalse(auto.getOutput("abb", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		
		assertFalse(auto.getOutput("bba", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		
		assertFalse(auto.getOutput("abba", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
	}
	
	@Test
	public void item2ex3(){
		auto.setNumOfStates(6);
		
		auto.addEdge(0, 4, "&");
		auto.addEdge(0, 5, "&");
		auto.addEdge(4, 4, "a");
		auto.addEdge(5, 5, "b");
		auto.addEdge(4, 2, "&");
		auto.addEdge(5, 3, "&");
		auto.addEdge(2, 1, "b");
		auto.addEdge(3, 1, "a");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		Set<Integer> set = new TreeSet<Integer>();
		
		assertTrue(auto.getOutput("ab", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		
		assertFalse(auto.getOutput("abb", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		
		assertTrue(auto.getOutput("bba", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		
		assertFalse(auto.getOutput("abba", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
	}
	
	public void item2ex4(){
		auto.setNumOfStates(7);
		
		auto.addEdge(0, 3, "&");
		auto.addEdge(3, 3, "a");
		auto.addEdge(3, 2, "&");
		auto.addEdge(2, 5, "&");
		auto.addEdge(5, 5, "b");
		auto.addEdge(5, 4, "&");
		auto.addEdge(4, 6, "&");
		auto.addEdge(6, 6, "c");
		auto.addEdge(6, 1, "&");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		Set<Integer> set = new TreeSet<Integer>();
		
		assertTrue(auto.getOutput("ab", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertTrue(set.contains(4));
		assertTrue(set.contains(5));
		assertTrue(set.contains(6));
		
		assertTrue(auto.getOutput("abb", set));
		assertFalse(set.contains(0));
		assertTrue(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertTrue(set.contains(4));
		assertTrue(set.contains(5));
		assertTrue(set.contains(6));
		
		assertFalse(auto.getOutput("bba", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		assertFalse(set.contains(6));
		
		assertFalse(auto.getOutput("abba", set));
		assertFalse(set.contains(0));
		assertFalse(set.contains(1));
		assertFalse(set.contains(2));
		assertFalse(set.contains(3));
		assertFalse(set.contains(4));
		assertFalse(set.contains(5));
		assertFalse(set.contains(6));
	}
	
	//TESTES ITEM 4 -----------------------------
	
	@Test
	public void item4ex1(){
		auto.setNumOfStates(7);
		
		auto.addEdge(0, 3, "&");
		auto.addEdge(3, 3, "a");
		auto.addEdge(3, 3, "b");
		auto.addEdge(3, 2, "&");
		auto.addEdge(2, 4, "b");
		auto.addEdge(4, 5, "b");
		auto.addEdge(5, 6, "&");
		auto.addEdge(6, 6, "a");
		auto.addEdge(6, 6, "b");
		auto.addEdge(6, 1, "&");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		System.out.println("Regex 1: " + auto.getRegex());
	}
	
	@Test
	public void item4ex2(){
		auto.setNumOfStates(4);
		
		auto.addEdge(0, 2, "&");
		auto.addEdge(2, 1, "&");
		auto.addEdge(2, 3, "a");
		auto.addEdge(3, 2, "b");
		auto.addEdge(3, 2, "c");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		System.out.println("Regex 2: " + auto.getRegex());
	}
	
	@Test
	public void item4ex3(){
		auto.setNumOfStates(6);
		
		auto.addEdge(0, 4, "&");
		auto.addEdge(0, 5, "&");
		auto.addEdge(4, 4, "a");
		auto.addEdge(5, 5, "b");
		auto.addEdge(4, 2, "&");
		auto.addEdge(5, 3, "&");
		auto.addEdge(2, 1, "b");
		auto.addEdge(3, 1, "a");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		System.out.println("Regex 3: " + auto.getRegex());
	}
	
	@Test
	public void item4ex4(){
		auto.setNumOfStates(7);
		
		auto.addEdge(0, 3, "&");
		auto.addEdge(3, 3, "a");
		auto.addEdge(3, 2, "&");
		auto.addEdge(2, 5, "&");
		auto.addEdge(5, 5, "b");
		auto.addEdge(5, 4, "&");
		auto.addEdge(4, 6, "&");
		auto.addEdge(6, 6, "c");
		auto.addEdge(6, 1, "&");
		
		auto.setStartState(0);
		auto.addFinalState(1);
		
		System.out.println("Regex 4: " + auto.getRegex());
	}

}
