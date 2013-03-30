package Homework1;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This is the JUnit Test class.
 * @author Aniket 
 *
 */
public class test 
{
	/**
	 * This test case tests if the object is deleted from the portfolio if the number of stocks becomes zero.
	 */
	@Test
	public void test1() {
		ImplPortfolio testBag1=new ImplPortfolio();
		testBag1.newTrade("IBM", 100);
		testBag1.newTrade("IBM", -100);
		assertEquals(0, testBag1.isEmpty());
	}
	
	/**
	 * This test case tests if the object is added correctly.
	 */
	@Test
	public void test2() {
		ImplPortfolio testBag2=new ImplPortfolio();
		testBag2.newTrade("IBM", 100);
		testBag2.newTrade("MSFT", 200);
		testBag2.newTrade("IBM", -150);
		testBag2.newTrade("FB", 150);
		assertEquals(3, testBag2.isEmpty());
	}
	
	/**
	 * This test case tests if the object is updated correctly.
	 */
	@Test
	public void test3() {
		ImplPortfolio testbag3=new ImplPortfolio();
		testbag3.newTrade("YHOO", 100);
		testbag3.newTrade("YHOO", -150);
		testbag3.newTrade("YHOO", 400);
		assertEquals(1, testbag3.isEmpty());
		PositionIter p=testbag3.getPositionIter();
		assertEquals(350, p.getNextPosition().getQuantity());
		
	}
	
	/**
	 * This test case tests if the object added has zero quantity.
	 */
	@Test
	public void test4() {
		ImplPortfolio testbag4=new ImplPortfolio();
		testbag4.newTrade("YHOO", 0);
		assertEquals(0, testbag4.isEmpty());
	}
	
	/**
	 * This test case tests if the object is added if the symbol is null.
	 */
	@Test
	public void test5() {
		ImplPortfolio testbag5=new ImplPortfolio();
		testbag5.newTrade(null, 100);
		assertEquals(0, testbag5.isEmpty());
	}
	
	/**
	 * This test case tests if the object is added if the symbol has only spaces.
	 */
	@Test
	public void test6() {
		ImplPortfolio testbag6=new ImplPortfolio();
		testbag6.newTrade("   ", 100);
		assertEquals(0, testbag6.isEmpty());
	}
	
}
