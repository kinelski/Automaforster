import static org.junit.Assert.*;

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
		regex = "(a+b)*b*b(b+a)*";
		auto.processaRegex(regex);
	}
	

}
