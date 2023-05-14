package subway.utils;

import subway.entity.SectionEntity;

import static subway.utils.LineEntityFixture.LINE_NUMBER_TWO_ENTITY;
import static subway.utils.StationEntityFixture.*;

public class SectionEntityFixture {

    public static final int DISTANCE = 5;

    public static final SectionEntity SULLEUNG_TO_JAMSIL_SECTION_ENTITY = new SectionEntity.Builder().downstreamId(JAMSIL_STATION_ENTITY.getId()).upstreamId(SULLEUNG_STATION_ENTITY.getId()).distance(DISTANCE).id(1L).lineId(LINE_NUMBER_TWO_ENTITY.getId()).build();
    public static final SectionEntity JAMSIL_TO_JAMSIL_NARU_SECTION_ENTITY = new SectionEntity.Builder().downstreamId(JAMSIL_NARU_STATION_ENTITY.getId()).upstreamId(JAMSIL_STATION_ENTITY.getId()).distance(DISTANCE).id(2L).lineId(LINE_NUMBER_TWO_ENTITY.getId()).build();
    public static final SectionEntity NO_ID_SULLEUNG_TO_JAMSIL_SECTION_ENTITY = new SectionEntity.Builder().downstreamId(JAMSIL_STATION_ENTITY.getId()).upstreamId(SULLEUNG_STATION_ENTITY.getId()).distance(DISTANCE).lineId(LINE_NUMBER_TWO_ENTITY.getId()).build();
    public static final SectionEntity NO_ID_JAMSIL_TO_JAMSIL_NARU_SECTION_ENTITY = new SectionEntity.Builder().downstreamId(JAMSIL_NARU_STATION_ENTITY.getId()).upstreamId(JAMSIL_STATION_ENTITY.getId()).distance(DISTANCE).lineId(LINE_NUMBER_TWO_ENTITY.getId()).build();
}
