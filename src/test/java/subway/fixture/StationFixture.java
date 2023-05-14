package subway.fixture;

import java.util.List;
import subway.dao.entity.StationEntity;
import subway.domain.station.Station;

public final class StationFixture {

    public static final StationEntity 잠실역_엔티티 = new StationEntity(1L, "잠실역");
    public static final Station 잠실역 = new Station(잠실역_엔티티.getName());

    public static final StationEntity 선릉역_엔티티 = new StationEntity(2L, "선릉역");
    public static final Station 선릉역 = new Station(선릉역_엔티티.getName());

    public static final StationEntity 강남역_엔티티 = new StationEntity(3L, "강남역");
    public static final Station 강남역 = new Station(강남역_엔티티.getName());

    public static final StationEntity 신림역_엔티티 = new StationEntity(4L, "신림역");
    public static final Station 신림역 = new Station(신림역_엔티티.getName());

    public static final StationEntity 복정역_엔티티 = new StationEntity(4L, "복정역");
    public static final Station 복정역 = new Station(복정역_엔티티.getName());

    public static final StationEntity 남위례역_엔티티 = new StationEntity(5L, "남위례역");
    public static final Station 남위례역 = new Station(남위례역_엔티티.getName());

    public static final StationEntity 산성역_엔티티 = new StationEntity(6L, "산성역");
    public static final Station 산성역 = new Station(산성역_엔티티.getName());

    public static List<StationEntity> 역_엔티티들() {
        return List.of(잠실역_엔티티, 선릉역_엔티티, 강남역_엔티티, 신림역_엔티티, 복정역_엔티티, 남위례역_엔티티, 산성역_엔티티);
    }
}
