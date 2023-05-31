package fixtures;

import subway.domain.station.Station;
import subway.dto.StationRequest;
import subway.entity.StationEntity;

import static fixtures.LineFixtures.*;
import static fixtures.SectionFixtures.*;

public class StationFixtures {

    /**
     * data
     */
    public static final long STATION_잠실역_ID = 1L;
    public static final long STATION_건대역_ID = 2L;
    public static final long STATION_강변역_ID = 3L;
    public static final long STATION_대림역_ID = 3L;
    public static final long STATION_성수역_ID = 3L;
    public static final long STATION_온수역_ID = 3L;
    public static final long STATION_철산역_ID = 4L;
    public static final String STATION_잠실역_NAME = "잠실역";
    public static final String STATION_건대역_NAME = "건대역";
    public static final String STATION_강변역_NAME = "강변역";
    public static final String STATION_대림역_NAME = "대림역";
    public static final String STATION_성수역_NAME = "성수역";
    public static final String STATION_신림역_NAME = "신림역";
    public static final String STATION_예외역_NAME = "예외역";
    public static final String STATION_온수역_NAME = "온수역";
    public static final String STATION_철산역_NAME = "철산역";

    /**
     * request
     */
    public static final StationRequest REQUEST_잠실역_TO_건대역 = new StationRequest(STATION_잠실역_NAME, STATION_건대역_NAME, DISTANCE_잠실역_TO_건대역, LINE2_ID);
    public static final StationRequest REQUEST_잠실역_TO_강변역 = new StationRequest(STATION_잠실역_NAME, STATION_강변역_NAME, DISTANCE_잠실역_TO_강변역, LINE2_ID);
    public static final StationRequest REQUEST_강변역_TO_건대역 = new StationRequest(STATION_강변역_NAME, STATION_건대역_NAME, DISTANCE_강변역_TO_건대역, LINE2_ID);
    public static final StationRequest REQUEST_대림역_TO_잠실역 = new StationRequest(STATION_대림역_NAME, STATION_잠실역_NAME, DISTANCE_대림역_TO_잠실역, LINE2_ID);
    public static final StationRequest REQUEST_건대역_TO_성수역 = new StationRequest(STATION_건대역_NAME, STATION_성수역_NAME, DISTANCE_건대역_TO_성수역, LINE2_ID);
    public static final StationRequest REQUEST_대림역_TO_신림역 = new StationRequest(STATION_대림역_NAME, STATION_신림역_NAME, DISTANCE_대림역_TO_신림역, LINE2_ID);
    public static final StationRequest REQUEST_LONG_DISTANCE = new StationRequest(STATION_잠실역_NAME, STATION_예외역_NAME, DISTANCE_잠실역_TO_예외역, LINE2_ID);

    /**
     * domain
     */
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
    public static final Station STATION_온수역 = new Station(STATION_온수역_ID, STATION_온수역_NAME, LINE7);
    public static final Station STATION_철산역 = new Station(STATION_철산역_ID, STATION_철산역_NAME, LINE7);

    /**
     * entity
     */
    public static final StationEntity ENTITY_건대역_FIND = new StationEntity(STATION_건대역_ID, STATION_건대역_NAME, LINE2_ID);
    public static final StationEntity ENTITY_강변역_INSERT = new StationEntity(null, STATION_강변역_NAME, LINE2_ID);
    public static final StationEntity ENTITY_강변역_FIND = new StationEntity(STATION_강변역_ID, STATION_강변역_NAME, LINE2_ID);
}
