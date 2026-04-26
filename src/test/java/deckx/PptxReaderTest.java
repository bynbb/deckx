package deckx;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PptxReaderTest {
    @TempDir
    Path tempDir;

    // DeckX must preserve slide order and meaningful text so generated output can trace back to test_data_presentation.pptx.
    @Test
    void readSlidesExtractsNonblankTextInSlideOrder() throws Exception {
        Path inputFile = tempDir.resolve("sample.pptx");

        try (XMLSlideShow presentation = new XMLSlideShow()) {
            XSLFSlide firstSlide = presentation.createSlide();
            XSLFTextBox firstTextBox = firstSlide.createTextBox();
            firstTextBox.setText("Title:>First Slide");

            XSLFSlide secondSlide = presentation.createSlide();
            XSLFTextBox blankTextBox = secondSlide.createTextBox();
            blankTextBox.setText("   ");

            XSLFTextBox secondTextBox = secondSlide.createTextBox();
            secondTextBox.setText("Title:>Second Slide");

            try (OutputStream output = Files.newOutputStream(inputFile)) {
                presentation.write(output);
            }
        }

        PptxReader reader = new PptxReader();

        List<SlideRecord> slides = reader.readSlides(inputFile);

        assertEquals(2, slides.size());

        assertEquals(1, slides.get(0).slideNumber());
        assertEquals(List.of("Title:>First Slide"), slides.get(0).textLines());

        assertEquals(2, slides.get(1).slideNumber());
        assertEquals(List.of("Title:>Second Slide"), slides.get(1).textLines());
    }
}
