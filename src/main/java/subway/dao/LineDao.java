package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import subway.entity.LineEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LineDao {

    LineEntity saveLine(LineEntity lineEntity);

    Optional<LineEntity> findByName(final String name);

    Optional<LineEntity> findById(final Long id);

    List<LineEntity> findAll();

    void deleteLine(Long id);
}
