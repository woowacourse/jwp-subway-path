package subway.persistence;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntity {
    private final String upStation;
    private final String downStation;
    private final Long distance;
    private final Long lineId;

    public SectionEntity(final String upStation, final String downStation, final Long distance, final Long lineId) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static List<SectionEntity> of(final Long lineId, final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        section.getDistance(),
                        lineId
                ))
                .collect(Collectors.toList());
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

    public Long getLineId() {
        return lineId;
    }
}
