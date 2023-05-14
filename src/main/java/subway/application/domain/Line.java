package subway.application.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class Line {

    private final LineProperty lineProperty;
    private final List<Section> sections;

    public Line(LineProperty lineProperty, List<Section> sections) {
        this.lineProperty = lineProperty;
        this.sections = sections;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validate(section);
        connectSection(section);
    }

    private void validate(Section section) {
        validateConnectivity(section);
        validateExistence(section);
        validateDistance(section);
    }

    private void validateConnectivity(Section target) {
        findSectionOf(section -> section.hasAnySameStationWith(target))
                .orElseThrow(() -> new IllegalStateException("노선과 연결되지 않는 역입니다."));
    }

    private void validateExistence(Section target) {
        boolean downBoundsExists = sections.stream().anyMatch(section -> section.containsUpBoundOf(target));
        boolean upBoundsExists = sections.stream().anyMatch(section -> section.containsDownBoundOf(target));

        if (downBoundsExists && upBoundsExists) {
            throw new IllegalStateException("이미 노선에 등록된 역입니다.");
        }
    }

    private void validateDistance(Section target) {
        findSectionOf(section -> section.overlaps(target))
                .filter(target::isDistanceBiggerOrEqualThan)
                .ifPresent((ignored) -> {
                    throw new IllegalStateException(" 신규로 등록된 역이 기존 노선의 거리 범위를 벗어날 수 없습니다.");
                });
    }

    private void connectSection(Section target) {
        findSectionOf(section -> section.overlaps(target))
                .ifPresentOrElse((section) -> connectTwoSection(section, target),
                        () -> sections.add(target));
    }

    private void connectTwoSection(Section originSection, Section newSection) {
        sections.add(originSection.makeConnectionTo(newSection));
        sections.add(newSection);
        sections.remove(originSection);
    }

    public void deleteStation(Station station) {
        validateStationExistence(station);
        Optional<Section> upBound = findSectionOf(section -> section.hasDownBound(station));
        Optional<Section> downBound = findSectionOf(section -> section.hasUpBound(station));

        if (upBound.isPresent() && downBound.isPresent()) {
            sections.add(upBound.get().merge(downBound.get()));
        }
        deleteOldSections(station);
    }

    private void validateStationExistence(Station station) {
        findSectionOf(section -> section.hasUpBound(station) || section.hasDownBound(station))
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 역에 대해 삭제할 수 없습니다."));
    }

    private void deleteOldSections(Station station) {
        findSectionOf(section -> section.hasDownBound(station) || section.hasUpBound(station))
                .ifPresent(sections::remove);
    }

    private Optional<Section> findSectionOf(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findAny();
    }

    public RouteMap routeMap() {
        return new RouteMap(sections);
    }

    public Long getId() {
        return lineProperty.getId();
    }

    public String getName() {
        return lineProperty.getName();
    }

    public String getColor() {
        return lineProperty.getColor();
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(lineProperty, line.lineProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineProperty);
    }
}
