package nl.commerquell.calendar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQJulianCalendarTest {
	private CQJulianCalendar cal;
	private int fixed;

	@BeforeEach
	void setUp() throws Exception {
		this.cal = new CQJulianCalendar(26, 8, 1959);
		this.fixed = -214193;
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	void testToFixed() {
		int toFixed = this.cal.toFixed();
		if (toFixed != fixed) {
			fail("Calculated=" + toFixed + ", should be " + fixed);
		}
	}
	
	@Test
	void testFromFixed() {
		CQJulianCalendar aCal = CQJulianCalendar.fromFixed(this.fixed);
		if (!aCal.equals(this.cal)) {
			fail("Calculated: " + aCal + ", should be " + this.cal);
		}
	}

}
