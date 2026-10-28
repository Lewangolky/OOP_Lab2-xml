package com.oop.knives.transform;

import com.oop.knives.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XslTransformerTest {

    private static Element grouped;

    @BeforeAll
    static void transformKnivesFile() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (InputStream input = TestData.KNIVES_XML.openStream()) {
            new XslTransformer(TestData.KNIVES_XSL).transform(input, output);
        }
        grouped = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(output.toByteArray()))
                .getDocumentElement();
    }

    private static List<Element> childElements(Element parent) {
        List<Element> result = new ArrayList<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) children.item(i));
            }
        }
        return result;
    }

    @Test
    void keepsRootElement() {
        assertEquals("knives", grouped.getTagName());
    }

    @Test
    void createsOneGroupPerTypeInOrderOfFirstAppearance() {
        List<String> groups = childElements(grouped).stream().map(Element::getTagName).toList();

        assertEquals(List.of("dagger", "saber", "knife", "sword", "machete"), groups);
    }

    @Test
    void putsEveryKnifeIntoGroupOfItsType() {
        int total = 0;
        for (Element group : childElements(grouped)) {
            for (Element knife : childElements(group)) {
                assertEquals("knife", knife.getTagName());
                assertEquals(group.getTagName(), knife.getAttribute("type"));
                total++;
            }
        }
        assertEquals(TestData.KNIVES_IN_FILE, total);
    }

    @Test
    void keepsKnivesOfGroupInDocumentOrder() {
        Element daggers = childElements(grouped).get(0);
        List<String> ids = childElements(daggers).stream().map(knife -> knife.getAttribute("id")).toList();

        assertEquals(List.of("k1", "k5"), ids);
    }

    @Test
    void keepsKnifeContent() {
        Element firstDagger = childElements(childElements(grouped).get(0)).get(0);

        assertEquals("Ukraine", firstDagger.getElementsByTagName("origin").item(0).getTextContent());
        assertEquals("oak", ((Element) firstDagger.getElementsByTagName("wooden").item(0)).getAttribute("wood"));
    }

    @Test
    void failsOnMalformedInput() throws TransformerConfigurationException {
        XslTransformer transformer = new XslTransformer(TestData.KNIVES_XSL);

        assertThrows(TransformerException.class,
                () -> transformer.transform(TestData.xml("<knives>"), new ByteArrayOutputStream()));
    }

    @Test
    void failsOnInvalidStylesheet() {
        assertThrows(TransformerConfigurationException.class, () -> new XslTransformer(TestData.KNIVES_XSD));
    }
}
