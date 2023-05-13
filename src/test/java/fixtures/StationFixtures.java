package fixtures;

import static fixtures.LineFixtures.*;
import static fixtures.SectionFixtures.*;

import subway.domain.station.Station;
import subway.dto.*;

import java.util.List;

public class StationFixtures {

    public static class STATION_A {
        public static final long ID = 1L;
        public static final String NAME = "A역";
        public static final Station INSERT_ENTITY = new Station(null, NAME, Line2.FIND_ENTITY);
        public static final Station FIND_ENTITY = new Station(ID, NAME, Line2.FIND_ENTITY);
    }

    public static class STATION_C {
        public static final long ID = 2L;
        public static final String NAME = "C역";
        public static final Station INSERT_ENTITY = new Station(null, NAME, Line2.FIND_ENTITY);
        public static final Station FIND_ENTITY = new Station(ID, NAME, Line2.FIND_ENTITY);
    }

    public static class STATION_B {
        public static final long ID = 3L;
        public static final String NAME = "B역";
        public static final Station INSERT_ENTITY = new Station(null, NAME, Line2.FIND_ENTITY);
        public static final Station FIND_ENTITY = new Station(ID, NAME, Line2.FIND_ENTITY);
    }

    public static class STATION_D {
        public static final long ID = 4L;
        public static final String NAME = "D역";
        public static final Station INSERT_ENTITY = new Station(null, NAME, Line2.FIND_ENTITY);
        public static final Station FIND_ENTITY = new Station(ID, NAME, Line2.FIND_ENTITY);
    }

    public static class STATION_E {
        public static final long ID = 5L;
        public static final String NAME = "E역";
        public static final Station INSERT_ENTITY = new Station(null, NAME, Line2.FIND_ENTITY);
        public static final Station FIND_ENTITY = new Station(ID, NAME, Line2.FIND_ENTITY);
    }

    /**
     * Request
     */
    public static class INITIAL_STATION_REQUEST_A_TO_C {
        public static final StationRequest REQUEST = new StationRequest(STATION_A.NAME, STATION_C.NAME, SECTION_A_TO_C.DISTANCE.getDistance(), Line2.NAME);
    }

    public static class DOWN_MIDDLE_STATION_REQUEST_A_TO_B {
        public static final StationRequest REQUEST = new StationRequest(STATION_A.NAME, STATION_B.NAME, SECTION_A_TO_B.DISTANCE.getDistance(), Line2.NAME);
    }

    public static class UP_MIDDLE_STATION_REQUEST_B_TO_C {
        public static final StationRequest REQUEST = new StationRequest(STATION_B.NAME, STATION_C.NAME, SECTION_B_TO_C.DISTANCE.getDistance(), Line2.NAME);
    }

    public static class UP_END_STATION_REQUEST_D_TO_A {
        public static final StationRequest REQUEST = new StationRequest(STATION_D.NAME, STATION_A.NAME, SECTION_D_TO_A.DISTANCE.getDistance(), Line2.NAME);
    }

    public static class DOWN_END_STATION_REQUEST_C_TO_E {
        public static final StationRequest REQUEST = new StationRequest(STATION_C.NAME, STATION_E.NAME, SECTION_C_TO_E.DISTANCE.getDistance(), Line2.NAME);
    }

    /**
     * response
     */
    public static class INITIAL_STATIONS_SAVE_RESPONSE_A_TO_C {
        public static final StationSaveResponse RESPONSE = new StationSaveResponse(
                LineDto.from(Line2.FIND_ENTITY),
                List.of(StationDto.from(STATION_A.FIND_ENTITY), StationDto.from(STATION_C.FIND_ENTITY)),
                List.of(SectionDto.from(SECTION_A_TO_C.FIND_ENTITY))
        );
    }

    public static class DOWN_MIDDLE_STATION_SAVE_RESPONSE_A_TO_B {
        public static final StationSaveResponse RESPONSE = new StationSaveResponse(
                LineDto.from(Line2.FIND_ENTITY),
                List.of(StationDto.from(STATION_A.FIND_ENTITY), StationDto.from(STATION_B.FIND_ENTITY)),
                List.of(SectionDto.from(SECTION_A_TO_B.FIND_ENTITY))
        );
    }

    public static class UP_MIDDLE_STATION_SAVE_RESPONSE_B_TO_C {
        public static final StationSaveResponse RESPONSE = new StationSaveResponse(
                LineDto.from(Line2.FIND_ENTITY),
                List.of(StationDto.from(STATION_B.FIND_ENTITY), StationDto.from(STATION_C.FIND_ENTITY)),
                List.of(SectionDto.from(SECTION_B_TO_C.FIND_ENTITY))
        );
    }

    public static class UP_END_STATION_SAVE_RESPONSE_D_TO_A {
        public static final StationSaveResponse RESPONSE = new StationSaveResponse(
                LineDto.from(Line2.FIND_ENTITY),
                List.of(StationDto.from(STATION_D.FIND_ENTITY), StationDto.from(STATION_A.FIND_ENTITY)),
                List.of(SectionDto.from(SECTION_D_TO_A.FIND_ENTITY))
        );
    }

    public static class DOWN_END_STATION_SAVE_RESPONSE_C_TO_E {
        public static final StationSaveResponse RESPONSE = new StationSaveResponse(
                LineDto.from(Line2.FIND_ENTITY),
                List.of(StationDto.from(STATION_C.FIND_ENTITY), StationDto.from(STATION_E.FIND_ENTITY)),
                List.of(SectionDto.from(SECTION_C_TO_E.FIND_ENTITY))
        );
    }
}
