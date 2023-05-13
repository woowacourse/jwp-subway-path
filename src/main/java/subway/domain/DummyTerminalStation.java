package subway.domain;

public class DummyTerminalStation extends Station {

    private static final String DUMMY_TERMINAL_NAME = "DummyTerminal";

    private static final DummyTerminalStation INSTANCE = new DummyTerminalStation();

    private DummyTerminalStation() {
        super(DUMMY_TERMINAL_NAME);
    }

    public static Station getInstance() {
        return INSTANCE;
    }
}
