package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private List<Station> stations;

    public Stations(final List<Station> stations) {
        this.stations = stations;
    }

    public Stations(final Station upEnd, final Station downEnd) {
        validateInitDuplication(upEnd, downEnd);

        stations = new ArrayList<>();
        stations.add(upEnd);
        stations.add(downEnd);
    }

    public void addStation(final Station station) {
        validateDuplication(station);
        stations.add(station);
    }

    private void validateInitDuplication(final Station upEnd, final Station downEnd) {
        if (upEnd.equals(downEnd)) {
            throw new IllegalArgumentException("이미 등록된 이름입니다. 다른 이름을 입력해주세요.");
        }
    }

    private void validateDuplication(final Station station) {
        if (stations.contains(station)) {
            throw new IllegalArgumentException("이미 등록된 이름입니다. 다른 이름을 입력해주세요.");
        }
    }

    public List<Station> getStations() {
        return stations;
    }
}
