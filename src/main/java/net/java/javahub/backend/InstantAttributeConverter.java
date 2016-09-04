package net.java.javahub.backend;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter(autoApply = true)
public class InstantAttributeConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final Instant instant) {
        return Timestamp.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(final Timestamp timestamp) {
        return timestamp.toInstant();
    }

}

