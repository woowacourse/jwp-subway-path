package subway.dto.response;

import subway.domain.Line;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LineSectionResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<String> stations;

    private LineSectionResponse(Long id, String name, String color, List<String> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineSectionResponse of(Line line) {
        List<String> stations = getStations(line);
        return new LineSectionResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public static LineSectionResponse from(Long id, String name, String color, List<String> stations) {
        return new LineSectionResponse(id, name, color, stations);
    }

    private static List<String> getStations(Line line) {
        List<Station> stations = line.findStations();

        if (stations == null) {
            return Collections.emptyList();
        }

        return stations.stream()
                       .map(Station::getName)
                       .collect(Collectors.toList());
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

    public List<String> getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineSectionResponse that = (LineSectionResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations);
    }

    @Override
    public String toString() {
        return "LineSectionResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stations=" + stations +
                '}';
    }
}
