package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (!sections.isEmpty()) {
            validateConnectivity(section);
        }
        sections.add(section);
    }

    private void validateConnectivity(Section section) {
        sections.stream()
                .filter(sec -> sec.hasSameStationWith(section))
                .findAny()
                .orElseThrow(()-> new IllegalStateException("노선과 연결되지 않는 역입니다."));
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
