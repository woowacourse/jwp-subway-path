package subway.fixture;

import java.util.List;
import subway.application.dto.StationRequest;
import subway.dao.entity.StationEntity;
import subway.domain.station.Station;
import subway.domain.station.dto.StationRes;

public final class StationFixture {

    public static final StationEntity 잠실역_엔티티 = new StationEntity(1L, "잠실역");
    public static final Station 잠실역 = Station.create(잠실역_엔티티.getName());

    public static final StationEntity 선릉역_엔티티 = new StationEntity(2L, "선릉역");
    public static final Station 선릉역 = Station.create(선릉역_엔티티.getName());

    public static final StationEntity 강남역_엔티티 = new StationEntity(3L, "강남역");
    public static final Station 강남역 = Station.create(강남역_엔티티.getName());

    public static final StationEntity 신림역_엔티티 = new StationEntity(4L, "신림역");
    public static final Station 신림역 = Station.create(신림역_엔티티.getName());

    public static final StationEntity 복정역_엔티티 = new StationEntity(5L, "복정역");
    public static final Station 복정역 = Station.create(복정역_엔티티.getName());

    public static final StationEntity 남위례역_엔티티 = new StationEntity(6L, "남위례역");
    public static final Station 남위례역 = Station.create(남위례역_엔티티.getName());

    public static final StationEntity 산성역_엔티티 = new StationEntity(7L, "산성역");
    public static final Station 산성역 = Station.create(산성역_엔티티.getName());

    public static final StationRequest 역_요청_정보 = new StationRequest(잠실역.name().name());

    public static List<StationEntity> 역_엔티티들() {
        return List.of(잠실역_엔티티, 선릉역_엔티티, 강남역_엔티티, 신림역_엔티티, 복정역_엔티티, 남위례역_엔티티, 산성역_엔티티);
    }

    public static List<StationRes> 역_응답_정보() {
        final StationRes 잠실역_응답 = new StationRes(잠실역_엔티티.getId(), 잠실역_엔티티.getName());
        final StationRes 선릉역_응답 = new StationRes(선릉역_엔티티.getId(), 선릉역_엔티티.getName());
        final StationRes 강남역_응답 = new StationRes(강남역_엔티티.getId(), 강남역_엔티티.getName());
        return List.of(잠실역_응답, 선릉역_응답, 강남역_응답);
    }
}
