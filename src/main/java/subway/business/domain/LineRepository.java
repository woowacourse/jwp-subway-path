package subway.business.domain;

import java.util.List;

public interface LineRepository {

    Line create(Line line);

    Line findById(Long id);

    List<Line> findAll();

    Line update(Line line);
}
