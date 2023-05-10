package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Line {
    private Long id;
    private String name;
    private List<Section> sections;

    public Line() {
    }

    public Line(String name, List<Section> sections) {
        this(null, name, sections);
    }

    public Line(Long id, String name, List<Section> sections) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.sections = new ArrayList<>(sections);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("아름에는 빈 문자가 들어올 수 없습니다.");
        }
    }

    public void addSections(List<Section> saveSections) {
        sections.addAll(saveSections);
    }

    public void addSection(Section newSection) {
        validateDuplicatedName(newSection);

        Optional<Section> downSection = findBySource(newSection);
        Optional<Section> upSection = findByTarget(newSection);
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
        sections.add(newSection);
    }

    private Optional<Section> findByTarget(Section newSection) {
        return sections.stream()
                .filter(section -> section.getTarget().isSameName(newSection.getTarget()))
                .findAny();
    }

    private Optional<Section> findBySource(Section newSection) {
        return sections.stream()
                .filter(section -> section.getSource().isSameName(newSection.getSource()))
                .findAny();
    }

    private void validateDuplicatedName(Section section) {
        if (isDuplicatedName(section.getSource()) && isDuplicatedName(section.getTarget())) {
            throw new IllegalArgumentException("두 역이 이미 모두 존재합니다.");
        }
    }

    private boolean isDuplicatedName(Station source) {
        return sections.stream()
                .anyMatch(it -> it.isAnySame(source));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
