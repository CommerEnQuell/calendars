package nl.commerquell.calendar;

@Cycles(months = {"Muharram", "Safar", "Rabi` I", "Rabi` II", "Jumada I", "Jumada II", "Rajab", "Sha`ban", "Ramadan", "Shawwal", "Dhu al-Qa`da", "Dhu al-Hijja"})
public class CQIslamicCalendar extends CQCalendar {
	private static final int EPOCH = 227015;
	private static final int MONTHS = 12;

	private CQIslamicCalendar() {
		super();
		this.cycleValues = new int[3];
		this.startOfDay = StartOfDay.SUNSET;
	}
	
	public CQIslamicCalendar(int day, int month, int year) {
		this(day, month, year, true);
	}
	
	private CQIslamicCalendar(int day, int month, int year, boolean validityCheck) {
		this();
		this.cycleValues[0] = day;
		this.cycleValues[1] = month;
		this.cycleValues[2] = year;
		if (validityCheck) {
			assertIsValid();
		}
	}
	
	@Override
	public int toFixed() {
		int m = ceiling(29.5d * (cycleValues[1]- 1));
		int y = (3 + 11 * cycleValues[2]) / 30;
		int retval = cycleValues[0] + m + (cycleValues[2] - 1) * 354 + y + EPOCH - 1;
		return retval;
	}

	@Override
	public boolean inLeapYear() {
		return testOneInt(cycleValues[2], n -> (11 * n + 14) % 30 < 11);	// Years 2, 5, 7, 10, 13, 16, 18, 21, 24, 26 and 29 of a 30-year cycle
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
		boolean valid = (month >= 1 && month <= MONTHS);
		valid = valid && day >= 1;
		valid = valid && testTwoInts(day, month, (d, m) -> {
			int monthLength = 29 + m % 2;
			if (m == MONTHS && isLeapYear(CQIslamicCalendar.class, year)) {
				monthLength++;
			}
			return (d <= monthLength);
		});
		return valid;
	}

	@Override
	public int getCalendarType() {
		return 4;
	}

	public static CQIslamicCalendar fromFixed(int fixed) {
		int year = (30 * (fixed - EPOCH) + 10646) / 10631;
		double dm =  (fixed - (29 + new CQIslamicCalendar(1, 1, year, false).toFixed())) / 29.5;
		int m = 1 + ceiling(dm);
		int month = (m > 12 ? 12 : m);
		int day = 1 + fixed - new CQIslamicCalendar(1, month, year, false).toFixed();
		return new CQIslamicCalendar(day, month, year);
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
