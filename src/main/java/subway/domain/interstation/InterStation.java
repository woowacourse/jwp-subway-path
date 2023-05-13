package subway.domain.interstation;

import lombok.Getter;
import subway.domain.station.Station;
import subway.exception.BusinessException;

@Getter
public class InterStation {

    private final Long id;
    private final Station firstStation;
    private final Station secondStation;
    private final long distance;

    public InterStation(final Long id, final Station firstStation, final Station secondStation,
            final long distance) {
        validateDistance(distance);
        this.id = id;
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.distance = distance;
    }

    public InterStation(final Station firstStation, final Station secondStation, final long distance) {
        this(null, firstStation, secondStation, distance);
    }

    private void validateDistance(final long distance) {
        if (distance < 0) {
            throw new BusinessException("거리는 양수이어야 합니다.");
        }
    }

    public boolean containsAll(final Station frontStation, final Station backStation) {
        return firstStation.equals(frontStation) && secondStation.equals(backStation)
                || firstStation.equals(backStation) && secondStation.equals(firstStation);
    }

    public boolean contains(final Station station) {
        if (station == null) {
            return false;
        }
        return firstStation.equals(station) || secondStation.equals(station);
    }
}
