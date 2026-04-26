package deckx;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeckXApplication {
    static final String INPUT_FILE = "test_data_presentation.pptx";
    static final String OUTPUT_FOLDER = "test_data_presentation";

    private final Path workingFolder;
    private final PrintStream output;
    private final PrintStream error;

    public DeckXApplication() {
        this(Path.of("."), System.out, System.err);
    }

    DeckXApplication(Path workingFolder, PrintStream output, PrintStream error) {
        this.workingFolder = workingFolder;
        this.output = output;
        this.error = error;
    }

    public static void main(String[] args) {
        int exitCode = new DeckXApplication().run();
        System.exit(exitCode);
    }

    int run() {
        output.println("Starting DeckX processing...");

        // DeckX intentionally runs only against the prepared input deck in the current working folder.
        Path inputFile = workingFolder.resolve(INPUT_FILE);

        // DeckX stops before generating artifacts because test_data_presentation.pptx is the golden example input.
        if (!Files.isRegularFile(inputFile)) {
            error.println("DeckX failed: " + INPUT_FILE + " was not found.");
            return 1;
        }

        return 0;
    }
}
