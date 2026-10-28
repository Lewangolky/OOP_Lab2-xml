package com.oop.knives.parser;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads the whole document into a DOM tree and then walks it.
 */
public class DomKnivesParser extends AbstractKnivesParser {

    @Override
    protected void read(InputStream input, KnifeAssembler assembler) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultHandler());
        Element root = builder.parse(input).getDocumentElement();
        visit(root, assembler);
    }

    private static void visit(Element element, KnifeAssembler assembler) {
        assembler.startElement(element.getTagName(), attributesOf(element));
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element childElement) {
                visit(childElement, assembler);
            } else if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                assembler.characters(child.getNodeValue());
            }
        }
        assembler.endElement(element.getTagName());
    }

    private static Map<String, String> attributesOf(Element element) {
        NamedNodeMap attributes = element.getAttributes();
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            result.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        return result;
    }
}
