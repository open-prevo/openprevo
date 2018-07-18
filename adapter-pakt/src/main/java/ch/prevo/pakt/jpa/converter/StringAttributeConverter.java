package ch.prevo.pakt.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StringAttributeConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String string) {
		return string;
	}

	@Override
	public String convertToEntityAttribute(String string) {
		return string == null ? string : string.trim();
	}

}
