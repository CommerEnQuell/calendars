package nl.commerquell.calendar;

public class CQJulianCalendar extends CQCalendar {
	public static final int EPOCH = -1;
	public static final int MONTHS = 12;
	public static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	public static final int[] DAY_OF_YEAR = {306, 337, 0, 31, 61, 92, 122, 153, 184, 214, 245, 275};
	
	
	private CQJulianCalendar() {
		super();
		this.cycleValues = new int[3];
	}
	
	public CQJulianCalendar(int day, int month, int year) {
		this();
		this.cycleValues[0] = day;
		this.cycleValues[1] = month;
		this.cycleValues[2] = year;
		assertIsValid();
	}

	@Override
	public int toFixed() {
		int year = this.cycleValues[2];
		int month = this.cycleValues[1];
		int day = this.cycleValues[0];
		if (year < 0) {
			year++;
		}
		if (month < 3) {
			year--;
		}
		int leapDays = (year - (year < 0 ? 3 : 0)) / 4;
		int julDate = 365 * year + leapDays;
		int idx = month - 1;
		julDate += DAY_OF_YEAR[idx];
		julDate += day;
		julDate -= (DAY_OF_YEAR[0] + 1 - EPOCH);
		
		return julDate;
	}

	@Override
	public boolean inLeapYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int epoch() {
		return EPOCH;
	}

	@Override
	public boolean isValid() {
		int day = cycleValues[0];
		int month = cycleValues[1];
		int year = cycleValues[2];
		if (year == 0) {
			return false;
		}
		if (month < 1 || month > MONTHS) {
			return false;
		}
		if (year < 0) {
			year++;
		}
		boolean isLeapYear = (year % 4 == 0);
		int days = DAYS_PER_MONTH[month - 1];
		if (isLeapYear && month == 2) {
			days++;
		}
		if (day < 1 || day > days) {
			return false;
		}
		return true;
	}
	
	public static CQJulianCalendar fromFixed(int fixed) {
		int theFixed = fixed + 1 - EPOCH;
		int periods = 0;
		while (theFixed < 0) {
			theFixed += 102270;
			periods += 10;
		}
		theFixed += DAY_OF_YEAR[0];
		int leapDays = theFixed / 1461;
		theFixed -= leapDays;
		int year = (theFixed - 1) / 365;
		theFixed -= (365 * year);
		int month = 3;
		int idx = 2;
		while (theFixed > DAY_OF_YEAR[idx]) {
			month++;
			idx++;
			if (idx >= MONTHS) {
				month -= MONTHS;
				idx -= MONTHS;
			}
			if (idx == 2) {
				break;
			}
		}
		month = (idx > 0 ? idx : MONTHS);
		idx = month - 1;
		theFixed -= DAY_OF_YEAR[idx];
		int day = theFixed;
		year -= (28 * periods);
		if (year < 1) {
			year--;
		}
		
		return new CQJulianCalendar(day, month, year);
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
