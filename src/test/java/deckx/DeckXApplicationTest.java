package deckx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckXApplicationTest {
    @TempDir
    Path tempDir;

    @Test
    void runReportsMissingFixedInputFile() {
        // The runtime must fail here because DeckX treats test_data_presentation.pptx as its golden example input.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        DeckXApplication application = new DeckXApplication(
                tempDir,
                new PrintStream(output),
                new PrintStream(error)
        );

        int exitCode = application.run();

        assertEquals(1, exitCode);
        assertEquals("Starting DeckX processing..." + System.lineSeparator(), output.toString());
        assertEquals(
                "DeckX failed: test_data_presentation.pptx was not found." + System.lineSeparator(),
                error.toString()
        );
    }

    @Test
    void runSucceedsWhenFixedInputFileExists() throws Exception {
        // This test only checks the fixed-name input gate, not whether the file is a readable PowerPoint deck.
        Path inputFile = tempDir.resolve("test_data_presentation.pptx");
        inputFile.toFile().createNewFile();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        DeckXApplication application = new DeckXApplication(
                tempDir,
                new PrintStream(output),
                new PrintStream(error)
        );

        int exitCode = application.run();

        assertEquals(0, exitCode);
        assertEquals("Starting DeckX processing..." + System.lineSeparator(), output.toString());
        assertEquals("", error.toString());
    }
}
