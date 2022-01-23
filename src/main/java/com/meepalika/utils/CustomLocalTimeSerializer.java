package com.meepalika.utils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomLocalTimeSerializer extends JsonSerializer<LocalTime> {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	@Override

	public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers)

			throws IOException {

		if (value == null) {

			gen.writeNull();

		} else {

			gen.writeString(formatter.format(value));

		}

	}

}
