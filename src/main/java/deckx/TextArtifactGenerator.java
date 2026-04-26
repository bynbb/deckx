package deckx;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class TextArtifactGenerator {
    List<GeneratedTextArtifact> generate(List<SlideRecord> slides) {
        List<GeneratedTextArtifact> artifacts = new ArrayList<>();

        for (SlideRecord slide : slides) {
            for (String textLine : slide.textLines()) {

                // The tag controls the text file identity because DeckX output must stay traceable to tagged slide meaning.
                String normalizedTag = normalizedTagFrom(textLine);
                String fileName = "slide_%02d__%s.txt".formatted(slide.slideNumber(), normalizedTag);

                artifacts.add(new GeneratedTextArtifact(fileName, textLine));
            }
        }

        return artifacts;
    }

    private String normalizedTagFrom(String textLine) {
        int delimiterIndex = textLine.indexOf(":>");
        String rawTag = delimiterIndex >= 0
                ? textLine.substring(0, delimiterIndex)
                : textLine;

        return rawTag.trim().toLowerCase(Locale.ROOT);
    }
}
