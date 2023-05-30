package subway.fixtures.station;

import subway.business.domain.Station;
import subway.persistence.entity.StationEntity;

public class StationFixture {
    public static Station 강남역 = new Station(1L, "강남역");
    public static StationEntity 강남역Entity = new StationEntity(1L, "강남역");
    public static Station 역삼역 = new Station(2L, "역삼역");
    public static StationEntity 역삼역Entity = new StationEntity(2L, "역삼역");
    public static Station 잠실역 = new Station(3L, "잠실역");
    public static StationEntity 잠실역Entity = new StationEntity(3L, "잠실역");
    public static Station 성수역 = new Station(4L, "성수역");
    public static StationEntity 성수역Entity = new StationEntity(4L, "성수역");
}
