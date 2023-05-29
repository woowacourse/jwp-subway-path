package subway.entity;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import subway.domain.Section;

public class SectionEntity {

    private final Long id;
    private final String startStationName;
    private final String endStationName;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(
            final String startStationName,
            final String endStationName,
            final Integer distance,
            final Long lineId
    ) {
        this(null, startStationName, endStationName, distance, lineId);
    }

    public SectionEntity(
            final Long id,
            final String startStationName,
            final String endStationName,
            final Integer distance,
            final Long lineId
    ) {
        this.id = id;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static List<SectionEntity> of(final List<Section> sections, final Long lineId) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        section.getStartName(),
                        section.getEndName(),
                        section.getDistanceValue(),
                        lineId
                ))
                .collect(toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(startStationName, that.startStationName)
                && Objects.equals(endStationName, that.endStationName) && Objects.equals(distance,
                that.distance) && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startStationName, endStationName, distance, lineId);
    }

    public Long getId() {
        return id;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
