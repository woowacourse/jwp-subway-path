package subway.business.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Subway {
    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = lines;
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }

    public List<Line> getLines() {
        return lines;
    }
}
