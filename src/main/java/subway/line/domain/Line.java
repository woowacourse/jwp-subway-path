package subway.line.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.section.domain.Sections;

import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class Line {
    private final String name;
    private final String color;
    private final Sections sections;
    
    public Line(final String name, final String color) {
        validateNameAndColor(name, color);
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }
    
    private void validateNameAndColor(final String name, final String color) {
        validateNullOrEmpty(name);
        validateNullOrEmpty(color);
    }
    
    private void validateNullOrEmpty(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("노선의 이름이나 색상은 null 또는 빈값일 수 없습니다.");
        }
    }
    
    public void initAddStation(final String leftAdditional, final String rightAdditional, final long distance) {
        sections.initAddStation(leftAdditional, rightAdditional, distance);
    }
    
    public void addStation(
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        sections.addStation(base, direction, additionalStation, distance);
    }
    
    public void removeStation(final String station) {
        sections.removeStation(station);
    }
}
