package com.oop.knives;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnifeApplicationTest {

    @TempDir
    Path tempDir;

    @Test
    void writesGroupedFileForValidDocument() throws Exception {
        Path output = tempDir.resolve("nested").resolve("grouped.xml");
        KnifeApplication application = new KnifeApplication(TestData.KNIVES_XML, TestData.KNIVES_XSD, TestData.KNIVES_XSL);

        assertTrue(application.run(output));
        assertTrue(Files.readString(output).contains("<dagger>"));
    }

    @Test
    void stopsOnInvalidDocument() throws Exception {
        Path output = tempDir.resolve("grouped.xml");
        KnifeApplication application = new KnifeApplication(TestData.INVALID_XML, TestData.KNIVES_XSD, TestData.KNIVES_XSL);

        assertFalse(application.run(output));
        assertFalse(Files.exists(output));
    }

    @Test
    void mainWritesToGivenPath() throws Exception {
        Path output = tempDir.resolve("main.xml");

        KnifeApplication.main(new String[]{output.toString()});

        assertTrue(Files.exists(output));
    }

    @Test
    void mainWritesToDefaultPathWithoutArguments() throws Exception {
        Files.deleteIfExists(KnifeApplication.DEFAULT_OUTPUT);

        KnifeApplication.main(new String[0]);

        assertTrue(Files.exists(KnifeApplication.DEFAULT_OUTPUT));
    }
}
