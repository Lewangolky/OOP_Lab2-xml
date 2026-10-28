package com.oop.knives;

import com.oop.knives.model.Blade;
import com.oop.knives.model.BladeMaterial;
import com.oop.knives.model.Handle;
import com.oop.knives.model.HandleMaterial;
import com.oop.knives.model.Handy;
import com.oop.knives.model.Knife;
import com.oop.knives.model.Visual;
import com.oop.knives.model.WeaponType;
import com.oop.knives.model.Wood;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Shared fixtures for the tests.
 */
public final class TestData {

    public static final URL KNIVES_XML = resource("/knives.xml");
    public static final URL KNIVES_XSD = resource("/knives.xsd");
    public static final URL KNIVES_XSL = resource("/knives-grouped.xsl");
    public static final URL INVALID_XML = resource("/invalid-knives.xml");

    public static final int KNIVES_IN_FILE = 8;

    private TestData() {
    }

    /** First knife of {@code knives.xml}. */
    public static Knife ukrainianDagger() {
        return knife("k1", WeaponType.DAGGER, "Ukraine", 25, true);
    }

    /** Third knife of {@code knives.xml}. */
    public static Knife germanKnife() {
        Visual visual = new Visual(new Blade(12, 20), BladeMaterial.STEEL,
                new Handle(HandleMaterial.PLASTIC, null), false);
        return new Knife("k3", WeaponType.KNIFE, Handy.ONE_HANDED, "Germany", visual, false);
    }

    public static Knife knife(String id, WeaponType type, String origin, int bladeLength, boolean collectible) {
        Visual visual = new Visual(new Blade(bladeLength, 35), BladeMaterial.STEEL,
                new Handle(HandleMaterial.WOODEN, Wood.OAK), true);
        return new Knife(id, type, Handy.ONE_HANDED, origin, visual, collectible);
    }

    public static InputStream xml(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    private static URL resource(String name) {
        return TestData.class.getResource(name);
    }
}
