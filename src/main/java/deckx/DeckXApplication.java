package deckx;

public class DeckXApplication {
    static final String INPUT_FILE = "test_data_presentation.pptx";
    static final String OUTPUT_FOLDER = "test_data_presentation";

    public static void main(String[] args) {
        new DeckXApplication().run();
    }

    void run() {
        System.out.println("Starting DeckX processing...");
    }
}