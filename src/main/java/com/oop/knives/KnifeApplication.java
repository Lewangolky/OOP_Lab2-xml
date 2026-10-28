package com.oop.knives;

import com.oop.knives.model.Knife;
import com.oop.knives.parser.KnifeParseException;
import com.oop.knives.parser.ParserType;
import com.oop.knives.sort.KnifeSort;
import com.oop.knives.transform.XslTransformer;
import com.oop.knives.validation.XmlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Validates {@code knives.xml}, parses it with every parser, prints it in several
 * orders and regroups it by weapon type with {@code knives-grouped.xsl}.
 */
public class KnifeApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(KnifeApplication.class);

    static final Path DEFAULT_OUTPUT = Path.of("target", "knives-grouped.xml");

    private final URL xml;
    private final URL xsd;
    private final URL xsl;

    public KnifeApplication(URL xml, URL xsd, URL xsl) {
        this.xml = xml;
        this.xsd = xsd;
        this.xsl = xsl;
    }

    public static void main(String[] args) throws Exception {
        Path output = args.length > 0 ? Path.of(args[0]) : DEFAULT_OUTPUT;
        KnifeApplication application = new KnifeApplication(
                KnifeApplication.class.getResource("/knives.xml"),
                KnifeApplication.class.getResource("/knives.xsd"),
                KnifeApplication.class.getResource("/knives-grouped.xsl"));
        application.run(output);
    }

    /**
     * @return {@code false} if the document is not valid and nothing else was done
     */
    public boolean run(Path output) throws IOException, SAXException, KnifeParseException, TransformerException {
        if (!validate()) {
            LOGGER.error("{} does not match {}", xml, xsd);
            return false;
        }
        LOGGER.info("{} is valid against {}", xml, xsd);

        Map<ParserType, List<Knife>> results = parseWithAllParsers();
        List<Knife> knives = results.get(ParserType.DOM);
        boolean consistent = results.values().stream().allMatch(knives::equals);
        LOGGER.info("All parsers returned the same {} knives: {}", knives.size(), consistent);

        for (KnifeSort sort : KnifeSort.values()) {
            List<Knife> sorted = new ArrayList<>(knives);
            sorted.sort(sort);
            LOGGER.info("Sorted {}:", sort);
            sorted.forEach(knife -> LOGGER.info("    {}", knife));
        }

        transform(output);
        LOGGER.info("Knives grouped by type written to {}", output.toAbsolutePath());
        return true;
    }

    private boolean validate() throws IOException, SAXException {
        XmlValidator validator = new XmlValidator(xsd);
        try (InputStream input = xml.openStream()) {
            return validator.isValid(input);
        }
    }

    private Map<ParserType, List<Knife>> parseWithAllParsers() throws IOException, KnifeParseException {
        Map<ParserType, List<Knife>> results = new EnumMap<>(ParserType.class);
        for (ParserType type : ParserType.values()) {
            try (InputStream input = xml.openStream()) {
                results.put(type, type.createParser().parse(input));
            }
            LOGGER.info("{} parser read {} knives", type, results.get(type).size());
        }
        return results;
    }

    private void transform(Path output) throws IOException, TransformerException {
        Path parent = output.toAbsolutePath().getParent();
        Files.createDirectories(parent);
        XslTransformer transformer = new XslTransformer(xsl);
        try (InputStream input = xml.openStream(); OutputStream out = Files.newOutputStream(output)) {
            transformer.transform(input, out);
        }
    }
}
