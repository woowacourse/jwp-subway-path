package subway.persistence;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntity {
    private final String line;
    private final String upStation;
    private final String downStation;
    private final Long distance;

    public SectionEntity(final String line, final String upStation, final String downStation,
        final Long distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static List<SectionEntity> of(final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        section.getLine().getName(),
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        section.getDistance()
                ))
                .collect(Collectors.toList());
    }

    public String getLine() {
        return line;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
