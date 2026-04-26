package deckx;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextArtifactGeneratorTest {
    // DeckX names text artifacts from slide number and tag so each file can be traced back to deck content.
    @Test
    void generateNamesTextArtifactsFromSlideNumberAndTag() {
        SlideRecord slide = new SlideRecord(
                1,
                List.of("Title:>Test Data Presentation", "Author:> DAISY DUCK"),
                List.of()
        );

        TextArtifactGenerator generator = new TextArtifactGenerator();

        List<GeneratedTextArtifact> artifacts = generator.generate(List.of(slide));

        assertEquals(2, artifacts.size());

        assertEquals("slide_01__title.txt", artifacts.get(0).fileName());
        assertEquals("Title:>Test Data Presentation", artifacts.get(0).textContent());

        assertEquals("slide_01__author.txt", artifacts.get(1).fileName());
        assertEquals("Author:> DAISY DUCK", artifacts.get(1).textContent());
    }

    // DeckX uses two-digit slide numbers so generated file names sort in deck order.
    @Test
    void generatePadsSlideNumbersToTwoDigits() {
        SlideRecord slide = new SlideRecord(
                3,
                List.of("ID:> ID04212026"),
                List.of()
        );

        TextArtifactGenerator generator = new TextArtifactGenerator();

        List<GeneratedTextArtifact> artifacts = generator.generate(List.of(slide));

        assertEquals("slide_03__id.txt", artifacts.get(0).fileName());
    }

    // DeckX trims and lowercases tags because file names use normalized tag identity.
    @Test
    void generateNormalizesTagName() {
        SlideRecord slide = new SlideRecord(
                2,
                List.of(" Sequence :> 1 of 2"),
                List.of()
        );

        TextArtifactGenerator generator = new TextArtifactGenerator();

        List<GeneratedTextArtifact> artifacts = generator.generate(List.of(slide));

        assertEquals("slide_02__sequence.txt", artifacts.get(0).fileName());
    }
}
