package subway.domain;

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
        sections.add(newSections);
        newSections.createInitialSection(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Station addStation(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Sections sections = findSectionsOf(line);
        return sections.addStation(upStation, downStation, distance);
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

    public int findDistanceBetween(final Line line, final Station upStation, final Station downStation) {
        final Sections sections = findSectionsOf(line);

        if (!sections.containsStation(upStation) || !sections.containsStation(downStation)) {
            throw new IllegalArgumentException("존재하지 않는 역입니다!");
        }
        return sections.findDistanceBetween(upStation, downStation);
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
}
