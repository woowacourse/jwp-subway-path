package subway.business.domain;

import java.util.List;

public interface LineRepository {

    long save(Line line);

    Line findById(Long id);

    List<Line> findAll();
}
