package com.oop.knives.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates XML documents against an XSD schema.
 */
public class XmlValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlValidator.class);

    private final Schema schema;

    public XmlValidator(URL schemaLocation) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        this.schema = factory.newSchema(schemaLocation);
        LOGGER.debug("Schema loaded from {}", schemaLocation);
    }

    /**
     * @return all validation errors; an empty list means the document is valid
     */
    public List<String> validate(InputStream xml) throws IOException {
        List<String> errors = new ArrayList<>();
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new CollectingErrorHandler(errors));
        try {
            validator.validate(new StreamSource(xml));
        } catch (SAXException e) {
            // a fatal (well-formedness) error stops validation; it is already in the list
            LOGGER.debug("Validation stopped by a fatal error", e);
        }
        errors.forEach(error -> LOGGER.warn("Validation error: {}", error));
        return errors;
    }

    public boolean isValid(InputStream xml) throws IOException {
        return validate(xml).isEmpty();
    }

    private static final class CollectingErrorHandler extends DefaultHandler {

        private final List<String> errors;

        private CollectingErrorHandler(List<String> errors) {
            this.errors = errors;
        }

        @Override
        public void error(SAXParseException e) {
            errors.add(describe(e));
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            errors.add(describe(e));
            throw e;
        }

        private static String describe(SAXParseException e) {
            return "line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage();
        }
    }
}
