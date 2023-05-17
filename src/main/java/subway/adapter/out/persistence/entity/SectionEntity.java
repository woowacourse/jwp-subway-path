package subway.adapter.out.persistence.entity;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntity {
    private final Long id;
    private final Long lineId;
    private final String upStationName;
    private final String downStationName;
    private final Long distance;

    public SectionEntity(final Long lineId, final String upStationName, final String downStationName, final Long distance) {
        this(null, lineId, upStationName, downStationName, distance);
    }

    public SectionEntity(final Long id, final Long lineId, final String upStationName, final String downStationName, final Long distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
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

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getDistance() {
        return distance;
    }
}
