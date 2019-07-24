package nl.commerquell.calendar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CQCalendarPicker {
	
	public static CQCalendar getCalendarByType(int type, int... cycles) throws Exception {
		Class<? extends CQCalendar> theClass = getCalendarClass(type);
		
		int cycleCount = cycles.length;
		Constructor<?>[] constructors = theClass.getConstructors();
		Constructor<?> theConstructor = null;
		for (Constructor<?> c : constructors) {
			Class<?>[] paramTypes = c.getParameterTypes();
			if (paramTypes.length != cycleCount) {
				continue;
			}
			boolean found = true;
			for (Class<?> paramType : paramTypes) {
				if (paramType != int.class) {
					found = false;
					break;
				}
			}
			if (found) {
				theConstructor = c;
				break;
			}
		}
		if (theConstructor == null) {
			throw new RuntimeException(theClass.getName() + " does not have a constructor with " + cycleCount + " int parameters");
		}
		
		CQCalendar retval = null;
		switch (cycleCount) {
		case 2:		// Tzolkin, Haab
			retval = (CQCalendar) theConstructor.newInstance(cycles[0], cycles[1]);
			break;
		case 3:		// Most calendars
			retval = (CQCalendar) theConstructor.newInstance(cycles[0], cycles[1], cycles[2]);
			break;
		case 4:		// Chinese
			retval = (CQCalendar) theConstructor.newInstance(cycles[0], cycles[1], cycles[2], cycles[3]);
			break;
		case 5:	// Long Count
			retval = (CQCalendar) theConstructor.newInstance(cycles[0], cycles[1], cycles[2], cycles[3], cycles[4]);
			break;
			
		}

		return retval;
	}
	
	public static CQCalendar getCalendarFromFixed(int type, int fixed) throws Exception {
		Class<? extends CQCalendar> theClass = getCalendarClass(type);
		Method m = theClass.getMethod("fromFixed", int.class);
		CQCalendar retval = (CQCalendar) m.invoke(null, fixed);
		
		return retval;
	}
	
	private static Class<? extends CQCalendar> getCalendarClass(int type) {
		Class<? extends CQCalendar> retval = null;
		switch (type) {
		case 1:
			retval = CQGregorianCalendar.class;
			break;
		case 2:
			retval = CQJulianCalendar.class;
			break;
		case 3:
			retval = CQHebrewCalendar.class;
			break;
		default:
			throw new IllegalArgumentException("No existing calendar of type " + type + " available");
		}
		
		return retval;
	
	}
	
}
