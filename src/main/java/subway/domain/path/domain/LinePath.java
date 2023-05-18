package subway.domain.path.domain;

import subway.domain.line.entity.LineEntity;
import subway.domain.station.entity.StationEntity;

import java.util.List;

public class LinePath {

    private final LineEntity lineEntity;
    private final List<StationEntity> stationEntities;

    public LinePath(final LineEntity lineEntity, final List<StationEntity> stationEntities) {
        this.lineEntity = lineEntity;
        this.stationEntities = stationEntities;
    }

    public LineEntity getLine() {
        return lineEntity;
    }

    public List<StationEntity> getStations() {
        return stationEntities;
    }
}
