package subway.repository;

import java.util.List;
import subway.domain.Line;

public interface LineRepository {

    Line findById(long lineId);

    void updateSections(Line line);

    List<Line> findAll();
}
