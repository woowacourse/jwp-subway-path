package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private void validateIsLinked(List<Section> sections) {
        if (sections.size() + 1 != getStations().size()) {
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

    public boolean isSameName(String otherName) {
        return name.equals(otherName);
    }

    public void addSections(List<Section> saveSections) {
        sections.addAll(saveSections);
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
            throw new IllegalArgumentException("현재 삭제하려는 구간에는 노선에 존재하지 않는 역이 포함돼 있습니다.");
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
            return;
        }
        findSource.ifPresent(sections::remove);
        findTarget.ifPresent(sections::remove);
        }

    public boolean isEmpty() {
        return sections.isEmpty();
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
                .findAny().orElseThrow(() -> new IllegalArgumentException("종점역을 찾지 못했습니다"));
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
