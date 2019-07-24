package nl.commerquell.calendar.renditions;

import java.io.Serializable;
import java.math.BigDecimal;

import nl.commerquell.calendar.CQCalendar;

public class CalendarRendition implements Serializable {
	private int type;
	private int[] cycleValues;
	private int fixedDate;
	private BigDecimal julianDate;
	private int dayOfCalendar;
	private int weekNo = -1;
	private int dayOfWeek;
	
	public CalendarRendition(CQCalendar calendar) {
		initFromCalendar(calendar);
	}
	
	private void initFromCalendar(CQCalendar calendar) {
		this.type = calendar.getCalendarType();
		this.cycleValues = calendar.getCycleValues();
		this.fixedDate = calendar.toFixed();
		this.dayOfWeek = this.fixedDate % 7;
		this.weekNo = calendar.getWeekNumber();
		BigDecimal bd = BigDecimal.valueOf(this.fixedDate * 1L);
		this.julianDate = bd.add(BigDecimal.valueOf(17214245L, 1));
		this.dayOfCalendar = calendar.dayOfCalendar();
	}
	
	public int getType() {
		return type;
	}
	
	public int[] getCycleValues() {
		int[] retval = new int[this.cycleValues.length];
		System.arraycopy(this.cycleValues, 0, retval, 0, this.cycleValues.length);
		return retval;
	}

	public int getFixedDate() {
		return fixedDate;
	}
	
	public BigDecimal getJulianDate() {
		return julianDate;
	}
	
	public int getWeekNumber() {
		return weekNo;
	}
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	
	public int getDayOfCalendar() {
		return dayOfCalendar;
	}
}
