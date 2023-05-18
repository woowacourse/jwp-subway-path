package subway.dto.response;

import subway.domain.Line;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LineSectionResponse {
    private Long id;
    private String name;
    private String color;
    private List<String> stations;

    public LineSectionResponse(Long id, String name, String color, List<String> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineSectionResponse of(Line line) {
        List<String> stations = getStations(line);
        return new LineSectionResponse(line.getId(), line.getName(), line.getColor(), stations);
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

}
