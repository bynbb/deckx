package deckx;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageArtifactGeneratorTest {
    // DeckX names image artifacts from slide number and per-slide image order so visuals stay traceable to deck content.
    @Test
    void generateNamesImageArtifactsFromSlideNumberAndImageOrder() {
        byte[] firstImage = new byte[]{1, 2, 3};
        byte[] secondImage = new byte[]{4, 5, 6};

        SlideRecord slide = new SlideRecord(
                1,
                List.of(),
                List.of(firstImage, secondImage)
        );

        ImageArtifactGenerator generator = new ImageArtifactGenerator();

        List<GeneratedImageArtifact> artifacts = generator.generate(List.of(slide));

        assertEquals(2, artifacts.size());

        assertEquals("slide_01__image_01.png", artifacts.get(0).fileName());
        assertArrayEquals(firstImage, artifacts.get(0).binaryContent());

        assertEquals("slide_01__image_02.png", artifacts.get(1).fileName());
        assertArrayEquals(secondImage, artifacts.get(1).binaryContent());
    }

    // DeckX restarts image numbering on each slide because image identity is scoped to its source slide.
    @Test
    void generateRestartsImageNumberingForEachSlide() {
        SlideRecord firstSlide = new SlideRecord(
                1,
                List.of(),
                List.of(new byte[]{1})
        );

        SlideRecord secondSlide = new SlideRecord(
                2,
                List.of(),
                List.of(new byte[]{2})
        );

        ImageArtifactGenerator generator = new ImageArtifactGenerator();

        List<GeneratedImageArtifact> artifacts = generator.generate(List.of(firstSlide, secondSlide));

        assertEquals("slide_01__image_01.png", artifacts.get(0).fileName());
        assertEquals("slide_02__image_01.png", artifacts.get(1).fileName());
    }

    // DeckX uses two-digit numbers so generated image file names sort in deck order.
    @Test
    void generatePadsSlideAndImageNumbersToTwoDigits() {
        SlideRecord slide = new SlideRecord(
                3,
                List.of(),
                List.of(new byte[]{1})
        );

        ImageArtifactGenerator generator = new ImageArtifactGenerator();

        List<GeneratedImageArtifact> artifacts = generator.generate(List.of(slide));

        assertEquals("slide_03__image_01.png", artifacts.get(0).fileName());
    }
}
