package fixtures.path;

import static fixtures.path.PathLineFixtures.*;

import java.util.List;

import subway.domain.station.Station;
import subway.dto.StationFindDto;

public class PathStationFixtures {

    public static class INITIAL_STATION_LINE2_A {
        public static final Long ID = 1L;
        public static final String NAME = "A역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE2.FIND_LINE);
        public static final StationFindDto FIND_DTO = new StationFindDto(INITIAL_LINE2.NAME, NAME);
    }

    public static class INITIAL_STATION_LINE2_C {
        public static final Long ID = 2L;
        public static final String NAME = "C역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE2.FIND_LINE);
    }

    public static class INITIAL_STATION_LINE7_A {
        public static final Long ID = 3L;
        public static final String NAME = "A역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE7.FIND_LINE);
        public static final StationFindDto FIND_DTO = new StationFindDto(INITIAL_LINE7.NAME, NAME);
    }

    public static class INITIAL_STATION_LINE7_D {
        public static final Long ID = 4L;
        public static final String NAME = "D역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE7.FIND_LINE);
        public static final StationFindDto FIND_DTO = new StationFindDto(INITIAL_LINE7.NAME, NAME);
    }

    public static class INITIAL_STATION_LINE3_C {
        public static final Long ID = 5L;
        public static final String NAME = "C역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE3.FIND_LINE);
    }

    public static class INITIAL_STATION_LINE3_D {
        public static final Long ID = 6L;
        public static final String NAME = "D역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE3.FIND_LINE);
        public static final StationFindDto FIND_DTO = new StationFindDto(INITIAL_LINE3.NAME, NAME);
    }

    public static class INITIAL_STATION_LINE3_E {
        public static final Long ID = 7L;
        public static final String NAME = "E역";

        public static final Station FIND_STATION = new Station(ID, NAME, INITIAL_LINE3.FIND_LINE);
        public static final StationFindDto FIND_DTO = new StationFindDto(INITIAL_LINE3.NAME, NAME);
    }

    public static class DUMMY_STATION_LINE7_E {
        public static final Long ID = -1L;
        public static final String NAME = "E역";
        public static final Station DUMMY = new Station(ID, NAME, INITIAL_LINE7.FIND_LINE);
    }

    public static final List<Station> ALL_INITIAL_STATION =
            List.of(
                    INITIAL_STATION_LINE2_A.FIND_STATION,
                    INITIAL_STATION_LINE2_C.FIND_STATION,
                    INITIAL_STATION_LINE7_A.FIND_STATION,
                    INITIAL_STATION_LINE7_D.FIND_STATION,
                    INITIAL_STATION_LINE3_C.FIND_STATION,
                    INITIAL_STATION_LINE3_D.FIND_STATION,
                    INITIAL_STATION_LINE3_E.FIND_STATION
            );
}
