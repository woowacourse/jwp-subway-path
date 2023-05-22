package subway.line;

import subway.line.application.exception.InvalidSectionColorException;
import subway.line.application.exception.InvalidLineNameException;
import subway.line.application.exception.LineNotHavingSectionException;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Line {
    public static final int MIN_STATIONS_SIZE = 2;

    private final Long id;
    private String name;
    private String color;
    private final List<Section> sections;
    private Station head;

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
        this.head = null;
    }

    public Line(Long id, String name, String color, Station head) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
        this.head = head;
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

    public Station getHead() {
        return head;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Optional<Section> findSectionByPreviousStation(Station station) {
        return sections.stream()
                .filter(section -> section.getPreviousStation().equals(station))
                .findAny();
    }

    public void addSections(List<Section> sections) {
        this.sections.addAll(sections);
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public boolean hasSection() {
        return this.sections.isEmpty();
    }

    public Optional<Section> findSectionByNextStation(Station station) {
        return sections.stream()
                .filter(section -> section.getNextStation().equals(station))
                .findAny();
    }

    public List<Station> findAllStationsOrderByUp() {
        final var stations = new ArrayList<Station>();

        var station = head;
        while (station != null) {
            Station currentStation = station;

            final var nextSection = findSectionByPreviousStation(currentStation);

            if (nextSection.isPresent()) {
                stations.add(nextSection.get().getPreviousStation());
                station = nextSection.get().getNextStation();
            }
            if (nextSection.isEmpty()) {
                station = null;
            }
        }

        return stations;
    }

    public Distance findDistanceBetween(Station stationA, Station stationB) {
        final var section = sections.stream()
                .filter(sec ->
                        (sec.getPreviousStation().equals(stationA) && sec.getNextStation().equals(stationB))
                                || (sec.getPreviousStation().equals(stationB) && sec.getNextStation().equals(stationA))
                ).findAny()
                .orElseThrow(LineNotHavingSectionException::new);
        return section.getDistance();
    }

    public void updateSection(Section section) {
        final var sectionToUpdate = sections.stream().filter(sec -> sec.getId().equals(section.getId()))
                .findAny()
                .orElseThrow(LineNotHavingSectionException::new);
        final var sectionIndex = this.sections.indexOf(sectionToUpdate);
        sections.set(sectionIndex, section);
    }

    public void changeHead(Station station) {
        if (station == null) {
            this.head = new EmptyStation();
            return;
        }
        this.head = station;
    }

    public void changeName(String name) {
        if (name == null) {
            throw new InvalidLineNameException();
        }
        this.name = name;
    }

    public void changeColor(String color) {
        if (color == null) {
            throw new InvalidSectionColorException();
        }
        this.color = color;
    }

    public void clearSection() {
        this.sections.clear();
    }

    public int getStationsSize() {
        return findAllStationsOrderByUp().size();
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
