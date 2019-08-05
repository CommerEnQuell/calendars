package nl.commerquell.calendar.renditions;

import java.io.Serializable;

import nl.commerquell.calendar.CQCalendar;
import nl.commerquell.calendar.Cycles;

public class CalendarTypeRendition implements Serializable {
	private int type;
	private String[] cycleNames;
	private String[] monthNames;
	private String[] daysOfWeek;
	private String startOfDay;
	
	
	public CalendarTypeRendition(CQCalendar calendar) {
		initFromCalendar(calendar);
	}
	
	private void initFromCalendar(CQCalendar calendar) {
		this.type = calendar.getCalendarType();
		this.daysOfWeek = CQCalendar.getDaysOfWeek();
		this.startOfDay = calendar.getStartOfDay().toString();
		if (calendar.getClass().isAnnotationPresent(Cycles.class)) {
			Cycles c = calendar.getClass().getAnnotation(Cycles.class);
			String[] cycleNames = c.cycles();
			this.cycleNames = new String[cycleNames.length];
			System.arraycopy(cycleNames, 0, this.cycleNames, 0, cycleNames.length);
			String[] months = c.months();
			this.monthNames = new String[months.length];
			System.arraycopy(months,  0,  this.monthNames,  0,  months.length);
		} else {
			this.cycleNames = new String[0];
			this.monthNames = new String[0];
		}
	}
	
	public int getType() {
		return type;
	}
	
	public String[] getCycleNames() {
		String[] retval = new String[this.cycleNames.length];
		System.arraycopy(this.cycleNames, 0, retval, 0, this.cycleNames.length);
		return retval;
	}
	
	public String[] getDaysOfWeek() {
		String[] retval = new String[daysOfWeek.length];
		System.arraycopy(this.daysOfWeek, 0, retval, 0, this.daysOfWeek.length);
		return retval;
	}
	
	public String[] getMonthNames() {
		String[] retval = new String[monthNames.length];
		System.arraycopy(this.monthNames, 0, retval, 0, this.monthNames.length);
		return retval;
	}
	
	public String getStartOfDay() {
		return this.startOfDay;
	}

}
