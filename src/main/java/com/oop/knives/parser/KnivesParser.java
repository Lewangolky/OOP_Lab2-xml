package com.oop.knives.parser;

import com.oop.knives.model.Knife;

import java.io.InputStream;
import java.util.List;

public interface KnivesParser {

    List<Knife> parse(InputStream input) throws KnifeParseException;
}
