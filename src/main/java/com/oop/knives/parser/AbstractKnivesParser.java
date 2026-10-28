package com.oop.knives.parser;

import com.oop.knives.model.Knife;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * Common part of all parsers: error handling and logging. Subclasses only feed
 * XML events into the {@link KnifeAssembler}.
 */
abstract class AbstractKnivesParser implements KnivesParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKnivesParser.class);

    @Override
    public final List<Knife> parse(InputStream input) throws KnifeParseException {
        String parserName = getClass().getSimpleName();
        LOGGER.debug("{}: parsing started", parserName);
        KnifeAssembler assembler = new KnifeAssembler();
        try {
            read(input, assembler);
        } catch (Exception e) {
            LOGGER.error("{}: parsing failed - {}", parserName, e.getMessage());
            throw new KnifeParseException(parserName + " could not parse the document", e);
        }
        List<Knife> knives = assembler.getKnives();
        LOGGER.debug("{}: parsed {} knives", parserName, knives.size());
        return knives;
    }

    protected abstract void read(InputStream input, KnifeAssembler assembler) throws Exception;
}
