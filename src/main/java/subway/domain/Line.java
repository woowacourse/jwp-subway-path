package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Line {

    private final Long id;
    private final String name;
    private final List<Section> sections;

    public Line(String name, List<Section> sections) {
        this(null, name, sections);
    }

    public Line(Long id, String name, List<Section> sections) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.sections = new ArrayList<>(sections);
        validateIsLinked(sections);
    }

    private void validateIsLinked(List<Section> sections) {
        if (getStations().size() != 0 && sections.size() + 1 != getStations().size()) {
            throw new IllegalArgumentException("연결되지 않은 역이 있습니다");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("아름에는 빈 문자가 들어올 수 없습니다.");
        }
    }

    public boolean isSame(Line other) {
        return name.equals(other.getName());
    }

    public void addSection(Section newSection) {
        validateDuplicatedName(newSection);

        Optional<Section> downSection = findBySource(newSection.getSource());
        Optional<Section> upSection = findByTarget(newSection.getTarget());
        if (downSection.isPresent()) {
            Section section = downSection.get();
            sections.remove(section);
            sections.add(new Section(section.getSource(), newSection.getTarget(), newSection.getDistance()));
            sections.add(new Section(newSection.getTarget(), section.getTarget(),
                    section.getDistance() - newSection.getDistance()));
            return;
        }
        if (upSection.isPresent()) {
            Section section = upSection.get();
            sections.remove(section);
            sections.add(new Section(section.getSource(), newSection.getSource(),
                    section.getDistance() - newSection.getDistance()));
            sections.add(new Section(newSection.getSource(), section.getTarget(), newSection.getDistance()));
            return;
        }
        if (sections.isEmpty() || isLastSection(newSection)) {
            sections.add(newSection);
            return;
        }
        throw new IllegalArgumentException("연결되지 않은 역이 있습니다");
    }

    private boolean isLastSection(Section newSection) {
        return findByTarget(newSection.getSource()).isPresent() || findBySource(newSection.getTarget()).isPresent();
    }

    private Optional<Section> findByTarget(Station target) {
        return sections.stream()
                .filter(section -> section.getTarget().isSameName(target))
                .findAny();
    }

    private Optional<Section> findBySource(Station source) {
        return sections.stream()
                .filter(section -> section.getSource().isSameName(source))
                .findAny();
    }

    private void validateDuplicatedName(Section section) {
        if (isDuplicatedName(section.getSource()) && isDuplicatedName(section.getTarget())) {
            throw new IllegalArgumentException("두 역이 이미 모두 존재합니다.");
        }
    }

    private boolean isDuplicatedName(Station station) {
        return sections.stream()
                .anyMatch(it -> it.isAnySame(station));
    }

    public void removeStation(Station station) {
        if (!isDuplicatedName(station)) {
            throw new NoSuchElementException("현재 삭제하려는 구간에는 노선에 존재하지 않는 역이 포함돼 있습니다.");
        }
        Optional<Section> findSource = findBySource(station);
        Optional<Section> findTarget = findByTarget(station);

        if (findSource.isPresent() && findTarget.isPresent()) {
            Section downSection = findSource.get();
            Section upSection = findTarget.get();

            sections.remove(downSection);
            sections.remove(upSection);

            sections.add(new Section(upSection.getSource(), downSection.getTarget(), downSection.getDistance()
                    + upSection.getDistance()));
        }
        findSource.ifPresent(sections::remove);
        findTarget.ifPresent(sections::remove);
    }

    public List<Station> getStations() {
        Map<String, String> sourceToTarget = sections.stream()
                .collect(Collectors.toMap(Section::getSourceName, Section::getTargetName));
        return findFirstStationName(sourceToTarget)
                .map(station -> getSortedStations(sourceToTarget, station))
                .orElse(Collections.emptyList());
    }

    private List<Station> getSortedStations(Map<String, String> sourceToTarget, String firstStation) {
        List<String> stations = new ArrayList<>();
        String station = firstStation;
        stations.add(station);
        while (sourceToTarget.containsKey(station)) {
            String nextStation = sourceToTarget.get(station);
            stations.add(nextStation);
            station = nextStation;
        }
        return stations.stream()
                .map(this::findStationByName)
                .collect(Collectors.toList());
    }

    private Optional<String> findFirstStationName(Map<String, String> sourceToTarget) {
        Set<String> sources = new HashSet<>(sourceToTarget.keySet());
        sources.removeAll(sourceToTarget.values());
        return sources.stream()
                .findAny();
    }

    public Station findStationByName(String name) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getSource(), section.getTarget()))
                .filter(station -> station.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당하는 역이 없습니다."));
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return id.equals(line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
