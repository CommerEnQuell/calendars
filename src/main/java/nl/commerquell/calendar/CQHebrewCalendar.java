package nl.commerquell.calendar;

import nl.commerquell.calendar.error.BogusDateException;

@Cycles(months= {"Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Tevet", "Shevat", "Adar I", "Adar II"})
public class CQHebrewCalendar extends CQCalendar {
	public static final int EPOCH = -1373427;
	public static final int MONTHS = 13;
	public static final int[] DAYS_PER_MONTH = {30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 29};	// Months 7-13 followed by 1-6
	
	private boolean isChecked;
	
	private CQHebrewCalendar() {
		super();
		this.cycleValues = new int[3];
	}
	
	public CQHebrewCalendar(int day, int month, int year) {
		this(day, month, year, true);
	}
	
	private CQHebrewCalendar(int day, int month, int year, boolean validityCheck) {
		this();
		this.cycleValues[0] = day;
		this.cycleValues[1] = month;
		this.cycleValues[2] = year;
		this.isChecked = validityCheck;
		if (validityCheck) {
			assertIsValid();
		}
	}

	@Override
	public int toFixed() {
		int[] daysInMonth = getMonthLengths();
		int retval = EPOCH;
		retval += elapsedDays(cycleValues[2]);
		retval += newYearDelay(cycleValues[2]);
		retval += cycleValues[0];
		retval--;
		int idx = cycleValues[1] - 7;
		if (idx < 0) {
			idx += MONTHS;
		}
		for (int i = 0; i < idx; i++) {
			retval += daysInMonth[i];
		}
		return retval;
	}

	@Override
	public boolean inLeapYear() {
		return testLeap(cycleValues[2], n -> (7 * n + 1) % 19 < 7);
	}

	@Override
	public int epoch() {
		return EPOCH;
	}

	@Override
	public boolean isValid() {
		int months = MONTHS - (inLeapYear() ? 0 : 1);
		if (cycleValues[1] < 1 || cycleValues[1] > months) {
			return false;
		}
		int[] daysPerMonth = new int[months];
		System.arraycopy(DAYS_PER_MONTH, 0, daysPerMonth, 0, months);
		if (longHeshvan()) {
			daysPerMonth[7] = 30;
		} else if (shortKislev()) {
			daysPerMonth[8] = 29;
		}
		if (cycleValues[0] < 1 || cycleValues[0] > daysPerMonth[cycleValues[1] - 1]) {
			return false;
		}
		return true;
	}
	
	public boolean longHeshvan() {
		return testLeap(lengthOfYear(cycleValues[2]), l -> l % 10 == 5);
	}
	
	public boolean shortKislev() {
		return testLeap(lengthOfYear(cycleValues[2]), l -> l % 10 == 3);
	}
	
	private int[] getMonthLengths() {
		int[] daysPerMonth = new int[MONTHS];
		System.arraycopy(DAYS_PER_MONTH, 0, daysPerMonth, 0, MONTHS);
		if (!inLeapYear()) {
			daysPerMonth[6] = 0;
		} else {
			daysPerMonth[5] = 30;
		}
		if (isChecked) {		// Needed to avoid stack overflow caused by chains of invocations.
			if (longHeshvan()) {
				daysPerMonth[1] = 30;
			} else if (shortKislev()) {	// Long Cheshvan and short Kislev are mutually exclusive
				daysPerMonth[2] = 29;
			}
		}
		
		return daysPerMonth;
	}
	
	private static int newYearDelay(int thisYear) {
		int lastYear = thisYear - 1;
		int nextYear = thisYear + 1;
		int thisLength = elapsedDays(nextYear) - elapsedDays(thisYear);
		int lastLength = elapsedDays(thisYear) - elapsedDays(lastYear);
		int retval = 0;
		if (thisLength == 356) {
			retval = 2;
		} else if (lastLength == 382) {
			retval = 1;
		}
		return retval;
	}
	
	private static int elapsedDays(int year) {
		int monthsElapsed = (235 * year - 234) / 19;
		int partsElapsed = 12084 + 13753 * monthsElapsed;
		int day = 29 * monthsElapsed + (partsElapsed / 25920);
		int r = (3 * (day + 1)) % 7;
		if (r < 3) {
			day++;
		}
		return day;
	}
	
	public static int lengthOfYear(int year) {
		int nextYear = year + 1;
		int nextNewYear = new CQHebrewCalendar(1, 7, nextYear, false).toFixed();
		int thisNewYear = new CQHebrewCalendar(1, 7, year, false).toFixed();
		return nextNewYear - thisNewYear;
	}

	public static CQHebrewCalendar fromFixed(int fixed) {
		if (fixed < EPOCH) {
			throw new BogusDateException("This calendar does not allow dates before its starting point");
		}
		
		long work = 98496L * Long.valueOf(fixed - EPOCH).longValue() / 35975351L;
		int approx = Long.valueOf(work).intValue();
		int year = approx;
		CQHebrewCalendar newYear = new CQHebrewCalendar(1, 7, year, false);
		while (fixed >= newYear.toFixed()) {
			year++;
			newYear = new CQHebrewCalendar(1, 7, year, false);
		}
		year--;
		newYear = new CQHebrewCalendar(1, 7, year);
		int[] monthLengths = newYear.getMonthLengths();
		int day = fixed + 1 - newYear.toFixed();
		int idx = 0;
		while (day > monthLengths[idx]) {
			day -= monthLengths[idx];
			idx++;
			if (idx >= MONTHS) {
				break;
			}
		}
		int month = (idx > 6 ? idx - 6 : idx + 7);
		
		return new CQHebrewCalendar(day, month, year);
	}
	
	@Override
	public int getCalendarType() {
		return 3;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(getClass().getName());
		buf.append(" [");
		buf.append(format(cycleValues[0], 2, '0')).append(".");
		buf.append(format(cycleValues[1], 2, '0')).append(".");
		buf.append(cycleValues[2]);
		buf.append("]");
		
		return buf.toString();
	}
}
