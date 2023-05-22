package subway.domain;

import subway.domain.graph.Graph;
import subway.dto.PathDto;
import subway.exeption.LineNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Subway {
    private final List<Sections> sections;

    public Subway(final List<Sections> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Map<Line, List<Station>> findAll() {
        return sections.stream()
                .collect(Collectors.toMap(
                        Sections::getLine,
                        Sections::findAllStationsInOrder));
    }

    public List<Line> findAllLines() {
        return sections.stream()
                .map(Sections::getLine)
                .collect(Collectors.toList());
    }

    public void createSectionsOf(final Line line, final Graph graph) {
        final Sections newSections = new Sections(line, graph);
        sections.add(newSections);
    }

    public void addSection(final Line line, final Section section) {
        final Sections newSections = findSectionsOf(line);
        newSections.createInitialSection(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public void addStation(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Sections sections = findSectionsOf(line);
        sections.addStation(upStation, downStation, distance);
    }

    public Sections findSectionsOf(final Line line) {
        return sections.stream().filter(element -> element.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException("해당 노선이 존재하지 않습니다."));
    }

    public List<Station> findStationsInOrder(final Line line) {
        final Sections sections = findSectionsOf(line);
        return sections.findAllStationsInOrder();
    }

    public Station findStationBefore(final Line line, final Station station) {
        final Sections sections = findSectionsOf(line);
        return sections
                .findAdjacentStationOf(station, element -> sections.getUpStationsOf(station));
    }

    public Station findStationAfter(final Line line, final Station station) {
        final Sections sections = findSectionsOf(line);
        return sections
                .findAdjacentStationOf(station, element -> sections.getDownStationsOf(station));
    }

    public PathDto findShortestPath(final Station source, final Station target) {
        final ShortestPath shortestPath = ShortestPath.from(sections);

        final List<Station> stations = shortestPath.path(source, target);
        double distance = shortestPath.distance(source, target);

        return new PathDto(stations, distance);
    }
}
