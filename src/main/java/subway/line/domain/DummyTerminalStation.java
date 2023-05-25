package subway.line.domain;

import subway.station.domain.Station;

class DummyTerminalStation extends Station {

    public static final Long STATION_ID = 0L;
    private static final String DUMMY_TERMINAL_NAME = "DummyTerminal";

    private static final DummyTerminalStation INSTANCE = new DummyTerminalStation();

    private DummyTerminalStation() {
        super(STATION_ID, DUMMY_TERMINAL_NAME);
    }

    public static Station getInstance() {
        return INSTANCE;
    }
}
