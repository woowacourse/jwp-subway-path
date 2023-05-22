package subway;

import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.dao.vo.SectionStationMapper;

public class TestFeature {

    // 라인과 관련된 테스트 상수
    public static final LineEntity LINE_ENTITY_2호선 = new LineEntity(1L, "2호선", "초록색");
    public static final LineEntity LINE_ENTITY_1호선 = new LineEntity(2L, "1호선", "파랑색");
    public static final LineEntity LINE_ENTITY_3호선 = new LineEntity(3L, "7호선", "올리브색");


    // 역과 관련된 테스트 상수
    public static final StationEntity STATION_ENTITY_서울대입구 = new StationEntity(1L, "서울대입구역");
    public static final StationEntity STATION_ENTITY_봉천역 = new StationEntity(2L, "봉천역");
    public static final StationEntity STATION_ENTITY_낙성대역 = new StationEntity(3L, "낙성대역");
    public static final StationEntity STATION_ENTITY_사당역 = new StationEntity(4L, "사당역");
    public static final StationEntity STATION_ENTITY_방배역 = new StationEntity(5L, "방배역");
    public static final StationEntity STATION_ENTITY_교대역 = new StationEntity(6L, "교대역");
    public static final StationEntity STATION_ENTITY_인천역 = new StationEntity(7L, "인천역");
    public static final StationEntity STATION_ENTITY_동인천역 = new StationEntity(8L, "동인천역");



    // 섹션과 관련된 테스트 상수
    public static final SectionEntity SECTION_ENTITY_봉천_서울대입구 = new SectionEntity(1L, 1L, 2L, 1L, 5);
    public static final SectionEntity SECTION_ENTITY_서울대입구_사당 = new SectionEntity(2L, 1L, 1L, 4L, 7);
    public static final SectionEntity SECTION_ENTITY_방배_봉천 = new SectionEntity(3L, 1L, 5L, 2L, 8);
    public static final SectionEntity SECTION_ENTITY_인천_방배 = new SectionEntity(4L, 1L, 7L, 5L, 3);
    public static final SectionEntity SECTION_ENTITY_사당_동인천 = new SectionEntity(5L, 1L, 4L, 8L, 4);


    // 역-섹션 매퍼와 관련된 테스트 상수
    public static final SectionStationMapper SECTION_STATION_MAPPER_봉천_서울대입구 = new SectionStationMapper(1L, 2L, "봉천역", 1L, "서울대입구역", 5);
    public static final SectionStationMapper SECTION_STATION_MAPPER_서울대입구_사당 = new SectionStationMapper(2L, 1L, "서울대입구역", 4L, "사당역", 7);
    public static final SectionStationMapper SECTION_STATION_MAPPER_방배_봉천 = new SectionStationMapper(3L, 5L, "방배역", 2L, "봉천역", 8);
    public static final SectionStationMapper SECTION_STATION_MAPPER_인천_방배 = new SectionStationMapper(4L, 7L, "인천역", 5L, "방배역", 3);
    public static final SectionStationMapper SECTION_STATION_MAPPER_사당_동인천 = new SectionStationMapper(5L, 4L, "사당역", 8L, "동인천역", 4);
    public static final SectionStationMapper SECTION_STATION_MAPPER_사당_낙성 = new SectionStationMapper(6L, 4L, "사당역", 3L, "낙성대역", 7);

}
