package com.me.ttrpg.tracker.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AggColumn {

	AggColumnType type() default AggColumnType.STRING;

	String name();

	String role() default "";

	public static enum AggColumnType {
		STRING(o -> "'" + String.valueOf(o).replace("'", "\\'") + "'"),
		NUMBER(String::valueOf),
		DATE(AggColumnType::massageDate);

		private AggColumnType(final Function<Object, String> massageFunc) {
			this.massageFunc = massageFunc;
		}

		private final Function<Object, String> massageFunc;

		public String getDataType() {
			return toString().toLowerCase();
		}

		public String massageValue(final Object obj) {
			return massageFunc.apply(obj);
		}

		private static String massageDate(final Object obj) {
			int year, month, day;
			if(obj instanceof LocalDate) {
				final LocalDate date = (LocalDate) obj;
				year = date.getYear();
				month = date.getMonthValue() - 1;
				day = date.getDayOfMonth();
			} else if(obj instanceof Date) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime((Date) obj);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				day = cal.get(Calendar.DAY_OF_MONTH);
			} else {
				throw new IllegalArgumentException("not a date " + obj);
			}
			return "new Date(" + year + ", " + month + ", " + day + ")";
		}
	}

}
