package subway.domain.path;

import subway.domain.LineDirection;
import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;
import subway.exception.IllegalStationEdgeStateException;

import java.util.List;

public class LinePath {

    private final Long lineId;
    private final StationEdges stationEdges;

    public LinePath(final Long lineId, final StationEdges stationEdges) {
        this.lineId = lineId;
        this.stationEdges = stationEdges;
    }

    public void add(final StationEdge stationEdge) {
        validatePathContinuous(stationEdge);
        stationEdges.addStation(
                stationEdge.getDownStationId(),
                stationEdges.findDownEndStationId(),
                LineDirection.DOWN,
                stationEdge.getDistance()
        );
    }

    private void validatePathContinuous(final StationEdge stationEdge) {
        if (!stationEdge.isUpStationId(stationEdges.findDownEndStationId())) {
            throw new IllegalStationEdgeStateException("경로가 연결되지 않습니다.");
        }
    }

    public boolean isLineId(final Long id) {
        return this.lineId.equals(id);
    }

    public int getTotalDistance() {
        return stationEdges.getTotalDistance();
    }

    public Long getLineId() {
        return lineId;
    }

    public List<Long> getStationIds() {
        return stationEdges.getStationIdsInOrder();
    }
}
