package subway.domain.line;

import subway.domain.station.Stations;

public class Line {
    private final Stations stations;
    private final LineName lineName;
    private final LineColor lineColor;

    public Line(final Stations stations, final LineName lineName, final LineColor lineColor) {
        this.stations = stations;
        this.lineName = lineName;
        this.lineColor = lineColor;
    }
}
