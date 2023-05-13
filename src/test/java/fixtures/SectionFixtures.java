package fixtures;


import static fixtures.LineFixtures.*;
import static fixtures.StationFixtures.*;

import subway.domain.section.Distance;
import subway.domain.section.NearbyStations;
import subway.domain.section.Section;

public class SectionFixtures {

    public static class SECTION_A_TO_C {
        public static final long ID = 1L;
        public static final Distance DISTANCE = new Distance(10);
        public static final NearbyStations NEARBY_STATIONS = NearbyStations.createByUpStationAndDownStation(STATION_A.FIND_ENTITY, STATION_C.FIND_ENTITY);
        public static final Section INSERT_ENTITY = new Section(null, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
        public static final Section FIND_ENTITY = new Section(ID, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
    }

    public static class SECTION_A_TO_B {
        public static final long ID = 2L;
        public static final Distance DISTANCE = new Distance(3);
        public static final NearbyStations NEARBY_STATIONS = NearbyStations.createByUpStationAndDownStation(STATION_A.FIND_ENTITY, STATION_B.FIND_ENTITY);
        public static final Section INSERT_ENTITY = new Section(null, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
        public static final Section FIND_ENTITY = new Section(ID, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
    }

    public static class SECTION_B_TO_C {
        public static final long ID = 3L;
        public static final Distance DISTANCE = new Distance(7);
        public static final NearbyStations NEARBY_STATIONS = NearbyStations.createByUpStationAndDownStation(STATION_B.FIND_ENTITY, STATION_C.FIND_ENTITY);
        public static final Section INSERT_ENTITY = new Section(null, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
        public static final Section FIND_ENTITY = new Section(ID, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
    }

    public static class SECTION_D_TO_A {
        public static final long ID = 4L;
        public static final Distance DISTANCE = new Distance(2);
        public static final NearbyStations NEARBY_STATIONS = NearbyStations.createByUpStationAndDownStation(STATION_D.FIND_ENTITY, STATION_A.FIND_ENTITY);
        public static final Section INSERT_ENTITY = new Section(null, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
        public static final Section FIND_ENTITY = new Section(ID, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);

    }

    public static class SECTION_C_TO_E {
        public static final long ID = 5L;

        public static final Distance DISTANCE = new Distance(4);

        public static final NearbyStations NEARBY_STATIONS = NearbyStations.createByUpStationAndDownStation(STATION_C.FIND_ENTITY, STATION_E.FIND_ENTITY);
        public static final Section INSERT_ENTITY = new Section(null, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
        public static final Section FIND_ENTITY = new Section(ID, NEARBY_STATIONS, Line2.FIND_ENTITY, DISTANCE);
    }
}
