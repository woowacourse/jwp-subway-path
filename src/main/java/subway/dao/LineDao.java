package subway.dao;

import subway.Entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {

    LineEntity insert(final LineEntity lineEntity);

    List<LineEntity> findAll();

    Optional<LineEntity> findById(final long id);

    int update(final LineEntity lineEntity);

    int deleteById(final long id);

    int countByName(final String name);

    int countByColor(final String color);
}
