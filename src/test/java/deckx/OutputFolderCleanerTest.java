package deckx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputFolderCleanerTest {
    @TempDir
    Path tempDir;

    // DeckX creates the output folder so generated artifacts have a fixed PowerPoint-independent destination.
    @Test
    void cleanCreatesOutputFolderWhenMissing() throws Exception {
        Path outputFolder = tempDir.resolve("test_data_presentation");

        OutputFolderCleaner cleaner = new OutputFolderCleaner();

        cleaner.clean(outputFolder);

        assertTrue(Files.isDirectory(outputFolder));
    }

    // DeckX removes prior output before a rerun so stale files cannot be mistaken for current generated artifacts.
    @Test
    void cleanRemovesExistingOutputContents() throws Exception {
        Path outputFolder = tempDir.resolve("test_data_presentation");
        Files.createDirectories(outputFolder);

        Path oldTextFile = outputFolder.resolve("old.txt");
        Files.writeString(oldTextFile, "old output");

        Path oldNestedFolder = outputFolder.resolve("old_nested");
        Files.createDirectories(oldNestedFolder);
        Path oldNestedFile = oldNestedFolder.resolve("old_nested.txt");
        Files.writeString(oldNestedFile, "old nested output");

        OutputFolderCleaner cleaner = new OutputFolderCleaner();

        cleaner.clean(outputFolder);

        assertTrue(Files.isDirectory(outputFolder));
        assertFalse(Files.exists(oldTextFile));
        assertFalse(Files.exists(oldNestedFolder));
    }
}
