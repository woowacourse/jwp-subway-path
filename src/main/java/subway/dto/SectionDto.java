package subway.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.Distance;
import subway.domain.Section;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionDto {
    private final StationDto upStation;
    private final StationDto downStation;
    private final int distance;

    public static SectionDto of(StationDto upStation, StationDto downStation, Distance distance) {
        return new SectionDto(upStation, downStation, distance.getValue());
    }
}
