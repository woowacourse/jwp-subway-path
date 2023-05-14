package subway.persistence.entity;

import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntity {
    private final Long lineId;
    private final String upStation;
    private final String downStation;
    private final Long distance;

    public SectionEntity(final Long lineId, final String upStation, final String downStation, final Long distance) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static List<SectionEntity> of(final Long lineId, final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        lineId,
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        section.getDistance()
                ))
                .collect(Collectors.toList());
    }


    public Long getLineId() {
        return lineId;
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
