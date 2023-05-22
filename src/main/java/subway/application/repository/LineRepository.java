package subway.application.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;

import java.util.List;

@Repository
public interface LineRepository {

    Line saveLine(Line line);

    Line findLineById(Long id);

    List<Line> findAllLines();

    void deleteLineById(Long id);
}
