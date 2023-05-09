package subway.domain;

import java.util.Objects;

public class Section {
    private final Long id;
    private final Station src;
    private final Station tar;
    private final Line line;
    private final Distance distance;

    public Section(Long id, Station src, Station tar, Line line, Distance distance) {
        this.id = id;
        this.src = src;
        this.tar = tar;
        this.line = line;
        this.distance = distance;
    }

    public boolean isSrc(Long sourceStationId) {
        return src.getId().equals(sourceStationId);
    }

    public boolean isTar(Long targetStationId) {
        return tar.getId().equals(targetStationId);
    }

    public boolean containsTheseStations(Long sourceStationId, Long targetStationId) {
        return (isSrc(sourceStationId) && isTar(targetStationId))
            || (isSrc(targetStationId) && isTar(sourceStationId));
    }

    public boolean hasShorterOrSameDistanceThan(Integer distance) {
        return this.distance.value() <= distance;
    }

    public Long getId() {
        return id;
    }

    public Station getSrc() {
        return src;
    }

    public Station getTar() {
        return tar;
    }

    public Line getLine() {
        return line;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Section section = (Section)o;
        return Objects.equals(src, section.src) && Objects.equals(tar, section.tar)
            && Objects.equals(line, section.line) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, tar, line, distance);
    }

}
