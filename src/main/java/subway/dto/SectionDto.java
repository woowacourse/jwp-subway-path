package subway.dto;

import subway.domain.Distance;

public class SectionDto {
    private final StationDto upStation;
    private final StationDto downStation;
    private final int distance;

    private SectionDto(final StationDto upStation, final StationDto downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionDto of(StationDto upStation, StationDto downStation, Distance distance) {
        return new SectionDto(upStation, downStation, distance.getValue());
    }

    public StationDto getUpStation() {
        return upStation;
    }

    public StationDto getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
