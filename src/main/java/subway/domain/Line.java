package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class Line {

    private final Long id;
    private final String name;
    private final String color;

    private final List<Section> sections;

    public Line(final String name, final String color) {
        this(null, name, color, new ArrayList<>());
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new ArrayList<>());
    }

    public Line(final Long id, final String name, final String color, final List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>(sections);
    }

    public void addSection(final Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validate(section);
        add(section);
    }

    private void validate(final Section section) {
        validateConnectivity(section);
        validateDuplication(section);
        validateDistance(section);
    }

    private void add(final Section target) {
        findSectionOf(section -> section.includeSection(target))
                .ifPresentOrElse(section -> connectNewSection(section, target),
                        () -> sections.add(target));
    }

    private void connectNewSection(final Section originSection, final Section newSection) {
        if (originSection.containLeftStationOf(newSection)) {
            sections.add(originSection.makeLeftSectionTo(newSection));
        }
        if (originSection.containRightStationOf(newSection)) {
            sections.add(originSection.makeRightSectionTo(newSection));
        }
        sections.add(newSection);
        sections.remove(originSection);
    }

    private void validateConnectivity(final Section target) {
        findSectionOf(section -> section.hasSameStationWith(target))
                .orElseThrow(() -> new IllegalStateException("노선과 연결되지 않는 역입니다."));
    }

    private void validateDuplication(final Section target) {
        final boolean existLeftStation = sections.stream().anyMatch(section -> section.containLeftStationOf(target));
        final boolean existRightStation = sections.stream().anyMatch(section -> section.containRightStationOf(target));
        if (existLeftStation && existRightStation) {
            throw new IllegalStateException("이미 노선에 등록된 역입니다.");
        }
    }

    private void validateDistance(final Section target) {
        findSectionOf(section -> section.includeSection(target))
                .filter(section -> !section.isDistanceBiggerThan(target))
                .ifPresent(ignored -> {
                    throw new IllegalStateException(" 신규로 등록된 역이 기존 노선의 거리 범위를 벗어날 수 없습니다.");
                });
    }

    public void deleteStation(final Station station) {
        final Optional<Section> leftSection = findSectionOf(section -> section.hasRightStation(station));
        final Optional<Section> rightSection = findSectionOf(section -> section.hasLeftStation(station));

        if (leftSection.isEmpty() && rightSection.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 역에 대해 삭제할 수 없습니다.");
        }

        if (leftSection.isPresent() && rightSection.isPresent()) {
            final Section newSection = leftSection.get().merge(rightSection.get());
            sections.add(newSection);
        }
        deleteOldSections(station);
    }

    private void deleteOldSections(final Station station) {
        findSectionOf(section -> section.hasRightStation(station))
                .ifPresent(sections::remove);
        findSectionOf(section -> section.hasLeftStation(station))
                .ifPresent(sections::remove);
    }

    private Optional<Section> findSectionOf(final Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findAny();
    }

    public LineRouteMap routeMap() {
        return new LineRouteMap(sections);
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

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
