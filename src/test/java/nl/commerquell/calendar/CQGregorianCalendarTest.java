package nl.commerquell.calendar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQGregorianCalendarTest {
	private CQGregorianCalendar testDate;
	private int fixedValue;
	
	@BeforeEach
	void setUp() throws Exception {
		this.testDate = new CQGregorianCalendar(24, 7, -586);
		this.fixedValue = -214193;
	}

	@Test
	void testToFixed() {
		int fixed = testDate.toFixed();
		if (fixed != fixedValue) {
			
//			System.err.println("Unexpected value: " + fixed);
			fail("Should be " + fixedValue + ", not " + fixed);
		}
	}

	@Test
	void testInLeapYear() {
		fail("Not yet implemented");
	}

	@Test
	void testEpoch() {
		fail("Not yet implemented");
	}

	@Test
	void testIsValid() {
		fail("Not yet implemented");
	}

	@Test
	void testCQGregorianCalendarInt() {
		fail("Not yet implemented");
	}

	@Test
	void testCQGregorianCalendarIntIntInt() {
		fail("Not yet implemented");
	}
	
	@Test
	void testFromFixed() {
		CQGregorianCalendar aDate = CQGregorianCalendar.fromFixed(fixedValue);
		fail("Is " + aDate + ", should be " + testDate);
	}

}
