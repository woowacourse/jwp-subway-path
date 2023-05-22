package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public void deleteStation(final Station station) {
        lines.forEach(line -> line.deleteSections(station));
    }

    public List<Section> getAllSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }

    public List<Station> getAllStations() {
        return lines.stream()
                .flatMap(line -> line.getAllStations().stream())
                .collect(Collectors.toList());
    }

    public List<Line> getLines() {
        return List.copyOf(lines);
    }
}
