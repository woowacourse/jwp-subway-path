package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private List<Section> sections;

    public Line() {
    }

    public Line(Long id, String name) {
        this(id, name, Collections.emptyList());
    }

    public Line(String name, List<Section> sections) {
        this(null, name, sections);
    }

    public Line(Long id, String name, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = new ArrayList<>(sections);
    }

    public void addSections(List<Section> saveSections) {
        sections.addAll(saveSections);
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
