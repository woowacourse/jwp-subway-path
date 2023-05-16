package subway.domain;

import java.util.List;

public class SubwayGuide {

    private final Path path;

    private SubwayGuide(final Path path) {
        this.path = path;
    }

    public static SubwayGuide from(final List<Line> lines) {
        return new SubwayGuide(Path.from(lines));
    }

    public List<Station> getPathStations(final Station from, final Station to) {
        return path.getShortestPathStations(from, to);
    }

    public int getFare(final Station from, final Station to, final Age age) {
        return Fare.from(path.getShortestPathDistance(from, to))
                .applyDiscountRateOfAge(age)
                .getFare();
    }
}
