package subway.domain.strategy;

import subway.domain.Direction;
import subway.domain.Graph;
import subway.domain.Station;

public interface AddStationStrategy {

    void addToTerminal(Graph graph,
                       Direction direction,
                       Station existingStation,
                       Station upStation,
                       int distance);

    void addToMiddle(Graph graph,
                     Direction direction,
                     Station existingStation,
                     Station newStation,
                     Station adjacentStation,
                     int distance);
}
