package com.oop.knives.parser;

import com.oop.knives.model.XmlEnum;

/**
 * Elements of {@code knives.xsd}.
 */
enum KnifeTag implements XmlEnum {
    KNIVES, KNIFE, HANDY, ORIGIN, VISUAL, BLADE, LENGTH, WIDTH,
    BLADE_MATERIAL, HANDLE, WOODEN, PLASTIC, METAL, FULLER, COLLECTIBLE;

    static KnifeTag of(String name) {
        return XmlEnum.fromValue(KnifeTag.class, name);
    }
}
