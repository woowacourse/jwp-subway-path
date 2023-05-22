package subway.domain.strategy;

import subway.domain.graph.Graph;
import subway.domain.Station;

public interface StationAddStrategy {

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
