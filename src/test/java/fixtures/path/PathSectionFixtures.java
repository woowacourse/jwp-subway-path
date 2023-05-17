package fixtures.path;

import static fixtures.path.PathLineFixtures.*;
import static fixtures.path.PathStationFixtures.*;

import subway.domain.section.Distance;
import subway.domain.section.NearbyStations;
import subway.domain.section.Section;

public class PathSectionFixtures {

    public static class INITIAL_SECTION_LINE2_A_TO_C {
        public static final Long ID = 1L;
        public static final int DISTANCE = 10;

        public static final Section FIND_SECTION = new Section(
                ID,
                NearbyStations.createByUpStationAndDownStation(
                        INITIAL_STATION_LINE2_A.FIND_STATION, INITIAL_STATION_LINE2_C.FIND_STATION),
                INITIAL_LINE2.FIND_LINE,
                new Distance(DISTANCE)
        );
    }

    public static class INITIAL_SECTION_LINE3_C_TO_E {
        public static final Long ID = 2L;
        public static final int DISTANCE = 8;

        public static final Section FIND_SECTION = new Section(
                ID,
                NearbyStations.createByUpStationAndDownStation(
                        INITIAL_STATION_LINE3_C.FIND_STATION, INITIAL_STATION_LINE3_E.FIND_STATION),
                INITIAL_LINE3.FIND_LINE,
                new Distance(DISTANCE)
        );
    }

    public static class INITIAL_SECTION_LINE7_A_TO_D {
        public static final Long ID = 3L;
        public static final int DISTANCE = 12;

        public static final Section FIND_SECTION = new Section(
                ID,
                NearbyStations.createByUpStationAndDownStation(
                        INITIAL_STATION_LINE7_A.FIND_STATION, INITIAL_STATION_LINE7_D.FIND_STATION),
                INITIAL_LINE7.FIND_LINE,
                new Distance(DISTANCE)
        );
    }

    public static class INITIAL_SECTION_LINE3_D_TO_E {
        public static final Long ID = 4L;
        public static final int DISTANCE = 5;

        public static final Section FIND_SECTION = new Section(
                ID,
                NearbyStations.createByUpStationAndDownStation(
                        INITIAL_STATION_LINE3_D.FIND_STATION, INITIAL_STATION_LINE3_E.FIND_STATION),
                INITIAL_LINE3.FIND_LINE,
                new Distance(DISTANCE)
        );
    }
}
