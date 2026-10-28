package com.oop.knives.parser;

import com.oop.knives.TestData;
import com.oop.knives.model.HandleMaterial;
import com.oop.knives.model.Knife;
import com.oop.knives.model.Wood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Every test runs against the DOM, SAX and StAX parsers.
 */
class KnivesParserTest {

    private static final String KNIFE_TEMPLATE = """
            <knives>
                <!-- comment and <![CDATA[Ukr]]> sections must be handled -->
                <knife id="k1" type="%s">
                    <handy>one-handed</handy>
                    <origin><![CDATA[Ukr]]>aine</origin>
                    <visual>
                        <blade><length>%s</length><width>35</width></blade>
                        <blade-material>steel</blade-material>
                        <handle><wooden wood="oak"/></handle>
                        <fuller>1</fuller>
                    </visual>
                    <collectible>%s</collectible>
                </knife>
            </knives>
            """;

    private static List<Knife> parse(ParserType type, String content) throws KnifeParseException {
        return type.createParser().parse(TestData.xml(content));
    }

    private static String knifeXml(String type, String length, String collectible) {
        return KNIFE_TEMPLATE.formatted(type, length, collectible);
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesAllKnivesFromFile(ParserType type) throws Exception {
        List<Knife> knives;
        try (InputStream input = TestData.KNIVES_XML.openStream()) {
            knives = type.createParser().parse(input);
        }

        assertEquals(TestData.KNIVES_IN_FILE, knives.size());
        assertEquals(TestData.ukrainianDagger(), knives.get(0));
        assertEquals(TestData.germanKnife(), knives.get(2));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void readsHandleKinds(ParserType type) throws Exception {
        List<Knife> knives;
        try (InputStream input = TestData.KNIVES_XML.openStream()) {
            knives = type.createParser().parse(input);
        }

        assertEquals(HandleMaterial.WOODEN, knives.get(3).getVisual().getHandle().getMaterial());
        assertEquals(Wood.ASH, knives.get(3).getVisual().getHandle().getWood());
        assertEquals(HandleMaterial.METAL, knives.get(1).getVisual().getHandle().getMaterial());
        assertNull(knives.get(1).getVisual().getHandle().getWood());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void handlesCommentsCdataAndNumericBooleans(ParserType type) throws Exception {
        Knife knife = parse(type, knifeXml("dagger", "25", "1")).get(0);

        assertEquals(TestData.ukrainianDagger(), knife);
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void readsFalseAsZero(ParserType type) throws Exception {
        Knife knife = parse(type, knifeXml("dagger", "25", "0")).get(0);

        assertFalse(knife.isCollectible());
        assertTrue(knife.getVisual().hasFuller());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void rejectsMalformedXml(ParserType type) {
        assertThrows(KnifeParseException.class, () -> parse(type, "<knives><knife></knives>"));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void rejectsUnknownEnumValue(ParserType type) {
        KnifeParseException e = assertThrows(KnifeParseException.class,
                () -> parse(type, knifeXml("spear", "25", "true")));

        assertInstanceOf(IllegalArgumentException.class, e.getCause());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void rejectsNonNumericLength(ParserType type) {
        assertThrows(KnifeParseException.class, () -> parse(type, knifeXml("dagger", "long", "true")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void rejectsInvalidBoolean(ParserType type) {
        assertThrows(KnifeParseException.class, () -> parse(type, knifeXml("dagger", "25", "yes")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void rejectsUnknownElement(ParserType type) {
        assertThrows(KnifeParseException.class, () -> parse(type, "<knives><spear/></knives>"));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void rejectsElementOutsideOfItsParent(ParserType type) {
        KnifeParseException e = assertThrows(KnifeParseException.class,
                () -> parse(type, "<knives><length>25</length></knives>"));

        assertInstanceOf(IllegalStateException.class, e.getCause());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void returnsEmptyListForEmptyRoot(ParserType type) throws Exception {
        assertTrue(parse(type, "<knives/>").isEmpty());
    }

    @Test
    void allParsersReturnTheSameKnives() throws Exception {
        List<Knife> expected = null;
        for (ParserType type : ParserType.values()) {
            List<Knife> knives;
            try (InputStream input = TestData.KNIVES_XML.openStream()) {
                knives = type.createParser().parse(input);
            }
            if (expected == null) {
                expected = knives;
            }
            assertEquals(expected, knives, type + " differs");
        }
    }

    @Test
    void factoryCreatesMatchingParser() {
        assertInstanceOf(DomKnivesParser.class, ParserType.DOM.createParser());
        assertInstanceOf(SaxKnivesParser.class, ParserType.SAX.createParser());
        assertInstanceOf(StaxKnivesParser.class, ParserType.STAX.createParser());
    }

    @Test
    void wrapsReadErrors() {
        InputStream failing = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("disk failure");
            }
        };

        assertThrows(KnifeParseException.class, () -> new SaxKnivesParser().parse(failing));
    }
}
