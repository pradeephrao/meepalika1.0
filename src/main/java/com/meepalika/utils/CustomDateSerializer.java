package com.meepalika.utils;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<Date> {

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public CustomDateSerializer() {
		this(null);
	}

	public CustomDateSerializer(Class t) {
		super(t);
	}

	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		generator.writeString(formatter.format(date));
	}

	public static Date getDateOfNextWeek(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, day);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i <= 7; i++) {
			c.add(Calendar.DATE, 1);
		}
		String strdate = df.format(c.getTime());
		Date dateofWeek = Date.valueOf(strdate);
		return dateofWeek;
	}
}
