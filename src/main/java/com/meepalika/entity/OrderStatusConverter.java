package com.meepalika.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<ORDERSTATUS, String> {

	@Override
	public String convertToDatabaseColumn(ORDERSTATUS status) {
		if (status == null) {
			return null;
		}
		return status.geStatus();
	}

	@Override
	public ORDERSTATUS convertToEntityAttribute(String code) {
		if (code == null) {
			return null;
		}

		return Stream.of(ORDERSTATUS.values())
				.filter(c -> c.geStatus().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
