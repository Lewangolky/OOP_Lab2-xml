package com.oop.knives.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlEnumTest {

    @Test
    void valueIsLowerCaseAndHyphenated() {
        assertEquals("damascus-steel", BladeMaterial.DAMASCUS_STEEL.value());
        assertEquals("one-handed", Handy.ONE_HANDED.value());
        assertEquals("oak", Wood.OAK.value());
    }

    @Test
    void fromValueFindsConstant() {
        assertSame(Handy.TWO_HANDED, XmlEnum.fromValue(Handy.class, "two-handed"));
        assertSame(WeaponType.STILETTO, XmlEnum.fromValue(WeaponType.class, "stiletto"));
        assertSame(HandleMaterial.METAL, XmlEnum.fromValue(HandleMaterial.class, "metal"));
    }

    @Test
    void fromValueRejectsUnknownValue() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> XmlEnum.fromValue(Wood.class, "pine"));
        assertTrue(e.getMessage().contains("Wood"));
        assertTrue(e.getMessage().contains("pine"));
    }

    @Test
    void fromValueIsCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> XmlEnum.fromValue(WeaponType.class, "DAGGER"));
    }

    @ParameterizedTest
    @EnumSource(BladeMaterial.class)
    void valueRoundTrips(BladeMaterial material) {
        assertSame(material, XmlEnum.fromValue(BladeMaterial.class, material.value()));
    }
}
