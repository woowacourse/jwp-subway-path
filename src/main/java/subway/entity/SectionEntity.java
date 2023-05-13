package subway.entity;

import static java.util.stream.Collectors.toList;

import java.util.List;
import subway.domain.Line;

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

    public static List<SectionEntity> of(final Line line, final long lineId) {
        return line.getSections().stream()
                .map(section -> new SectionEntity(
                        section.getStartName(),
                        section.getEndName(),
                        section.getDistanceValue(),
                        lineId
                ))
                .collect(toList());
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
