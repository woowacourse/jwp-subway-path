package subway.entity;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final String left;
    private final String right;
    private final Integer distance;

    public SectionEntity(final Long lineId, final String left, final String right, final Integer distance) {
        this(null, lineId, left, right, distance);
    }

    public SectionEntity(final Long id, final Long lineId, final String left, final String right, final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public static List<SectionEntity> toEntities(final Long lineId, final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        lineId,
                        section.getLeft().getName(),
                        section.getRight().getName(),
                        section.getDistance().getDistance())
                )
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public Integer getDistance() {
        return distance;
    }
}
