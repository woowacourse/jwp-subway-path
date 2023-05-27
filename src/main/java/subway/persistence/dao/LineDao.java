package subway.persistence.dao;

import subway.Entity.LineEntity;
import subway.domain.line.Line;

import java.util.List;
import java.util.Optional;

public interface LineDao {

    LineEntity insert(Line line);

    List<LineEntity> findAll();

    Optional<LineEntity> findById(Long id);

    void update(LineEntity lineEntity);

    void deleteById(Long id);
}
