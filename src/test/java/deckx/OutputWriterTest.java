package deckx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputWriterTest {
    @TempDir
    Path tempDir;

    // DeckX writes text artifacts as readable files so tagged slide meaning can be inspected outside PowerPoint.
    @Test
    void writeCreatesTextFilesFromTextArtifacts() throws Exception {
        Path outputFolder = tempDir.resolve("test_data_presentation");
        Files.createDirectories(outputFolder);

        GeneratedTextArtifact textArtifact = new GeneratedTextArtifact(
                "slide_01__title.txt",
                "Title:>Test Data Presentation"
        );

        OutputWriter writer = new OutputWriter();

        writer.write(outputFolder, List.of(textArtifact), List.of());

        Path writtenFile = outputFolder.resolve("slide_01__title.txt");

        assertTrue(Files.isRegularFile(writtenFile));
        assertEquals("Title:>Test Data Presentation", Files.readString(writtenFile));
    }

    // DeckX writes image artifacts as binary files so embedded slide visuals can be reused outside PowerPoint.
    @Test
    void writeCreatesImageFilesFromImageArtifacts() throws Exception {
        Path outputFolder = tempDir.resolve("test_data_presentation");
        Files.createDirectories(outputFolder);

        byte[] imageBytes = new byte[]{1, 2, 3};

        GeneratedImageArtifact imageArtifact = new GeneratedImageArtifact(
                "slide_01__image_01.png",
                imageBytes
        );

        OutputWriter writer = new OutputWriter();

        writer.write(outputFolder, List.of(), List.of(imageArtifact));

        Path writtenFile = outputFolder.resolve("slide_01__image_01.png");

        assertTrue(Files.isRegularFile(writtenFile));
        assertArrayEquals(imageBytes, Files.readAllBytes(writtenFile));
    }
}
