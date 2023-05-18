package subway.application.port;

import subway.application.core.domain.Line;

import java.util.List;

public interface LineRepository {

    void insert(Line line);

    Line findById(Long linePropertyId);

    List<Line> findAll();
}
