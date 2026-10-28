package com.oop.knives.model;

import java.util.Arrays;
import java.util.Locale;

/**
 * Enum whose constants are written in XML as lower-case, hyphen-separated
 * names: {@code DAMASCUS_STEEL} is {@code "damascus-steel"}.
 */
public interface XmlEnum {

    String name();

    default String value() {
        return name().toLowerCase(Locale.ROOT).replace('_', '-');
    }

    static <E extends Enum<E> & XmlEnum> E fromValue(Class<E> type, String value) {
        return Arrays.stream(type.getEnumConstants())
                .filter(constant -> constant.value().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown " + type.getSimpleName() + " value: '" + value + "'"));
    }
}
