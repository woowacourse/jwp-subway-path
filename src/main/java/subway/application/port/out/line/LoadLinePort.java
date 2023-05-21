package subway.application.port.out.line;

import java.util.List;
import java.util.Optional;
import subway.domain.Line;

public interface LoadLinePort {

    boolean checkExistById(long lineId);

    Optional<Line> findById(long lineId);

    List<Line> findAll();

    boolean checkExistByName(String name);
}
