package fixtures;


import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.StationFixtures.INITIAL_STATION_A;
import static fixtures.StationFixtures.INITIAL_STATION_C;

import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.NearbyStations;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class SectionFixtures {

    public static class INITIAL_SECTION_A_TO_C {
        public static final Long ID = 1L;
        public static final Distance DISTANCE = new Distance(10);

        public static final Section FIND_SECTION = new Section(ID,
                NearbyStations.createByUpStationAndDownStation(INITIAL_STATION_A.FIND_STATION, INITIAL_STATION_C.FIND_STATION),
                INITIAL_Line2.FIND_LINE, DISTANCE);
    }

    public static class SECTION_A_TO_B {
        public static final Distance DISTANCE = new Distance(3);

        public static Section createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new Section(null, nearbyStations, line, DISTANCE);
        }
    }

    public static class SECTION_B_TO_C {
        public static final Distance DISTANCE = new Distance(7);

        public static Section createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new Section(null, nearbyStations, line, DISTANCE);
        }
    }

    public static class SECTION_D_TO_A {
        public static final Distance DISTANCE = new Distance(2);

        public static Section createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new Section(null, nearbyStations, line, DISTANCE);
        }

    }

    public static class SECTION_C_TO_E {

        public static final Distance DISTANCE = new Distance(4);

        public static Section createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new Section(null, nearbyStations, line, DISTANCE);
        }
    }
}
