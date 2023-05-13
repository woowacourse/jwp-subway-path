package fixtures;

import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.SectionFixtures.*;

import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.StationRequest;

public class StationFixtures {

    public static class INITIAL_STATION_A {

        public static final Long ID = 1L;
        public static final String NAME = "A역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_Line2.FIND_LINE);
    }

    public static class INITIAL_STATION_C {
        public static final Long ID = 2L;
        public static final String NAME = "C역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_Line2.FIND_LINE);
    }

    public static class STATION_B {
        public static final String NAME = "B역";

        public static Station createStationToInsert(Line line) {
            return new Station(null, NAME, line);
        }
    }

    public static class STATION_D {
        public static final String NAME = "D역";

        public static Station createStationToInsert(Line line) {
            return new Station(null, NAME, line);
        }
    }

    public static class STATION_E {
        public static final String NAME = "E역";

        public static Station createStationToInsert(Line line) {
            return new Station(null, NAME, line);
        }
    }

    /**
     * Request
     */
    public static class INITIAL_STATION_REQUEST_A_TO_C {
        public static final StationRequest REQUEST = new StationRequest(INITIAL_STATION_A.NAME, INITIAL_STATION_C.NAME, INITIAL_SECTION_A_TO_C.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class DOWN_MIDDLE_STATION_REQUEST_A_TO_B {
        public static final StationRequest REQUEST = new StationRequest(INITIAL_STATION_A.NAME, STATION_B.NAME, SECTION_A_TO_B.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class UP_MIDDLE_STATION_REQUEST_B_TO_C {
        public static final StationRequest REQUEST = new StationRequest(STATION_B.NAME, INITIAL_STATION_C.NAME, SECTION_B_TO_C.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class UP_END_STATION_REQUEST_D_TO_A {
        public static final StationRequest REQUEST = new StationRequest(STATION_D.NAME, INITIAL_STATION_A.NAME, SECTION_D_TO_A.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class DOWN_END_STATION_REQUEST_C_TO_E {
        public static final StationRequest REQUEST = new StationRequest(INITIAL_STATION_C.NAME, STATION_E.NAME, SECTION_C_TO_E.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }
}
