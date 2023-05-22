package subway.application.port.out.line;

import java.util.List;
import java.util.Optional;
import subway.domain.line.Line;
import subway.domain.station.Station;

public interface LoadLinePort {

    boolean checkExistById(long lineId);

    Optional<Line> findById(long lineId);

    List<Line> findAll();

    List<Long> findContainingLineIdsByStation(Station station);

    boolean checkExistByName(String name);
}
