package nl.commerquell.calendar;

@Cycles
public class CQGregorianCalendar extends CQCalendar {
	public static final int EPOCH = 1;
	public static final int MONTHS = 12;
	public static final int[] DAYS_OF_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	public static final int[] DAY_OF_YEAR = {306, 337, 0, 31, 61, 92, 122, 153, 184, 214, 245, 275};
	
	private CQGregorianCalendar() {
		super();
		this.cycleValues = new int[3];
	}
	
	public CQGregorianCalendar(int day, int month, int year) {
		this();
		this.cycleValues[0] = day;
		this.cycleValues[1] = month;
		this.cycleValues[2] = year;
		assertIsValid();
	}

	@Override
	public int toFixed() {
		int workYear = cycleValues[2];
		int month = cycleValues[1];
		int day = cycleValues[0];
		if (month < 3) {
			workYear--;
		}
		int retval = workYear * 365;
		boolean bc = false;
		if (workYear < 0) {
			workYear++;
			bc = true;
		}
		int leapDays = workYear / 4 - (bc ? 1 : 0);
		leapDays -= (workYear / 100);
		leapDays += (workYear / 400);
		retval += leapDays;
		retval += DAY_OF_YEAR[month - 1];
		retval += day;
		retval -= 306;

		return retval;
	}

	@Override
	public boolean inLeapYear() {
		return testOneInt(cycleValues[2], n -> (n % 4 == 0 && (n % 100 != 0 || n % 400 == 0)));
	}

	@Override
	public int epoch() {
		return EPOCH;
	}
	
	@Override
	public boolean isValid() {
		int day = cycleValues[0];
		int month = cycleValues[1];
		if (month < 1 || month > MONTHS) {
			return false;
		}
		int days = DAYS_OF_MONTH[month - 1];
		if (month == 2 && inLeapYear()) {
			days++;
		}
		if (day < 1 || day > days) {
			return false;
		}
		return true;
	}
	
	public static CQGregorianCalendar fromFixed(int fixed) {
	/*
	 * In case of pre-epoch dates, get a positive value to calculate the day and the month
	 * in an appropriate way and correct it afterwards.
	 */
		int periods = 0;
		int theFixed = fixed;
		while (theFixed < 0) {
			periods++;
			theFixed += 146097;
		}
	
	// Let's do business...
	
		int julDate = theFixed + DAY_OF_YEAR[0];
		julDate += ((julDate - 1)/ 36524);
		julDate -= ((julDate - 1)/ 146100);
		
		int year = 4 * (julDate - 1) / 1461;
		int leapDays = year / 4;
		julDate -= leapDays;
		julDate -= (365 * year);
		int month = 3;
		int idx = month - 1;
		int day = 0;
		while (julDate > DAY_OF_YEAR[idx]) {
			month++;
			idx++;
			if (idx >= DAY_OF_YEAR.length) {
				idx -= DAY_OF_YEAR.length;
			}
			if (idx == 2) {
				break;
			}
		}
		if (idx == 0) {
			idx = 12;
		}
		month = idx;
		julDate -= DAY_OF_YEAR[--idx];
		day = julDate;
		if (month < 3) {
			year++;
		}
		
		// Now correct the year for pre-epoch dates.
		
		year -= (400 * periods);
		
		return new CQGregorianCalendar(day, month, year);
	}
	
	@Override
	public int getCalendarType() {
		return 1;
	}
	
	@Override
	public int getWeekNumber() {
		int year = cycleValues[2];
		int thisDate = toFixed();
		int anchor = new CQGregorianCalendar(4, 1, year).toFixed();
		anchor -= amod(anchor, 7);
		if (thisDate < anchor) {
			year--;
			anchor = new CQGregorianCalendar(4, 1, year).toFixed();
			anchor -= amod(anchor, 7);
		}
		int dayInYear = thisDate - anchor;
		int retval = 1 + (dayInYear - 1) / 7;
		return retval;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(getClass().getName());
		buf.append(" [");
		buf.append(format(cycleValues[0], 2, '0')).append(".");
		buf.append(format(cycleValues[1], 2, '0')).append(".");
		int year = cycleValues[2];
		String era = (year > 0 ? "AD" : (year < 0 ? "BC" : ""));
		buf.append(year * (year < 0 ? -1 : 1));
		if (year != 0) {
			buf.append(" ").append(era);
		}
		buf.append("]");
		
		return buf.toString();
	}
}
