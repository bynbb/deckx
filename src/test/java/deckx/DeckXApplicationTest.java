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
                new PrintStream(error),
                new PptxReader(),
                new TextArtifactGenerator(),
                new ImageArtifactGenerator(),
                new OutputFolderCleaner(),
                new OutputWriter()
        );

        int exitCode = application.run();

        assertEquals(1, exitCode);
        assertEquals("Starting DeckX processing..." + System.lineSeparator(), output.toString());
        assertEquals(
                "DeckX failed: test_data_presentation.pptx was not found." + System.lineSeparator(),
                error.toString()
        );
    }
}
