package nl.commerquell.calendar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQIslamicCalendarTest {
	private CQIslamicCalendar cal;
	private int fixed;

	@BeforeEach
	void setUp() throws Exception {
		this.cal = new CQIslamicCalendar(12, 3, 1416);
		this.fixed = 728514;
	}

	@Test
	void testInLeapYear() {
		if (cal.inLeapYear()) {
			fail(cal.get(2) + " is unjustly consiered a leap year");
		}
	}

	@Test
	void testIsValid() {
		if (!cal.isValid()) {
			fail("Invalid date: " + cal);
		}
	}

	@Test
	void testIsLeapYear() {
		if (CQCalendar.isLeapYear(CQIslamicCalendar.class, cal.get(2))) {
			fail(cal.get(2) + " is unjustly considered a leap year in the static method");
		}
	}
	
	@Test
	void testToFixed() {
		int fixedDate = cal.toFixed();
		if (fixedDate != this.fixed) {
			fail("Fixed date calculated as " + fixedDate + ", should be " + this.fixed);
		}
	}
	
	@Test
	void testFromFixed() {
		CQIslamicCalendar moDay = CQIslamicCalendar.fromFixed(this.fixed);
		if (!moDay.equals(this.cal)) {
			fail("Fixed day "+ this.fixed + " is calculated as " + moDay + ", should be "+ this.cal);
		}
	}

}
