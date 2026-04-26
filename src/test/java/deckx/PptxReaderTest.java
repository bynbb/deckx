package deckx;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PptxReaderTest {
    private Path testDataPresentationPath() throws Exception {
        URI resourceUri = getClass().getResource("/test_data_presentation.pptx").toURI();
        return Path.of(resourceUri);
    }

    // DeckX must preserve known tagged text so generated output can trace back to test_data_presentation.pptx.
    @Test
    void readsFixedDeckAndExtractsKnownTaggedText() throws Exception {
        PptxReader reader = new PptxReader();

        List<SlideRecord> slides = reader.readSlides(testDataPresentationPath());

        assertFalse(slides.isEmpty());
        assertTrue(slides.get(0).textLines().contains("Title:>Test Data Presentation"));
        assertTrue(slides.get(0).textLines().contains("Author:> DAISY DUCK"));
        assertTrue(slides.get(0).textLines().contains("Scope:> user story"));
        assertTrue(slides.get(0).textLines().contains("ID:> ID04212026"));
    }

    // DeckX must preserve embedded images so visual slide evidence can become PowerPoint-independent output.
    @Test
    void readsFixedDeckAndExtractsEmbeddedImages() throws Exception {
        PptxReader reader = new PptxReader();

        List<SlideRecord> slides = reader.readSlides(testDataPresentationPath());

        long imageCount = slides.stream()
                .flatMap(slide -> slide.imageBytes().stream())
                .count();

        assertTrue(imageCount > 0);
    }
}
