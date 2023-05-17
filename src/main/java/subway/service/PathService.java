package subway.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import subway.domain.Path;
import subway.domain.Station;

@Service
public class PathService {

    public Optional<Path> getShortestPath(Station departure, Station arrival) {
        return Optional.empty();
    }
}
