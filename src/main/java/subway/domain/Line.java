package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import subway.exception.section.AlreadyExistSectionException;
import subway.exception.EmptyNameException;
import subway.exception.line.LineUnconnectedException;
import subway.exception.station.StationNotFoundException;

public class Line {
    private final Long id;
    private final String name;
    private final List<Section> sections;

    public Line(final String name, final List<Section> sections) {
        this(null, name, sections);
    }

    public Line(final Long id, String name, List<Section> sections) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.sections = new ArrayList<>(sections);
        validateIsLinked(sections);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new EmptyNameException();
        }
    }

    private void validateIsLinked(List<Section> sections) {
        if (sections.size() + 1 != getStations().size()) {
            throw new LineUnconnectedException();
        }
    }

    public void addSection(Section newSection) {
        validateDuplicatedName(newSection);

        Optional<Section> downSection = findBySource(newSection.getSource());
        Optional<Section> upSection = findByTarget(newSection.getTarget());
        if (downSection.isPresent()) {
            addAsUpSection(newSection, downSection.get());
            return;
        }
        if (upSection.isPresent()) {
            addAsDownSection(newSection, upSection.get());
            return;
        }
        if (sections.isEmpty() || isLastSection(newSection)) {
            sections.add(newSection);
            return;
        }
        throw new LineUnconnectedException();
    }

    private void validateDuplicatedName(Section section) {
        if (isDuplicatedName(section.getSource()) && isDuplicatedName(section.getTarget())) {
            throw new AlreadyExistSectionException();
        }
    }

    private boolean isDuplicatedName(Station station) {
        return sections.stream()
                .anyMatch(it -> it.isAnySame(station));
    }

    private Optional<Section> findBySource(Station source) {
        return sections.stream()
                .filter(section -> section.getSource().isSameName(source))
                .findAny();
    }

    private Optional<Section> findByTarget(Station target) {
        return sections.stream()
                .filter(section -> section.getTarget().isSameName(target))
                .findAny();
    }

    private void addAsUpSection(final Section newSection, final Section downSection) {
        sections.remove(downSection);
        sections.add(new Section(downSection.getSource(), newSection.getTarget(), newSection.getDistance()));
        sections.add(new Section(newSection.getTarget(), downSection.getTarget(),
                downSection.getDistance() - newSection.getDistance()));
    }

    private void addAsDownSection(final Section newSection, final Section upSection) {
        sections.remove(upSection);
        sections.add(new Section(upSection.getSource(), newSection.getSource(),
                upSection.getDistance() - newSection.getDistance()));
        sections.add(new Section(newSection.getSource(), upSection.getTarget(), newSection.getDistance()));
    }

    private boolean isLastSection(Section newSection) {
        return findByTarget(newSection.getSource()).isPresent() || findBySource(newSection.getTarget()).isPresent();
    }

    public boolean isSame(Line other) {
        return name.equals(other.getName());
    }

    public boolean isSameName(String otherName) {
        return name.equals(otherName);
    }

    public void removeStation(Station station) {
        validateStationExistence(station);
        Optional<Section> findSource = findBySource(station);
        Optional<Section> findTarget = findByTarget(station);

        if (findSource.isPresent() && findTarget.isPresent()) {
            removeStationExistsBetweenStation(findSource.get(), findTarget.get());
            return;
        }
        findSource.ifPresent(sections::remove);
        findTarget.ifPresent(sections::remove);
        }

    private void validateStationExistence(final Station station) {
        if (!containsStation(station)) {
            throw new StationNotFoundException("현재 삭제하려는 구간에는 노선에 존재하지 않는 역이 포함돼 있습니다.");
        }
    }

    public boolean containsStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private void removeStationExistsBetweenStation(final Section downSection, final Section upSection) {
        sections.remove(downSection);
        sections.remove(upSection);

        final Section mergedSection = new Section(
                upSection.getSource(),
                downSection.getTarget(),
                downSection.getDistance() + upSection.getDistance()
        );
        sections.add(mergedSection);
    }

    public List<Station> getStations() {
        Map<Station, Station> sourceToTarget = sections.stream()
                .collect(Collectors.toMap(Section::getSource, Section::getTarget));
        Station firstStation = findFirstStation(sourceToTarget);
        return getSortedStations(sourceToTarget, firstStation);
    }

    private List<Station> getSortedStations(Map<Station, Station> sourceToTarget, Station firstStation) {
        List<Station> stations = new ArrayList<>();
        Station station = firstStation;
        stations.add(station);
        while (sourceToTarget.containsKey(station)) {
            Station nextStation = sourceToTarget.get(station);
            stations.add(nextStation);
            station = nextStation;
        }
        return stations;
    }

    private Station findFirstStation(Map<Station, Station> sourceToTarget) {
        Set<Station> sources = new HashSet<>(sourceToTarget.keySet());
        sources.removeAll(sourceToTarget.values());
        return sources.stream()
                .findAny().orElseThrow(() -> new StationNotFoundException("종점역을 찾지 못했습니다"));
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
    }

}
