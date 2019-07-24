package nl.commerquell.calendar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CQGregorianCalendarTest {
	private CQGregorianCalendar testDate;
	private int fixedValue;
	private int weekNumber;
	private int dayOfCal;
	
	@BeforeEach
	void setUp() throws Exception {
		this.testDate = new CQGregorianCalendar(24, 7, -586);
		this.fixedValue = -214193;
		this.weekNumber = 29;
		this.dayOfCal = -214193;
	}

	@Test
	void testToFixed() {
		int fixed = testDate.toFixed();
		if (fixed != fixedValue) {
			fail("Should be " + fixedValue + ", not " + fixed);
		}
	}

	@Test
	void testInLeapYear() {
		if (this.testDate.inLeapYear()) {
			fail(this.testDate.get(CQGregorianCalendar.YEAR) + " unjustly considered a leap year");
		}
	}

	@Test
	void testEpoch() {
		System.out.println("The epoch is hard-coded, no further testing required");
	}

	@Test
	void testIsValid() {
		if (!this.testDate.isValid()) {
			fail("Crap. " + testDate + " is valid");
		}
	}

	@Test
	void testGetWeekNumber() {
		int weekNo = this.testDate.getWeekNumber();
		if (weekNo != this.weekNumber) {
			fail("Expected week number: 29, calculated: " + weekNo);
		}
	}
	
	@Test
	void testDayOfCalendar() {
		int calcDay = testDate.dayOfCalendar();
		if (calcDay != dayOfCal) {
			fail("Calculated day# of calendar: " + calcDay + ",  should be " + this.dayOfCal);
		}
	}

	@Test
	void test() {
		testToFixed();
		testFromFixed();
		testEpoch();
		testInLeapYear();
		testGetWeekNumber();
		testDayOfCalendar();
	}
	
	@Test
	void testFromFixed() {
		CQGregorianCalendar aDate = CQGregorianCalendar.fromFixed(fixedValue);
		if (!aDate.equals(this.testDate)) {
			fail("Is " + aDate + ", should be " + testDate);
		}
	}

}
