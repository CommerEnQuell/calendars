package nl.commerquell.calendar;

import java.util.function.Predicate;

import nl.commerquell.calendar.error.BogusDateException;

public abstract class CQCalendar {
	private static final String[] daysOfWeek =
		{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	public static final int BOGUS = Integer.MIN_VALUE;
	public static final int DAY = 0;
	public static final int MONTH = 1;
	public static final int YEAR = 2;
	
	protected int[] cycleValues;
	
	protected CQCalendar() {}
	
	public abstract int toFixed();

	public abstract boolean inLeapYear();
	public abstract int epoch();
	public abstract boolean isValid();
	public abstract int getCalendarType();
	
	protected boolean testLeap(int n, Predicate<Integer> p) {
		return p.test(n);
	}
	
	public int[] getCycleValues() {
		int[] retval = new int[this.cycleValues.length];
		System.arraycopy(this.cycleValues, 0, retval, 0, this.cycleValues.length);
		return retval;
	}
	
	public int get(int idx) {
		if (idx < 0 || idx >= cycleValues.length) {
			throw new IllegalArgumentException("Index should have a value less than " + cycleValues.length + " and not less than 0");
		}
		return cycleValues[idx];
	}
	
	public final int amod(int n, int d) {
		if (d < 1) {
			throw new IllegalArgumentException("Denominator must be a positive integer");
		}
		return (n % d == 0 ? d : n % d);
	}
	
	protected String format(int num, int length, char paddingChar) {
		StringBuffer buf = new StringBuffer();
		buf.append(num);
		while(buf.length() < length ) {
			buf.insert(0, paddingChar);
		}
		return buf.toString();
	}
	
	public int dayOfWeek() {
		return amod(toFixed(), 7);
	}
	
	public int getWeekNumber() {
		return -1;
	}
	
	protected void assertIsValid() {
		if (!isValid()) {
			throw new BogusDateException("Invalid date for this calendar: " + this);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}
		CQCalendar other = (CQCalendar) o;
		if (other.cycleValues.length != this.cycleValues.length) {
			return false;
		}
		boolean isEqual = true;
		for (int i = 0; i < this.cycleValues.length; i++) {
			if (other.cycleValues[i] != this.cycleValues[i]) {
				isEqual = false;
				break;
			}
		}
		return isEqual;
		
	}
	
	public static String[] getDaysOfWeek() {
		String[] days = new String[daysOfWeek.length];
		System.arraycopy(daysOfWeek, 0, days, 0, daysOfWeek.length);
		return days;
	}
	
	public static int diff(CQCalendar ldate, CQCalendar rdate) {
		return rdate.toFixed() - ldate.toFixed();
	}
	
	public final int dayOfCalendar() {
		return toFixed() + 1 - epoch();
	}
}
