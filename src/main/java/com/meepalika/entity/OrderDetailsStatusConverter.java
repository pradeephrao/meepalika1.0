package com.meepalika.entity;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderDetailsStatusConverter implements AttributeConverter<ORDERDETAILSTATUS, String> {

	@Override
	public String convertToDatabaseColumn(ORDERDETAILSTATUS status) {
		if (status == null) {
			return null;
		}
		return status.geStatus();
	}

	@Override
	public ORDERDETAILSTATUS convertToEntityAttribute(String code) {
		if (code == null) {
			return null;
		}

		return Stream.of(ORDERDETAILSTATUS.values())
				.filter(c -> c.geStatus().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}