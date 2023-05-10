package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public void addStation(Station station) {
        validateDuplication(station);
        stations.add(station);
    }

    private void validateDuplication(Station station) {
        boolean result = stations.stream().anyMatch(each -> each.isSameName(station));

        if (result) {
            throw new IllegalArgumentException("[ERROR] 동일한 이름의 역을 중복으로 등록할 수 없습니다.");
        }
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public void remove(Station station) {
        if (!stations.contains(station)) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 역을 삭제할 수 없습니다.");
        }
        stations.remove(station);
    }

    public Station find(Station station) {
        return stations.stream()
                .filter(each -> each.isSameName(station))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("[ERROR] 등록되지 않은 역을 조회했습니다."));
    }
}
