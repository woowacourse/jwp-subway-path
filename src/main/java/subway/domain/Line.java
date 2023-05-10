package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class Line {

    private Long id;
    private String name;
    private String color;

    private List<Section> sections = new ArrayList<>();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validate(section);
        add(section);

    }

    private void validate(Section section) {
        validateConnectivity(section);
        validateDuplication(section);
        validateDistance(section);
    }

    private void add(Section target) {
        sections.stream()
                .filter(section -> section.includeSection(target))
                .findAny()
                .ifPresentOrElse((section) -> connectNewSection(section, target),
                        () -> sections.add(target));
    }

    private void connectNewSection(Section originSection, Section newSection) {
        if (originSection.containLeftStationOf(newSection)) {
            sections.add(originSection.makeLeftSectionTo(newSection));
        }
        if (originSection.containRightStationOf(newSection)) {
            sections.add(originSection.makeRightSectionTo(newSection));
        }
        sections.add(newSection);
        sections.remove(originSection);
    }

    private void validateConnectivity(Section target) {
        findSectionOf(section -> section.hasSameStationWith(target))
                .orElseThrow(() -> new IllegalStateException("노선과 연결되지 않는 역입니다."));
    }

    private void validateDuplication(Section target) {
        boolean existLeftStation = sections.stream().anyMatch(section -> section.containLeftStationOf(target));
        boolean existRightStation = sections.stream().anyMatch(section -> section.containRightStationOf(target));
        if (existLeftStation && existRightStation) {
            throw new IllegalStateException("이미 노선에 등록된 역입니다.");
        }
    }

    private void validateDistance(Section target) {
        findSectionOf(section -> section.includeSection(target))
                .filter(section -> !section.isDistanceBiggerThan(target))
                .ifPresent((ignored) -> {
                    throw new IllegalStateException(" 신규로 등록된 역이 기존 노선의 거리 범위를 벗어날 수 없습니다.");
                });
    }

    private Optional<Section> findSectionOf(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findAny();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
