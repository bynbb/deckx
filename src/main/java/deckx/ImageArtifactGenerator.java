package deckx;

import java.util.ArrayList;
import java.util.List;

class ImageArtifactGenerator {
    List<GeneratedImageArtifact> generate(List<SlideRecord> slides) {
        List<GeneratedImageArtifact> artifacts = new ArrayList<>();

        for (SlideRecord slide : slides) {
            for (int imageIndex = 0; imageIndex < slide.imageBytes().size(); imageIndex++) {
                int imageNumber = imageIndex + 1;

                // Slide and image numbers preserve where each visual artifact came from in the prepared deck.
                String fileName = "slide_%02d__image_%02d.png".formatted(slide.slideNumber(), imageNumber);

                artifacts.add(new GeneratedImageArtifact(fileName, slide.imageBytes().get(imageIndex)));
            }
        }

        return artifacts;
    }
}
