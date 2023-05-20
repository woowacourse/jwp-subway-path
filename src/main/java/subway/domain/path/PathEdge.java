package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;
import subway.domain.station.Station;

public class PathEdge extends DefaultWeightedEdge {

    private final Station sourceStation;
    private final Station targetStation;
    private final Line line;

    private PathEdge(final Station sourceStation, final Station targetStation, final Line line) {
        validateStation(sourceStation, targetStation);
        validateLine(sourceStation, targetStation, line);

        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.line = line;
    }

    private void validateStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("동일한 역으로 이동할 수 없습니다.");
        }
    }

    private void validateLine(final Station sourceStation, final Station targetStation, final Line line) {
        if (!(line.isRegisterStation(sourceStation) && line.isRegisterStation(targetStation))) {
            throw new IllegalArgumentException("해당 노선에 등록되지 않은 역입니다.");
        }
    }

    public static PathEdge of(final Station sourceStation, final Station targetStation, final Line line) {
        return new PathEdge(sourceStation, targetStation, line);
    }

    @Override
    protected Station getSource() {
        return sourceStation;
    }

    @Override
    protected Station getTarget() {
        return targetStation;
    }

    @Override
    protected double getWeight() {
        return line.getSections()
                .sections()
                .get(sourceStation)
                .findDistanceByStation(targetStation)
                .distance();
    }

    public boolean isSameLine(final PathEdge other) {
        return this.line.equals(other.line);
    }

    public Station getSourceStation() {
        return getSource();
    }

    public Station getTargetStation() {
        return getTarget();
    }

    public int getDistance() {
        return (int) getWeight();
    }

    public Line getLine() {
        return line;
    }
}
