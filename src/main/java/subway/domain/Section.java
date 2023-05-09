package subway.domain;

public class Section {
    private final Station src;
    private final Station tar;
    private final Line line;
    private final Distance distance;

    public Section(Station src, Station tar, Line line, Distance distance) {
        this.src = src;
        this.tar = tar;
        this.line = line;
        this.distance = distance;
    }

    public boolean isSrc(Station station) {
        return src.equals(station);
    }

    public boolean isTar(Station station) {
        return tar.equals(station);
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
}
