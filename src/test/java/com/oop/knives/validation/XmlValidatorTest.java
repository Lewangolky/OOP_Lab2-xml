package com.oop.knives.validation;

import com.oop.knives.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlValidatorTest {

    private XmlValidator validator;

    @BeforeEach
    void setUp() throws SAXException {
        validator = new XmlValidator(TestData.KNIVES_XSD);
    }

    @Test
    void acceptsKnivesFile() throws Exception {
        try (InputStream input = TestData.KNIVES_XML.openStream()) {
            assertTrue(validator.isValid(input));
        }
    }

    @Test
    void reportsEveryBrokenRule() throws Exception {
        List<String> errors;
        try (InputStream input = TestData.INVALID_XML.openStream()) {
            errors = validator.validate(input);
        }

        String report = String.join("\n", errors);
        assertTrue(report.contains("cvc-id.2"), "duplicate ID: " + report);
        assertTrue(report.contains("cvc-minInclusive-valid"), "blade too short: " + report);
        assertTrue(report.contains("cvc-complex-type.4"), "missing wood attribute: " + report);
        assertTrue(errors.stream().allMatch(error -> error.startsWith("line ")));
    }

    @Test
    void rejectsValueOutsideEnumeration() throws Exception {
        String xml = """
                <knives>
                    <knife id="k1" type="spear">
                        <handy>one-handed</handy>
                        <origin>Ukraine</origin>
                        <visual>
                            <blade><length>25</length><width>35</width></blade>
                            <blade-material>steel</blade-material>
                            <handle><metal/></handle>
                            <fuller>true</fuller>
                        </visual>
                        <collectible>true</collectible>
                    </knife>
                </knives>
                """;

        assertFalse(validator.isValid(TestData.xml(xml)));
    }

    @Test
    void rejectsIdThatIsNotAnXmlName() throws Exception {
        String xml = """
                <knives>
                    <knife id="1" type="dagger">
                        <handy>one-handed</handy>
                        <origin>Ukraine</origin>
                        <visual>
                            <blade><length>25</length><width>35</width></blade>
                            <blade-material>steel</blade-material>
                            <handle><plastic/></handle>
                            <fuller>true</fuller>
                        </visual>
                        <collectible>true</collectible>
                    </knife>
                </knives>
                """;

        assertFalse(validator.isValid(TestData.xml(xml)));
    }

    @Test
    void rejectsMalformedDocument() throws Exception {
        List<String> errors = validator.validate(TestData.xml("not xml at all"));

        assertEquals(1, errors.size());
    }

    @Test
    void failsOnBrokenSchema() {
        URL notASchema = TestData.KNIVES_XSL;

        assertThrows(SAXException.class, () -> new XmlValidator(notASchema));
    }
}
