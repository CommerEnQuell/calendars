package nl.commerquell.calendar;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Cycles {
	String[] cycles() default {"Day", "Month", "Year"};
	String[] months() default {"January", "February", "March", "April", "May", "June",
							   "July", "August", "September", "October", "November", "December"};
	int yearIndex() default 2;
}
