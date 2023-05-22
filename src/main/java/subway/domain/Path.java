package subway.domain;

import java.util.List;
import subway.domain.vo.Distance;

public class Path {

    private final List<StationInformation> stationInformations;
    private final Distance distance;
    private final int age;

    public Path(final List<StationInformation> stationInformations, final Distance distance, final int age) {
        this.stationInformations = stationInformations;
        this.distance = distance;
        this.age = age;
    }

    public List<StationInformation> getStationInformations() {
        return stationInformations;
    }

    public Distance getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }
}
