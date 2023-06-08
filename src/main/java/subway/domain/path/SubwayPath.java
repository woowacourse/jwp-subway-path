package subway.domain.path;

import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;

import java.util.ArrayList;
import java.util.List;

public class SubwayPath {

    final List<LinePath> linePaths;

    public SubwayPath() {
        this.linePaths = new ArrayList<>();
    }

    public void add(final Long lineId, final StationEdge stationEdge) {
        if (isTransfer(lineId)) {
            final LinePath linePath = new LinePath(lineId, StationEdges.from(stationEdge));
            linePaths.add(linePath);
            return;
        }
        final LinePath latestLinePath = getLatestLinePath();
        latestLinePath.add(stationEdge);
    }

    private boolean isTransfer(final Long lineId) {
        return linePaths.isEmpty() || !getLatestLinePath().isLineId(lineId);
    }

    private LinePath getLatestLinePath() {
        return linePaths.get(linePaths.size() - 1);
    }

    public int getTotalDistance() {
        return linePaths.stream()
                .mapToInt(LinePath::getTotalDistance)
                .sum();
    }

    public List<LinePath> getLinePaths() {
        return linePaths;
    }
}
