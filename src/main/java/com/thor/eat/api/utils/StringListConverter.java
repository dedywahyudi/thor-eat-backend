/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Converts the permission list to comma separated string and vice versa.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    /**
     * Converts to database column.
     * @param list the list of entities.
     * @return the comma separated string.
     */
    @Override
    public String convertToDatabaseColumn(final List<String> list) {
        return String.join("\n", list);
    }

    /**
     * Converts the string from db to entity attribute.
     * @param joined the comma sperated string.
     * @return the list of entities.
     */
    @Override
    public List<String> convertToEntityAttribute(final String joined) {
        if (joined == null || joined.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(joined.split("\n")));
    }
}
