package subway.dao;

import subway.entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {

    Optional<LineEntity> findById(Long id);

    List<LineEntity> findAll();

    LineEntity insert(LineEntity lineEntity);

    void update(LineEntity newLineEntity);

    void deleteById(Long id);
}
