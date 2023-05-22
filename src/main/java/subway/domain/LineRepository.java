package subway.domain;

import java.util.Optional;

public interface LineRepository {

    Optional<Line> findById(Long id);

    Lines findAll();

    Line save(Line line);

    void delete(Line line);

}
