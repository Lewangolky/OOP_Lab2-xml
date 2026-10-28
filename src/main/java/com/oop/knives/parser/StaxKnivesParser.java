package com.oop.knives.parser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Pull parser: the application asks {@link XMLStreamReader} for the next event.
 */
public class StaxKnivesParser extends AbstractKnivesParser {

    @Override
    protected void read(InputStream input, KnifeAssembler assembler) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT -> assembler.startElement(reader.getLocalName(), attributesOf(reader));
                    case XMLStreamConstants.CHARACTERS, XMLStreamConstants.CDATA -> assembler.characters(reader.getText());
                    case XMLStreamConstants.END_ELEMENT -> assembler.endElement(reader.getLocalName());
                    default -> {
                        // comments, processing instructions etc. are not needed
                    }
                }
            }
        } finally {
            reader.close();
        }
    }

    private static Map<String, String> attributesOf(XMLStreamReader reader) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            result.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }
        return result;
    }
}
