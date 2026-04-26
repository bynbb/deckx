package deckx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckXApplicationWorkflowTest {
    @TempDir
    Path tempDir;

    // DeckX must turn the golden example input into PowerPoint-independent files in the fixed output folder.
    @Test
    void runProcessesFixedDeckIntoOutputFolder() throws Exception {
        URI resourceUri = getClass().getResource("/test_data_presentation.pptx").toURI();
        Path sourcePptx = Path.of(resourceUri);
        Path runtimePptx = tempDir.resolve("test_data_presentation.pptx");
        Files.copy(sourcePptx, runtimePptx);

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

        Path outputFolder = tempDir.resolve("test_data_presentation");

        assertEquals(0, exitCode);
        assertEquals("", error.toString());
        assertTrue(output.toString().contains("Processing complete."));
        assertTrue(output.toString().contains("Output folder: test_data_presentation"));
        assertTrue(output.toString().contains("Generated files: "));
        assertTrue(Files.isDirectory(outputFolder));
        assertTrue(Files.isRegularFile(outputFolder.resolve("slide_01__title.txt")));
        assertTrue(Files.isRegularFile(outputFolder.resolve("slide_01__author.txt")));
        try (var files = Files.list(outputFolder)) {
            assertTrue(files.anyMatch(path -> path.getFileName().toString().endsWith(".png")));
        }
    }
}
