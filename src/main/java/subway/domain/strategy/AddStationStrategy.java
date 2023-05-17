package subway.domain.strategy;

import subway.domain.Graph;
import subway.domain.Station;

public interface AddStationStrategy {

    void addToTerminal(Graph graph,
                       Station existingStation,
                       Station upStation,
                       int distance);

    void addToMiddle(Graph graph,
                     Station existingStation,
                     Station newStation,
                     Station adjacentStation,
                     int distance);
}
