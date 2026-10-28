package com.oop.knives.parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Push parser: the SAX engine calls {@link KnifeHandler} for every event.
 */
public class SaxKnivesParser extends AbstractKnivesParser {

    @Override
    protected void read(InputStream input, KnifeAssembler assembler) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.newSAXParser().parse(input, new KnifeHandler(assembler));
    }

    private static final class KnifeHandler extends DefaultHandler {

        private final KnifeAssembler assembler;

        private KnifeHandler(KnifeAssembler assembler) {
            this.assembler = assembler;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                map.put(attributes.getQName(i), attributes.getValue(i));
            }
            assembler.startElement(qName, map);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            assembler.characters(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            assembler.endElement(qName);
        }
    }
}
