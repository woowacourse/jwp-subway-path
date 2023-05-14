package subway.dao;

import subway.Entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {

    LineEntity insert(LineEntity lineEntity);

    List<LineEntity> findAll();

    Optional<LineEntity> findById(long id);

    int update(LineEntity lineEntity);

    int deleteById(long id);
}
