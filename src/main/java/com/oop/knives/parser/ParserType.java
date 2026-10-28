package com.oop.knives.parser;

import java.util.function.Supplier;

public enum ParserType {
    DOM(DomKnivesParser::new),
    SAX(SaxKnivesParser::new),
    STAX(StaxKnivesParser::new);

    private final Supplier<KnivesParser> factory;

    ParserType(Supplier<KnivesParser> factory) {
        this.factory = factory;
    }

    public KnivesParser createParser() {
        return factory.get();
    }
}
