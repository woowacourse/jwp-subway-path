package subway.domain.station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {

    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        validateUniqueness(stations);
        this.stations = new ArrayList<>(stations);
    }

    public static Stations emptyStations() {
        return new Stations(new ArrayList<>());
    }

    private void validateUniqueness(final List<Station> stations) {
        if (countDistinct(stations) != stations.size()) {
            throw new IllegalArgumentException("중복되는 역이 있습니다.");
        }
    }

    private long countDistinct(final List<Station> stations) {
        return stations.stream().distinct().count();
    }

    public Stations addStations(final Stations other) {
        stations.addAll(other.stations);
        return new Stations(
                stations.stream()
                        .distinct()
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public List<Station> stations() {
        return stations;
    }
}
