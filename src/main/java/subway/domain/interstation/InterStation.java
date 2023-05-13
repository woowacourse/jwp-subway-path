package subway.domain.interstation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.domain.station.Station;
import subway.exception.interstation.InterStationException;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class InterStation {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public InterStation(final Long id,
                        final Station upStation,
                        final Station downStation,
                        final Distance distance) {
        validateStations(upStation, downStation);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public InterStation(final Long id, final Station upStation, final Station downStation, final long distance) {
        this(id, upStation, downStation, new Distance(distance));
    }

    public InterStation(final Station upStation, final Station downStation, final long distance) {
        this(null, upStation, downStation, distance);
    }

    private void validateStations(final Station firstStation, final Station secondStation) {
        if (firstStation.equals(secondStation)) {
            throw new InterStationException("상행역과 하행역이 같습니다.");
        }
    }

    public boolean contains(final Station station) {
        if (station == null) {
            return false;
        }
        return upStation.equals(station) || downStation.equals(station);
    }
}
