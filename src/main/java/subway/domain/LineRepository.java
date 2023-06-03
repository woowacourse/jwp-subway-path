package subway.domain;

import java.util.List;

public interface LineRepository {

    Long save(final Line line);

    void deleteById(final Long id);

    Line findById(final Long id);

    List<Line> findAll();
}
