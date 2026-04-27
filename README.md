# DeckX

DeckX is a small Java console application that extracts readable text files and image files from a prepared PowerPoint deck.

It was built as a focused proof-of-concept for transforming presentation content into simple file-system artifacts that can be inspected, searched, or reused outside PowerPoint.

## What DeckX Does

DeckX reads a PowerPoint file named:

```text
test_data_presentation.pptx
```

Then it creates an output folder named:

```
test_data_presentation
```

The output folder contains generated `.txt` and `.png` files extracted from the deck.

Text files are named from the slide number and tagged text label, for example:

```
slide_01__title.txt
slide_01__author.txt
```

Image files are named from the slide number and image order, for example:

```
slide_03__image_01.png
```

## Current Scope

DeckX currently runs against one prepared example deck:

```
test_data_presentation.pptx
```

The app intentionally does not provide a file picker, command-line input argument, configuration file, or general-purpose conversion workflow.

## Requirements

* Java 21
* Maven, if building from source

## Build

From the project root:

```bash
mvn clean package
```

The runnable jar is created at:

```
target/DeckX-1.0-SNAPSHOT.jar
```

## Run

Place `test_data_presentation.pptx` in the same folder where DeckX is run.

Then run:

```bash
java -jar target/DeckX-1.0-SNAPSHOT.jar
```

DeckX will create or refresh the `test_data_presentation` output folder.

## Project Status

DeckX is a focused, working proof-of-concept.

Implemented behavior includes:

* fixed PowerPoint input detection
* PowerPoint reading with Apache POI
* slide text extraction
* embedded image extraction
* deterministic text artifact naming
* deterministic image artifact naming
* output folder cleanup before rerun
* runnable portable jar packaging

## Origin of Concept

DeckX was motivated by several problems observed when asking AI tools to extract structured content directly from PowerPoint files:

1. Custom tagging schemes require consistent interpretation, which AI tools may apply inconsistently across runs.
2. Extracting multiple artifacts at once can lead to inconsistent grouping or unclear output structure when handled by AI tools.
3. AI-generated outputs are not guaranteed to be deterministic, which can result in inconsistent file names and reduced reproducibility.

DeckX reduces those risks by using a deterministic Java pipeline. It reads one prepared PowerPoint deck, extracts text and images, and writes files using predictable naming rules.

## Possible Next Steps

Potential future work could include:

* accepting a PowerPoint file path as a command-line argument
* supporting multiple input decks
* generating richer metadata
* grouping artifacts by slide
* exporting structured JSON
* improving handling of complex slide layouts
* adding a GUI or drag-and-drop workflow

## License

No license has been specified yet.
