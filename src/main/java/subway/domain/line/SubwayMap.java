package subway.domain.line;

import subway.domain.path.SectionEdge;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {

    private final List<Line> lines;

    public SubwayMap(final List<Line> lines) {
        this.lines = lines;
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getOrderedStations().stream())
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<SectionEdge> getSectionEdge() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream().map(section -> new SectionEdge(line.getId(), section)))
                .collect(Collectors.toUnmodifiableList());
    }
}
