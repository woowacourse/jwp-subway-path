package subway.dao.section;

import java.util.Map;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

public class SectionEntity {
    private final Long id;
    private final Long firstStationId;
    private final Long secondStationId;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(final Long firstStationId, final Long secondStationId, final Integer distance,
                         final Long lineId) {
        this(null, firstStationId, secondStationId, distance, lineId);
    }

    public SectionEntity(final Long id, final Long firstStationId, final Long secondStationId, final Integer distance,
                         final Long lineId) {
        this.id = id;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Section toSection(final Map<Long, Station> stationsById) {
        return new Section(
                stationsById.get(this.firstStationId),
                stationsById.get(this.secondStationId),
                new StationDistance(distance)
        );
    }

    public Long getId() {
        return id;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getSecondStationId() {
        return secondStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
