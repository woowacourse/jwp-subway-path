package subway.fixture;

import subway.persistence.dao.entity.StationEntity;

public class StationEntityFixture {
    public static StationEntity JAMSIL_ENTITY = new StationEntity(1L, "잠실");
    public static StationEntity SEONLEUNG_ENTITY = new StationEntity(2L, "선릉");
    public static StationEntity GANGNAM_ENTITY = new StationEntity(3L, "강남");
    public static StationEntity YUKSAM_ENTITY = new StationEntity(4L, "역삼");

    public static StationEntity JANGJI_ENTITY = new StationEntity(5L, "장지");
    public static StationEntity SEOKCHON_ENTITY = new StationEntity(6L, "석촌");
    public static StationEntity MONGCHON_ENTITY = new StationEntity(7L, "몽촌토성");

    public static StationEntity JAMSIL_NO_ID_ENTITY = new StationEntity(null, "잠실");
    public static StationEntity SEONLEUNG_NO_ID_ENTITY = new StationEntity(null, "선릉");
    public static StationEntity GANGNAM_NO_ID_ENTITY = new StationEntity(null, "강남");
    public static StationEntity YUKSAM_NO_ID_ENTITY = new StationEntity(null, "역삼");

    public static StationEntity JANGJI_NO_ID_ENTITY = new StationEntity(null, "장지");
    public static StationEntity SEOKCHON_NO_ID_ENTITY = new StationEntity(null, "석촌");
    public static StationEntity MONGCHON_NO_ID_ENTITY = new StationEntity(null, "몽촌토성");
}
