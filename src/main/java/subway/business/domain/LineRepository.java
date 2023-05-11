package subway.business.domain;

import java.util.List;

public interface LineRepository {

    long create(Line line);

    Line findById(Long id);

    List<Line> findAll();

    void save(Line line);
}
