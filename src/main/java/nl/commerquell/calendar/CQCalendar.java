package nl.commerquell.calendar;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BiPredicate;
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
	protected StartOfDay startOfDay;
	
	protected CQCalendar() {
		this.startOfDay = StartOfDay.MIDNIGHT;
	}
	
	public abstract int toFixed();

	public abstract boolean inLeapYear();
	public abstract int epoch();
	public abstract boolean isValid();
	public abstract int getCalendarType();
	
	protected boolean testOneInt(int n, Predicate<Integer> p) {
		return p.test(n);
	}
	
	protected boolean testTwoInts(int m, int n, BiPredicate<Integer, Integer> bp) {
		return bp.test(m, n);
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
	
	public static final int diff(CQCalendar ldate, CQCalendar rdate) {
		return rdate.toFixed() - ldate.toFixed();
	}
	
	public static boolean isLeapYear(Class<? extends CQCalendar> calendar, int year) {
		Cycles c = calendar.getAnnotation(Cycles.class);
		if (c == null) {
			throw new IllegalArgumentException("No cycles defined on class " + calendar.getName());
		}
		int cycles = c.cycles().length;
		if (c.yearIndex() < 0 || c.yearIndex() >= cycles) {
			return false;
		}
		int[] sample = new int[cycles];
		for (int i = 0; i < cycles; i++) {
			sample[i] = 1;
		}
		sample[c.yearIndex()] = year;
		RuntimeException rx = null;
		CQCalendar sampleDate = null;
		try {
			Constructor<?> cons = null;
			switch (cycles) {
			case 2:
				cons = calendar.getDeclaredConstructor(int.class, int.class);
				sampleDate = (CQCalendar) cons.newInstance(sample[0], sample[1]);
				break;
			case 3:
				cons = calendar.getDeclaredConstructor(int.class, int.class, int.class);
				sampleDate = (CQCalendar) cons.newInstance(sample[0], sample[1], sample[2]);
				break;
			case 4:
				cons = calendar.getDeclaredConstructor(int.class, int.class, int.class, int.class);
				sampleDate = (CQCalendar) cons.newInstance(sample[0], sample[1], sample[2], sample[3]);
				break;
			case 5:
				cons = calendar.getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class);
				sampleDate = (CQCalendar) cons.newInstance(sample[0], sample[1], sample[2], sample[3], sample[4]);
				break;
			default:
				rx = new RuntimeException("No constructor with " + cycles + " int parameters available for " + calendar.getName());
				break;
			}
		} catch (Exception x) {
			if (!(x instanceof RuntimeException)) {
				rx = new RuntimeException(x);
			} else {
				rx = (RuntimeException) x;
			}
		}
		if (rx != null) {
			throw rx;
		}
		return sampleDate.inLeapYear();
	}
	
	public final int dayOfCalendar() {
		return toFixed() + 1 - epoch();
	}
	
	protected static final int ceiling(double d) {
		BigDecimal bd = BigDecimal.valueOf(d);
		boolean fractional = false;
		BigInteger bi = bd.toBigInteger();
		try {
			@SuppressWarnings("unused")
			BigInteger bie = bd.toBigIntegerExact();
		} catch (ArithmeticException ax) {
			fractional = true;
		}
		int retval = bi.intValue() + (fractional ? 1 : 0);
	 	return retval;
	}
	
	public final StartOfDay getStartOfDay() {
		return this.startOfDay;
	}
}
