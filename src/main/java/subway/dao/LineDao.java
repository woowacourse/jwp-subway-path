package subway.dao;

import subway.entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {

    LineEntity insert(LineEntity line);

    List<LineEntity> findAll();

    Optional<LineEntity> findById(Long id);
}
