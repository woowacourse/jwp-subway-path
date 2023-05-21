package fixtures;

import static fixtures.GeneralSectionFixtures.*;
import static fixtures.LineFixtures.INITIAL_Line2;

import java.util.List;

import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.*;

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

        public static Station createDummyStation(Long dummyId, Line line) {
            return new Station(dummyId, NAME, line);
        }
    }

    public static class STATION_D {
        public static final String NAME = "D역";

        public static Station createStationToInsert(Line line) {
            return new Station(null, NAME, line);
        }

        public static Station createDummyStation(Long dummyId, Line line) {
            return new Station(dummyId, NAME, line);
        }
    }

    public static class STATION_E {
        public static final String NAME = "E역";

        public static Station createStationToInsert(Line line) {
            return new Station(null, NAME, line);
        }

        public static Station createDummyStation(Long dummyId, Line line) {
            return new Station(dummyId, NAME, line);
        }
    }

    /**
     * Request
     */
    public static class INITIAL_STATION_REQUEST_A_TO_C {
        public static final StationRequest REQUEST = new StationRequest(INITIAL_STATION_A.NAME, INITIAL_STATION_C.NAME, INITIAL_GENERAL_SECTION_A_TO_C.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class DOWN_MIDDLE_STATION_REQUEST_A_TO_B {
        public static final StationRequest REQUEST = new StationRequest(INITIAL_STATION_A.NAME, STATION_B.NAME, GENERAL_SECTION_A_TO_B.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class UP_MIDDLE_STATION_REQUEST_B_TO_C {
        public static final StationRequest REQUEST = new StationRequest(STATION_B.NAME, INITIAL_STATION_C.NAME, GENERAL_SECTION_B_TO_C.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class UP_END_STATION_REQUEST_D_TO_A {
        public static final StationRequest REQUEST = new StationRequest(STATION_D.NAME, INITIAL_STATION_A.NAME, GENERAL_SECTION_D_TO_A.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class DOWN_END_STATION_REQUEST_C_TO_E {
        public static final StationRequest REQUEST = new StationRequest(INITIAL_STATION_C.NAME, STATION_E.NAME, GENERAL_SECTION_C_TO_E.DISTANCE.getDistance(), INITIAL_Line2.NAME);
    }

    public static class NOT_EXIST_ALL_STATION_REQUEST_D_TO_E {
        public static final StationRequest REQUEST = new StationRequest(STATION_D.NAME, STATION_E.NAME, 5, INITIAL_Line2.NAME);
    }

    /**
     * Response
     */
    public static class INITIAL_SAVE_STATION_RESPONSE_A_TO_C {
        public static final StationSaveResponse RESPONSE =
                new StationSaveResponse(
                        LineDto.from(INITIAL_Line2.FIND_LINE),
                        List.of(StationSaveDto.from(INITIAL_STATION_A.FIND_STATION), StationSaveDto.from(INITIAL_STATION_C.FIND_STATION)),
                        List.of(GeneralSectionDto.from(INITIAL_GENERAL_SECTION_A_TO_C.FIND_SECTION))
                );
    }

    public static class UP_MIDDLE_SAVE_STATION_RESPONSE_B_TO_C {
        static Station dummyStationB = STATION_B.createDummyStation(-1L, INITIAL_Line2.FIND_LINE);
        static Station stationA = INITIAL_STATION_A.FIND_STATION;
        static Station stationC = INITIAL_STATION_C.FIND_STATION;
        static Line line2 = INITIAL_Line2.FIND_LINE;
        public static final StationSaveResponse RESPONSE =
                new StationSaveResponse(
                        LineDto.from(line2),
                        List.of(
                                StationSaveDto.from(dummyStationB)
                        ),
                        List.of(
                                GeneralSectionDto.from(GENERAL_SECTION_A_TO_B.createDummy(stationA, dummyStationB, line2)),
                                GeneralSectionDto.from(GENERAL_SECTION_B_TO_C.createDummy(dummyStationB, stationC, line2))
                        )
                );
    }

    public static class UP_END_SAVE_STATION_RESPONSE_D_TO_A {
        static Station dummyStationD = STATION_D.createDummyStation(-1L, INITIAL_Line2.FIND_LINE);
        static Station stationA = INITIAL_STATION_A.FIND_STATION;
        static Line line2 = INITIAL_Line2.FIND_LINE;
        public static final StationSaveResponse RESPONSE =
                new StationSaveResponse(
                        LineDto.from(line2),
                        List.of(StationSaveDto.from(dummyStationD)),
                        List.of(GeneralSectionDto.from(GENERAL_SECTION_D_TO_A.createDummy(dummyStationD, stationA, line2)))
                );
    }

    public static class DOWN_MIDDLE_SAVE_STATION_RESPONSE_A_TO_B {
        static Station dummyStationB = STATION_B.createDummyStation(-1L, INITIAL_Line2.FIND_LINE);
        static Station stationA = INITIAL_STATION_A.FIND_STATION;
        static Station stationC = INITIAL_STATION_C.FIND_STATION;
        static Line line2 = INITIAL_Line2.FIND_LINE;
        public static final StationSaveResponse RESPONSE =
                new StationSaveResponse(
                        LineDto.from(line2),
                        List.of(StationSaveDto.from(dummyStationB)),
                        List.of(
                                GeneralSectionDto.from(GENERAL_SECTION_A_TO_B.createDummy(stationA, dummyStationB, line2)),
                                GeneralSectionDto.from(GENERAL_SECTION_B_TO_C.createDummy(dummyStationB, stationC, line2))
                        )
                );
    }

    public static class DOWN_END_SAVE_STATION_RESPONSE_C_TO_E {
        static Station dummyStationE = STATION_E.createDummyStation(-1L, INITIAL_Line2.FIND_LINE);
        static Station stationC = INITIAL_STATION_C.FIND_STATION;
        static Line line2 = INITIAL_Line2.FIND_LINE;
        public static final StationSaveResponse RESPONSE =
                new StationSaveResponse(
                        LineDto.from(line2),
                        List.of(StationSaveDto.from(dummyStationE)),
                        List.of(GeneralSectionDto.from(GENERAL_SECTION_C_TO_E.createDummy(stationC, dummyStationE, line2)))
                );
    }

    public static List<Station> ALL_LINE2_STATION = List.of(
            INITIAL_STATION_A.FIND_STATION,
            INITIAL_STATION_C.FIND_STATION
    );
}
