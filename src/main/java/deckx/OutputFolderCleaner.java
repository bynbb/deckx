package deckx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

class OutputFolderCleaner {
    void clean(Path outputFolder) throws IOException {
        // DeckX always writes into a fixed PowerPoint-independent output folder.
        Files.createDirectories(outputFolder);

        // Rerun cleanup prevents stale generated files from being confused with the current deck output.
        try (var children = Files.list(outputFolder)) {
            for (Path child : children.toList()) {
                deleteRecursively(child);
            }
        }
    }

    private void deleteRecursively(Path path) throws IOException {
        if ("Thumbs.db".equalsIgnoreCase(path.getFileName().toString())) {
            return;
        }

        if (Files.isDirectory(path)) {
            try (var descendants = Files.walk(path)) {
                for (Path descendant : descendants
                        .sorted(Comparator.reverseOrder())
                        .toList()) {
                    if (!"Thumbs.db".equalsIgnoreCase(descendant.getFileName().toString())) {
                        Files.deleteIfExists(descendant);
                    }
                }
            }
        } else {
            Files.deleteIfExists(path);
        }
    }
}

