package deckx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class OutputWriter {
    void write(
            Path outputFolder,
            List<GeneratedTextArtifact> textArtifacts,
            List<GeneratedImageArtifact> imageArtifacts
    ) throws IOException {
        // Text artifacts preserve tagged slide meaning as readable PowerPoint-independent files.
        for (GeneratedTextArtifact artifact : textArtifacts) {
            Files.writeString(outputFolder.resolve(artifact.fileName()), artifact.textContent());
        }

        // Image artifacts preserve embedded slide visuals as native files outside the PowerPoint container.
        for (GeneratedImageArtifact artifact : imageArtifacts) {
            Files.write(outputFolder.resolve(artifact.fileName()), artifact.binaryContent());
        }
    }
}
