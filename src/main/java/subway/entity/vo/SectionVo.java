package subway.entity.vo;

import java.util.Objects;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionVo sectionVo = (SectionVo) o;
        return Objects.equals(upStationEntity, sectionVo.upStationEntity) && Objects.equals(
                downStationEntity, sectionVo.downStationEntity) && Objects.equals(distance, sectionVo.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationEntity, downStationEntity, distance);
    }
}
