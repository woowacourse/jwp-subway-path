package subway.utils;

import subway.station.entity.StationEntity;

public class StationEntityFixture {

    public static final StationEntity JAMSIL_STATION_ENTITY = new StationEntity.Builder().id(1L).name("잠실").build();
    public static final StationEntity NO_ID_JAMSIL_STATION_ENTITY = new StationEntity.Builder().name("잠실").build();
    public static final StationEntity SULLEUNG_STATION_ENTITY = new StationEntity.Builder().id(2L).name("선릉").build();
    public static final StationEntity NO_ID_SULLEUNG_STATION_ENTITY = new StationEntity.Builder().name("선릉").build();
    public static final StationEntity JAMSIL_NARU_STATION_ENTITY = new StationEntity.Builder().id(3L).name("잠실나루").build();
    public static final StationEntity NO_ID_JAMSIL_NARU_STATION_ENTITY = new StationEntity.Builder().name("잠실나루").build();

}
