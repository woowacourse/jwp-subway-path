package fixtures;

import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;

public class StationFixtures {
    /**
     * Dummy 데이터
     */
    public static final String DUMMY_LINE2_NAME = "2호선";
    public static final String DUMMY_LINE7_NAME = "7호선";
    public static final String DUMMY_STATION_잠실역_NAME = "잠실역";
    public static final String DUMMY_STATION_건대역_NAME = "건대역";
    public static final String DUMMY_STATION_강변역_NAME = "강변역";
    public static final long DUMMY_LINE2_ID = 1L;
    public static final long DUMMY_잠실_INSERTED_ID = 1L;
    public static final long DUMMY_건대_INSERTED_ID = 2L;
    public static final long DUMMY_강변_INSERTED_ID = 3L;

    /**
     * 잠실 - 건대
     * 잠실 - 강변 - 건대
     */
    public static final long DUMMY_SECTION_잠실_TO_건대_ID = 1L;
    public static final long DUMMY_SECTION_잠실_TO_강변_ID = 2L;
    public static final long DUMMY_SECTION_강변_TO_건대_ID = 3L;

    public static final int DUMMY_DISTANCE_잠실_TO_건대 = 10;
    public static final int DUMMY_DISTANCE_잠실_TO_강변 = 3;
    public static final int DUMMY_DISTANCE_강변_TO_건대 = 7;


    /**
     * request
     */
    public static final StationRequest 잠실_TO_건대_REQUEST = new StationRequest(DUMMY_STATION_잠실역_NAME, DUMMY_STATION_건대역_NAME, DUMMY_DISTANCE_잠실_TO_건대, DUMMY_LINE2_NAME);
    public static final StationRequest 잠실_TO_강변_REQUEST = new StationRequest(DUMMY_STATION_잠실역_NAME, DUMMY_STATION_강변역_NAME, DUMMY_DISTANCE_잠실_TO_강변, DUMMY_LINE2_NAME);
    public static final StationRequest 강변_TO_건대_REQUEST = new StationRequest(DUMMY_STATION_강변역_NAME, DUMMY_STATION_건대역_NAME, DUMMY_DISTANCE_강변_TO_건대, DUMMY_LINE2_NAME);

    /**
     * entity
     */
    public static final LineEntity LINE2_INSERT_ENTITY = new LineEntity(null, DUMMY_LINE2_NAME);
    public static final LineEntity LINE7_INSERT_ENTITY = new LineEntity(null, DUMMY_LINE7_NAME);
    public static final LineEntity LINE2_FIND_ENTITY = new LineEntity(DUMMY_LINE2_ID, DUMMY_LINE2_NAME);
    public static final StationEntity 잠실_INSERT_ENTITY = new StationEntity(null, DUMMY_STATION_잠실역_NAME, DUMMY_LINE2_ID);
    public static final StationEntity 잠실_FIND_ENTITY = new StationEntity(DUMMY_잠실_INSERTED_ID, DUMMY_STATION_잠실역_NAME, DUMMY_LINE2_ID);
    public static final StationEntity 건대_INSERT_ENTITY = new StationEntity(null, DUMMY_STATION_건대역_NAME, DUMMY_LINE2_ID);
    public static final StationEntity 건대_FIND_ENTITY = new StationEntity(DUMMY_건대_INSERTED_ID, DUMMY_STATION_건대역_NAME, DUMMY_LINE2_ID);
    public static final StationEntity 강변_INSERT_ENTITY = new StationEntity(null, DUMMY_STATION_강변역_NAME, DUMMY_LINE2_ID);
    public static final StationEntity 강변_FIND_ENTITY = new StationEntity(DUMMY_강변_INSERTED_ID, DUMMY_STATION_강변역_NAME, DUMMY_LINE2_ID);
    public static final SectionEntity 잠실_TO_건대_INSERT_SECTION_ENTITY = new SectionEntity(null, DUMMY_잠실_INSERTED_ID, DUMMY_건대_INSERTED_ID, DUMMY_LINE2_ID, DUMMY_DISTANCE_잠실_TO_건대);
    public static final SectionEntity 잠실_TO_건대_FIND_SECTION_ENTITY = new SectionEntity(DUMMY_SECTION_잠실_TO_건대_ID, DUMMY_잠실_INSERTED_ID, DUMMY_건대_INSERTED_ID, DUMMY_LINE2_ID, DUMMY_DISTANCE_잠실_TO_건대);
    public static final SectionEntity 잠실_TO_강변_INSERT_SECTION_ENTITY = new SectionEntity(null, DUMMY_잠실_INSERTED_ID, DUMMY_강변_INSERTED_ID, DUMMY_LINE2_ID, DUMMY_DISTANCE_잠실_TO_강변);
    public static final SectionEntity 강변_TO_건대_INSERT_SECTION_ENTITY = new SectionEntity(null, DUMMY_강변_INSERTED_ID, DUMMY_건대_INSERTED_ID, DUMMY_LINE2_ID, DUMMY_DISTANCE_강변_TO_건대);

    /**
     * response
     */
    public static final StationSaveResponse SAVE_INITIAL_STATIONS_잠실_TO_건대_RESPONSE = new StationSaveResponse(DUMMY_LINE2_ID, List.of(DUMMY_잠실_INSERTED_ID, DUMMY_건대_INSERTED_ID), List.of(DUMMY_SECTION_잠실_TO_건대_ID));
    public static final StationSaveResponse SAVE_NEW_STATION_잠실_TO_강변_RESPONSE = new StationSaveResponse(DUMMY_LINE2_ID, List.of(DUMMY_강변_INSERTED_ID), List.of(DUMMY_SECTION_잠실_TO_강변_ID, DUMMY_SECTION_강변_TO_건대_ID));
    public static final StationSaveResponse SAVE_NEW_STATION_강변_TO_건대_RESPONSE = new StationSaveResponse(DUMMY_LINE2_ID, List.of(DUMMY_강변_INSERTED_ID), List.of(DUMMY_SECTION_잠실_TO_강변_ID, DUMMY_SECTION_강변_TO_건대_ID));
}
