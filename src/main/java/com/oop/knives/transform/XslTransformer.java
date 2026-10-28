package com.oop.knives.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Applies a compiled XSL stylesheet to XML documents.
 */
public class XslTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(XslTransformer.class);

    private final Templates templates;

    public XslTransformer(URL stylesheet) throws TransformerConfigurationException {
        this.templates = TransformerFactory.newInstance().newTemplates(new StreamSource(stylesheet.toExternalForm()));
        LOGGER.debug("Stylesheet compiled from {}", stylesheet);
    }

    public void transform(InputStream xml, OutputStream output) throws TransformerException {
        templates.newTransformer().transform(new StreamSource(xml), new StreamResult(output));
        LOGGER.debug("Transformation finished");
    }
}
