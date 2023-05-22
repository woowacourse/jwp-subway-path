package subway.domain;

import java.util.List;
import subway.domain.vo.Distance;

public class Path {

    private final List<StationInformation> stationInformations;
    private final Distance distance;

    public Path(final List<StationInformation> stationInformations, final Distance distance) {
        this.stationInformations = stationInformations;
        this.distance = distance;
    }

    public long findHighestLineCharge() {
        return stationInformations.stream()
            .mapToLong(StationInformation::getLineCharge)
            .max()
            .orElse(0L);
    }

    public List<StationInformation> getStationInformations() {
        return stationInformations;
    }

    public Distance getDistance() {
        return distance;
    }
}
