package subway.domain.station;

public class Section {
    private final Station from;
    private final Station to;
    private final StationDistance distance;

    public Section(final Station from, final Station to, final StationDistance distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
}
