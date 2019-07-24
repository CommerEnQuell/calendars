package nl.commerquell.calendar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQJulianCalendarTest {
	private CQJulianCalendar cal;
	private int fixed;
	private int dayOfCal;

	@BeforeEach
	void setUp() throws Exception {
		this.cal = new CQJulianCalendar(1, 1, 1900);
		this.fixed = 693608;
		this.dayOfCal = 693610;
	}

	@Test
	void test() {
		testToFixed();
		testFromFixed();
		testDayOfCalendar();
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
	
	@Test
	void testDayOfCalendar() {
		int calcDay = cal.dayOfCalendar();
		if (calcDay != dayOfCal) {
			fail("Calculated day# of calendar: " + calcDay + ",  should be " + this.dayOfCal);
		}
	}

}
