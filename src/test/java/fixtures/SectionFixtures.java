package fixtures;

import subway.domain.section.Section;
import subway.entity.SectionEntity;

import static fixtures.LineFixtures.LINE2_ID;
import static fixtures.StationFixtures.*;

public class SectionFixtures {

    /**
     * data
     */
    public static final long SECTION_잠실역_TO_건대역_ID = 1L;
    public static final long SECTION_잠실역_TO_건대역_ID_AFTER_INSERT_AGAIN = 4L;
    public static final long SECTION_잠실역_TO_강변역_ID = 2L;
    public static final long SECTION_강변역_TO_건대역_ID = 2L;
    public static final long SECTION_대림역_TO_잠실역_ID = 2L;
    public static final long SECTION_건대역_TO_성수역_ID = 2L;
    public static final long SECTION_온수역_TO_철산역_ID = 2L;

    public static final int DISTANCE_잠실역_TO_건대역 = 10;
    public static final int DISTANCE_잠실역_TO_강변역 = 3;
    public static final int DISTANCE_강변역_TO_건대역 = 7;
    public static final int DISTANCE_대림역_TO_잠실역 = 20;
    public static final int DISTANCE_건대역_TO_성수역 = 15;
    public static final int DISTANCE_잠실역_TO_예외역 = 20;
    public static final int DISTANCE_대림역_TO_신림역 = 8;
    public static final int DISTANCE_온수역_TO_철산역 = 11;

    /**
     * domain
     */
    public static final Section SECTION_잠실역_TO_건대역 = new Section(SECTION_잠실역_TO_건대역_ID, STATION_잠실역, STATION_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_TO_INSERT_잠실역_TO_건대역 = new Section(null, STATION_TO_INSERT_잠실역, STATION_TO_INSERT_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_TO_INSERT_AFTER_DELETE_잠실역_TO_건대역 = new Section(null, STATION_잠실역, STATION_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_AFTER_DELETE_잠실역_TO_건대역 = new Section(SECTION_잠실역_TO_건대역_ID_AFTER_INSERT_AGAIN, STATION_잠실역, STATION_건대역, DISTANCE_잠실역_TO_건대역);
    public static final Section SECTION_잠실역_TO_강변역 = new Section(SECTION_잠실역_TO_강변역_ID, STATION_잠실역, STATION_강변역, DISTANCE_잠실역_TO_강변역);
    public static final Section SECTION_AFTER_CALCULATE_잠실역_TO_강변역 = new Section(null, STATION_잠실역, STATION_TO_INSERT_강변역, DISTANCE_잠실역_TO_강변역);
    public static final Section SECTION_TO_INSERT_잠실역_TO_강변역 = new Section(null, STATION_TO_INSERT_잠실역, STATION_TO_INSERT_강변역, DISTANCE_잠실역_TO_강변역);
    public static final Section SECTION_강변역_TO_건대역 = new Section(SECTION_강변역_TO_건대역_ID, STATION_강변역, STATION_건대역, DISTANCE_강변역_TO_건대역);
    public static final Section SECTION_AFTER_CALCULATE_강변역_TO_건대역 = new Section(null, STATION_TO_INSERT_강변역, STATION_건대역, DISTANCE_강변역_TO_건대역);
    public static final Section SECTION_TO_INSERT_강변역_TO_건대역 = new Section(null, STATION_TO_INSERT_강변역, STATION_TO_INSERT_건대역, DISTANCE_강변역_TO_건대역);
    public static final Section SECTION_대림역_TO_잠실역 = new Section(SECTION_대림역_TO_잠실역_ID, STATION_대림역, STATION_잠실역, DISTANCE_대림역_TO_잠실역);
    public static final Section SECTION_TO_INSERT_대림역_TO_잠실역 = new Section(null, STATION_TO_INSERT_대림역, STATION_TO_INSERT_잠실역, DISTANCE_대림역_TO_잠실역);
    public static final Section SECTION_건대역_TO_성수역 = new Section(SECTION_건대역_TO_성수역_ID, STATION_건대역, STATION_성수역, DISTANCE_건대역_TO_성수역);
    public static final Section SECTION_TO_INSERT_건대역_TO_성수역 = new Section(null, STATION_TO_INSERT_건대역, STATION_TO_INSERT_성수역, DISTANCE_건대역_TO_성수역);
    public static final Section SECTION_TO_INSERT_대림역_TO_신림역 = new Section(null, STATION_TO_INSERT_대림역, STATION_TO_INSERT_신림역, DISTANCE_대림역_TO_신림역);
    public static final Section SECTION_TO_INSERT_온수역_TO_철산역 = new Section(null, STATION_온수역, STATION_철산역, DISTANCE_온수역_TO_철산역);
    public static final Section SECTION_온수역_TO_철산역 = new Section(SECTION_온수역_TO_철산역_ID, STATION_온수역, STATION_철산역, DISTANCE_온수역_TO_철산역);

    /**
     * entity
     */
    public static final SectionEntity ENTITY_잠실역_TO_강변역_INSERT = new SectionEntity(null, STATION_잠실역_ID, STATION_강변역_ID, DISTANCE_잠실역_TO_강변역, LINE2_ID);
    public static final SectionEntity ENTITY_잠실역_TO_건대역_FIND = new SectionEntity(SECTION_잠실역_TO_건대역_ID, STATION_잠실역_ID, STATION_건대역_ID, DISTANCE_잠실역_TO_건대역, LINE2_ID);
    public static final SectionEntity ENTITY_잠실역_TO_강변역_FIND = new SectionEntity(SECTION_잠실역_TO_강변역_ID, STATION_잠실역_ID, STATION_강변역_ID, DISTANCE_잠실역_TO_강변역, LINE2_ID);
}
