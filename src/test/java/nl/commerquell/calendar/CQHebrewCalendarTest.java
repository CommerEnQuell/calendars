package nl.commerquell.calendar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQHebrewCalendarTest {
	private CQHebrewCalendar cal;
	private int fixed = 507850;
	private int dayOfCal = 1881278;

	@BeforeEach
	void setUp() throws Exception {
		this.cal = new CQHebrewCalendar(1, 4, 5151);
		int year = cal.get(CQHebrewCalendar.YEAR);
		System.out.println("The year " + year + " has " + CQHebrewCalendar.lengthOfYear(year) + " days");
	}
	
	@Test
	void testToFixed() {
		int fixed = this.cal.toFixed();
		if (fixed != this.fixed) {
			fail("Calculated: " + fixed + ", ought to be " + this.fixed);
		}
	}
	
	@Test
	void testFromFixed() {
		CQHebrewCalendar cal = CQHebrewCalendar.fromFixed(fixed);
		if (cal != null) {
			int thatYear = cal.get(CQHebrewCalendar.YEAR);
			int thisYear = this.cal.get(CQHebrewCalendar.YEAR);
			if (thatYear != this.cal.get(CQHebrewCalendar.YEAR)) {
				fail("Calculated year is " + thatYear + ", ought to be " + thisYear);
			}
		}
	}
	
	@Test
	void testDayOfCalendar() {
		int calcDay = cal.dayOfCalendar();
		if (calcDay != dayOfCal) {
			fail("Calculated day# of calendar: " + calcDay + ",  should be " + this.dayOfCal);
		}
	}

	@Test
	void test() {
		testToFixed();
		testFromFixed();
		testDayOfCalendar();
	}

}
