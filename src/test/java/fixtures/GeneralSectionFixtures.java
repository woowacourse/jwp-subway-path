package fixtures;


import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.StationFixtures.INITIAL_STATION_A;
import static fixtures.StationFixtures.INITIAL_STATION_C;

import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.general.NearbyStations;
import subway.domain.station.Station;

public class GeneralSectionFixtures {

    public static class INITIAL_GENERAL_SECTION_A_TO_C {
        public static final Long ID = 1L;
        public static final int RAW_DISTANCE = 10;
        public static final Distance DISTANCE = new Distance(RAW_DISTANCE);

        public static final GeneralSection FIND_SECTION = new GeneralSection(ID,
                NearbyStations.createByUpStationAndDownStation(INITIAL_STATION_A.FIND_STATION, INITIAL_STATION_C.FIND_STATION),
                INITIAL_Line2.FIND_LINE, DISTANCE);
    }

    public static class GENERAL_SECTION_A_TO_B {
        public static final Distance DISTANCE = new Distance(3);

        public static GeneralSection createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new GeneralSection(null, nearbyStations, line, DISTANCE);
        }

        public static GeneralSection createDummy(Station upStation, Station downStation, Line line) {
            return new GeneralSection(-1L, NearbyStations.createByUpStationAndDownStation(upStation, downStation), line, DISTANCE);
        }
    }

    public static class GENERAL_SECTION_B_TO_C {
        public static final Distance DISTANCE = new Distance(7);

        public static GeneralSection createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new GeneralSection(null, nearbyStations, line, DISTANCE);
        }

        public static GeneralSection createDummy(Station upStation, Station downStation, Line line) {
            return new GeneralSection(-1L, NearbyStations.createByUpStationAndDownStation(upStation, downStation), line, DISTANCE);
        }
    }

    public static class GENERAL_SECTION_D_TO_A {
        public static final Distance DISTANCE = new Distance(2);

        public static GeneralSection createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new GeneralSection(null, nearbyStations, line, DISTANCE);
        }

        public static GeneralSection createDummy(Station upStation, Station downStation, Line line) {
            return new GeneralSection(-1L, NearbyStations.createByUpStationAndDownStation(upStation, downStation), line, DISTANCE);
        }
    }

    public static class GENERAL_SECTION_C_TO_E {

        public static final Distance DISTANCE = new Distance(4);

        public static GeneralSection createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new GeneralSection(null, nearbyStations, line, DISTANCE);
        }

        public static GeneralSection createDummy(Station upStation, Station downStation, Line line) {
            return new GeneralSection(-1L, NearbyStations.createByUpStationAndDownStation(upStation, downStation), line, DISTANCE);
        }
    }

    public static class GENERAL_SECTION_B_TO_D {
        public static final Distance DISTANCE = new Distance(3);

        public static GeneralSection createSectionToInsert(Station upStation, Station downStation, Line line) {
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            return new GeneralSection(null, nearbyStations, line, DISTANCE);
        }

        public static GeneralSection createDummy(Station upStation, Station downStation, Line line) {
            return new GeneralSection(-1L, NearbyStations.createByUpStationAndDownStation(upStation, downStation), line, DISTANCE);
        }
    }
}
