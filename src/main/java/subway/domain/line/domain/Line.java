package subway.domain.line.domain;

import subway.domain.lineDetail.entity.LineDetailEntity;
import subway.domain.station.entity.StationEntity;

import java.util.List;

public class Line {

    private final LineDetailEntity lineDetailEntity;
    private final List<StationEntity> stationEntities;

    public Line(final LineDetailEntity lineDetailEntity, final List<StationEntity> stationEntities) {
        this.lineDetailEntity = lineDetailEntity;
        this.stationEntities = stationEntities;
    }

    public LineDetailEntity getLineDetail() {
        return lineDetailEntity;
    }

    public List<StationEntity> getStations() {
        return stationEntities;
    }
}
