package subway.line.application.strategy.stationdeleting;

import subway.line.Line;
import subway.line.domain.station.Station;

public interface StationDeletingStrategy {
    boolean support(Line line, Station station);

    void deleteStation(Line line, Station station);
}
