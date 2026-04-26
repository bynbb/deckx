package deckx;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class PptxReader {
    List<SlideRecord> readSlides(Path inputFile) throws IOException {
        try (XMLSlideShow presentation = new XMLSlideShow(Files.newInputStream(inputFile))) {
            List<SlideRecord> slides = new ArrayList<>();

            for (int index = 0; index < presentation.getSlides().size(); index++) {
                List<String> textLines = new ArrayList<>();

                // POI is the boundary where PowerPoint slide shapes become DeckX slide text.
                for (XSLFShape shape : presentation.getSlides().get(index).getShapes()) {
                    if (shape instanceof XSLFTextShape textShape) {
                        String text = textShape.getText();

                        if (text != null && !text.isBlank()) {
                            textLines.add(text.trim());
                        }
                    }
                }

                slides.add(new SlideRecord(index + 1, textLines));
            }

            return slides;
        }
    }
}
