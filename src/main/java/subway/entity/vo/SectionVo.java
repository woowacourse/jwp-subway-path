package subway.entity.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.entity.StationEntity;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionVo {
    private final StationEntity upStationEntity;
    private final StationEntity downStationEntity;
    private final Integer distance;

    public static SectionVo of(Long upStationId, String upStationName, Long downStationId, String downStationName, Integer distance) {
        return new SectionVo(StationEntity.of(upStationId, upStationName),
                StationEntity.of(downStationId, downStationName),
                distance);
    }
}
