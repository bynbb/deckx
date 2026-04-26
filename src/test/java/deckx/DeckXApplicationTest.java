package deckx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DeckXApplicationTest {
    @Test
    void runDoesNotThrow() {
        DeckXApplication application = new DeckXApplication();

        assertDoesNotThrow(application::run);
    }
}