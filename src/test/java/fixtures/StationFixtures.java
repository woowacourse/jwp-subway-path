package fixtures;

import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;

public class StationFixtures {
    /**
     * Dummy 데이터
     */
    public static final String LINE2_NAME = "2호선";
    public static final String LINE7_NAME = "7호선";
    public static final String STATION_잠실역_NAME = "잠실역";
    public static final String STATION_건대역_NAME = "건대역";
    public static final String STATION_강변역_NAME = "강변역";
    public static final String STATION_대림역_NAME = "대림역";
    public static final String STATION_성수역_NAME = "성수역";
    public static final String STATION_신림역_NAME = "신림역";
    public static final String STATION_예외역_NAME = "예외역";
    public static final long LINE2_ID = 1L;
    public static final long LINE7_ID = 2L;
    public static final long STATION_잠실역_ID = 1L;
    public static final long STATION_건대역_ID = 2L;
    public static final long STATION_강변역_ID = 3L;
    public static final long STATION_대림역_ID = 3L;
    public static final long STATION_성수역_ID = 3L;
    public static final long STATION_예외역_ID = 3L;

    /**
     * 잠실 - 건대
     * 잠실 - 강변 - 건대
     */
    public static final long SECTION_잠실역_TO_건대역_ID = 1L;
    public static final long SECTION_잠실역_TO_강변역_ID = 2L;
    private static final long SECTION_강변역_TO_건대역_ID = 2L;
    private static final long SECTION_대림역_TO_잠실역_ID = 2L;
    private static final long SECTION_건대역_TO_성수역_ID = 2L;
    private static final long SECTION_잠실역_TO_예외역_ID = 2L;

    public static final int DISTANCE_잠실역_TO_건대역 = 10;
    public static final int DISTANCE_잠실역_TO_강변역 = 3;
    public static final int DISTANCE_강변역_TO_건대역 = 7;
    public static final int DISTANCE_대림역_TO_잠실역 = 20;
    public static final int DISTANCE_건대역_TO_성수역 = 15;
    public static final int DISTANCE_잠실역_TO_예외역 = 20;
    public static final int DISTANCE_대림역_TO_신림역 = 8;


    /**
     * request
     */
    public static final StationRequest REQUEST_잠실역_TO_건대역 = new StationRequest(STATION_잠실역_NAME, STATION_건대역_NAME, DISTANCE_잠실역_TO_건대역, LINE2_NAME);
    public static final StationRequest REQUEST_잠실역_TO_강변역 = new StationRequest(STATION_잠실역_NAME, STATION_강변역_NAME, DISTANCE_잠실역_TO_강변역, LINE2_NAME);
    public static final StationRequest REQUEST_강변역_TO_건대역 = new StationRequest(STATION_강변역_NAME, STATION_건대역_NAME, DISTANCE_강변역_TO_건대역, LINE2_NAME);
    public static final StationRequest REQUEST_대림역_TO_잠실역 = new StationRequest(STATION_대림역_NAME, STATION_잠실역_NAME, DISTANCE_대림역_TO_잠실역, LINE2_NAME);
    public static final StationRequest REQUEST_건대역_TO_성수역 = new StationRequest(STATION_건대역_NAME, STATION_성수역_NAME, DISTANCE_건대역_TO_성수역, LINE2_NAME);
    public static final StationRequest REQUEST_대림역_TO_신림역 = new StationRequest(STATION_대림역_NAME, STATION_신림역_NAME, DISTANCE_대림역_TO_신림역, LINE2_NAME);
    public static final StationRequest REQUEST_LONG_DISTANCE = new StationRequest(STATION_잠실역_NAME, STATION_예외역_NAME, DISTANCE_잠실역_TO_예외역, LINE2_NAME);

    /**
     * domain
     */
    public static final Line LINE2 = new Line(LINE2_ID, LINE2_NAME);
    public static final Station STATION_잠실역 = new Station(STATION_잠실역_ID, STATION_잠실역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_잠실역 = new Station(null, STATION_잠실역_NAME, LINE2);
    public static final Station STATION_건대역 = new Station(STATION_건대역_ID, STATION_건대역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_건대역 = new Station(null, STATION_건대역_NAME, LINE2);
    public static final Station STATION_강변역 = new Station(STATION_강변역_ID, STATION_강변역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_강변역 = new Station(null, STATION_강변역_NAME, LINE2);
    public static final Station STATION_대림역 = new Station(STATION_대림역_ID, STATION_대림역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_대림역 = new Station(null, STATION_대림역_NAME, LINE2);
    public static final Station STATION_성수역 = new Station(STATION_성수역_ID, STATION_성수역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_성수역 = new Station(null, STATION_성수역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_신림역 = new Station(null, STATION_신림역_NAME, LINE2);
    public static final Station STATION_예외역 = new Station(STATION_예외역_ID, STATION_예외역_NAME, LINE2);
    public static final Station STATION_TO_INSERT_예외역 = new Station(null, STATION_예외역_NAME, LINE2);
    public static final Section SECTION_잠실역_TO_건대역 = new Section(SECTION_잠실역_TO_건대역_ID, STATION_잠실역, STATION_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_AFTER_CALCULATE_잠실역_TO_건대역 = new Section(null, STATION_TO_INSERT_잠실역, STATION_TO_INSERT_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_TO_INSERT_잠실역_TO_건대역 = new Section(null, STATION_TO_INSERT_잠실역, STATION_TO_INSERT_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_TO_INSERT_AFTER_DELETE_잠실역_TO_건대역 = new Section(null, STATION_잠실역, STATION_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_잠실역_TO_강변역 = new Section(SECTION_잠실역_TO_강변역_ID, STATION_잠실역, STATION_강변역, DISTANCE_잠실역_TO_강변역);
    public static final Section SECTION_AFTER_CALCULATE_잠실역_TO_강변역 = new Section(null, STATION_잠실역, STATION_TO_INSERT_강변역, DISTANCE_잠실역_TO_강변역);
    public static final Section SECTION_TO_INSERT_잠실역_TO_강변역 = new Section(null, STATION_TO_INSERT_잠실역, STATION_TO_INSERT_강변역, DISTANCE_잠실역_TO_강변역);
    public static final Section SECTION_강변역_TO_건대역 = new Section(SECTION_강변역_TO_건대역_ID, STATION_강변역, STATION_건대역, DISTANCE_강변역_TO_건대역);
    public static final Section SECTION_AFTER_CALCULATE_강변역_TO_건대역 = new Section(null, STATION_TO_INSERT_강변역, STATION_건대역, DISTANCE_강변역_TO_건대역);
    public static final Section SECTION_TO_INSERT_강변역_TO_건대역 = new Section(null, STATION_TO_INSERT_강변역, STATION_TO_INSERT_건대역, DISTANCE_강변역_TO_건대역);
    public static final Section SECTION_대림역_TO_잠실역 = new Section(SECTION_대림역_TO_잠실역_ID, STATION_대림역, STATION_잠실역, DISTANCE_대림역_TO_잠실역);
    public static final Section SECTION_AFTER_CALCULATE_대림역_TO_잠실역 = new Section(null, STATION_TO_INSERT_대림역, STATION_잠실역, DISTANCE_대림역_TO_잠실역);
    public static final Section SECTION_건대역_TO_성수역 = new Section(SECTION_건대역_TO_성수역_ID, STATION_건대역, STATION_성수역, DISTANCE_건대역_TO_성수역);
    public static final Section SECTION_AFTER_CALCULATE_건대역_TO_성수역 = new Section(null, STATION_건대역, STATION_TO_INSERT_성수역, DISTANCE_건대역_TO_성수역);
    public static final Section SECTION_잠실역_TO_예외역 = new Section(SECTION_잠실역_TO_예외역_ID, STATION_잠실역, STATION_예외역, DISTANCE_잠실역_TO_예외역);
    public static final Section SECTION_AFTER_CALCULATE_잠실역_TO_예외역 = new Section(null, STATION_잠실역, STATION_TO_INSERT_예외역, DISTANCE_잠실역_TO_예외역);
    public static final Section SECTION_TO_INSERT_대림역_TO_신림역 = new Section(null, STATION_TO_INSERT_대림역, STATION_TO_INSERT_신림역, DISTANCE_대림역_TO_신림역);

    /**
     * entity
     */
    public static final LineEntity LINE2_INSERT_ENTITY = new LineEntity(null, LINE2_NAME);
    public static final LineEntity LINE7_INSERT_ENTITY = new LineEntity(null, LINE7_NAME);
    public static final LineEntity LINE2_FIND_ENTITY = new LineEntity(LINE2_ID, LINE2_NAME);
    public static final LineEntity LINE7_FIND_ENTITY = new LineEntity(LINE7_ID, LINE7_NAME);
    public static final StationEntity ENTITY_건대역_FIND = new StationEntity(STATION_건대역_ID, STATION_건대역_NAME, LINE2_ID);
    public static final StationEntity ENTITY_강변역_INSERT = new StationEntity(null, STATION_강변역_NAME, LINE2_ID);
    public static final StationEntity ENTITY_강변역_FIND = new StationEntity(STATION_강변역_ID, STATION_강변역_NAME, LINE2_ID);
    public static final SectionEntity ENTITY_잠실역_TO_강변역_INSERT = new SectionEntity(null, STATION_잠실역_ID, STATION_강변역_ID, DISTANCE_잠실역_TO_강변역, LINE2_ID);
    public static final SectionEntity ENTITY_잠실역_TO_건대역_FIND = new SectionEntity(SECTION_잠실역_TO_건대역_ID, STATION_잠실역_ID, STATION_건대역_ID, DISTANCE_잠실역_TO_건대역, LINE2_ID);
    public static final SectionEntity ENTITY_잠실역_TO_강변역_FIND = new SectionEntity(SECTION_잠실역_TO_강변역_ID, STATION_잠실역_ID, STATION_강변역_ID, DISTANCE_잠실역_TO_강변역, LINE2_ID);

    /**
     * response
     */
    public static final StationSaveResponse RESPONSE_SAVE_INITIAL_STATIONS_잠실역_TO_건대역 =
            new StationSaveResponse(
                    LineDto.from(LINE2),
                    List.of(StationDto.from(STATION_잠실역), StationDto.from(STATION_건대역)),
                    List.of(SectionDto.from(SECTION_잠실역_TO_건대역)));
}
