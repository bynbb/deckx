package deckx;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DeckXApplication {
    static final String INPUT_FILE = "test_data_presentation.pptx";
    static final String OUTPUT_FOLDER = "test_data_presentation";

    private final Path workingFolder;
    private final PrintStream output;
    private final PrintStream error;
    private final PptxReader pptxReader;
    private final TextArtifactGenerator textArtifactGenerator;
    private final ImageArtifactGenerator imageArtifactGenerator;
    private final OutputFolderCleaner outputFolderCleaner;
    private final OutputWriter outputWriter;

    public DeckXApplication() {
        this(
                Path.of("."),
                System.out,
                System.err,
                new PptxReader(),
                new TextArtifactGenerator(),
                new ImageArtifactGenerator(),
                new OutputFolderCleaner(),
                new OutputWriter()
        );
    }

    DeckXApplication(
            Path workingFolder,
            PrintStream output,
            PrintStream error,
            PptxReader pptxReader,
            TextArtifactGenerator textArtifactGenerator,
            ImageArtifactGenerator imageArtifactGenerator,
            OutputFolderCleaner outputFolderCleaner,
            OutputWriter outputWriter
    ) {
        this.workingFolder = workingFolder;
        this.output = output;
        this.error = error;
        this.pptxReader = pptxReader;
        this.textArtifactGenerator = textArtifactGenerator;
        this.imageArtifactGenerator = imageArtifactGenerator;
        this.outputFolderCleaner = outputFolderCleaner;
        this.outputWriter = outputWriter;
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

        List<SlideRecord> slides;

        try {
            slides = pptxReader.readSlides(inputFile);
        } catch (Exception exception) {
            error.println("DeckX failed: " + INPUT_FILE + " could not be opened for reading.");
            return 1;
        }

        try {
            Path outputFolder = workingFolder.resolve(OUTPUT_FOLDER);

            // The runtime turns the golden example deck into PowerPoint-independent text and image artifacts.
            output.println("Generating text files...");
            List<GeneratedTextArtifact> textArtifacts = textArtifactGenerator.generate(slides);

            output.println("Generating image files...");
            List<GeneratedImageArtifact> imageArtifacts = imageArtifactGenerator.generate(slides);

            output.println("Writing output files...");
            output.println("Checking for an existing output folder: " + OUTPUT_FOLDER);

            if (outputFolderHasContents(outputFolder)) {
                output.println("Found existing output folder: " + OUTPUT_FOLDER);
                output.println("Removing old contents from output folder: " + OUTPUT_FOLDER);
            } else {
                output.println("No existing output folder contents found: " + OUTPUT_FOLDER);
            }

            outputFolderCleaner.clean(outputFolder);
            outputWriter.write(outputFolder, textArtifacts, imageArtifacts);

            // Completion reporting confirms the PowerPoint-independent output location and artifact count.
            int generatedFileCount = textArtifacts.size() + imageArtifacts.size();
            output.println("Processing complete.");
            output.println("Output folder: " + OUTPUT_FOLDER);
            output.println("Generated files: " + generatedFileCount);

            return 0;
        } catch (IOException exception) {
            error.println("DeckX failed: " + exception.getMessage());
            return 1;
        }
    }

    private boolean outputFolderHasContents(Path outputFolder) throws IOException {
        if (!Files.isDirectory(outputFolder)) {
            return false;
        }

        try (var children = Files.list(outputFolder)) {
            return children.findAny().isPresent();
        }
    }
}
