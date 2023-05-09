package subway.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;

    private final LinkedList<Station> stations;
    private final Map<Map.Entry<Station, Station>, Integer> distances = new HashMap<>();

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Line line = (Line)o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    public void insertBoth(Station top, int distance, Station bottom) {
        validateEmptyStations();

        stations.add(top);
        stations.add(bottom);

        insertDistanceBetween(top, distance, bottom);
    }

    private void insertDistanceBetween(Station station, int distance, Station other) {
        distances.put(Map.entry(station, other), distance);
        distances.put(Map.entry(other, station), distance);
    }

    private void validateEmptyStations() {
        if (!stations.isEmpty()) {
            throw new IllegalStateException("빈 노선에만 한 번에 두 역을 추가할 수 있습니다");
        }
    }

    public int getDistanceBetween(Station from, Station to) {
        int distance = 0;
        List<Station> subStations = stations.subList(stations.indexOf(from), stations.indexOf(to) + 1);
        for (int i = 0; i + 1 < subStations.size(); i++) {
            Station station = subStations.get(i);
            Station other = subStations.get(i + 1);
            distance += distances.get(Map.entry(station, other));
        }
        return distance;
    }

    public void insertUpper(Station upper, int distance, Station lower) {
        int lowerIndex = stations.indexOf(lower);

        if (stations.indexOf(lower) == 0) {
            stations.add(stations.indexOf(lower), upper);
            insertDistanceBetween(upper, distance, lower);
            return;
        }

        // getDistanceBetween();

        distances.remove(Map.entry(lower, stations.get(lowerIndex - 1)));
        distances.remove(Map.entry(stations.get(lowerIndex - 1), lower));

        stations.add(stations.indexOf(lower), upper);
    }
}
