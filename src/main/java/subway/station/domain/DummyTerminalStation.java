package subway.station.domain;

public class DummyTerminalStation extends Station {

    public static final Long STATION_ID = 0L;
    private static final String DUMMY_TERMINAL_NAME = "DummyTerminal";

    private static final DummyTerminalStation INSTANCE = new DummyTerminalStation();

    private DummyTerminalStation() {
        super(DUMMY_TERMINAL_NAME);
    }

    public static Station getInstance() {
        return INSTANCE;
    }
}
