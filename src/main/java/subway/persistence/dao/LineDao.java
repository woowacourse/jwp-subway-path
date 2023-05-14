package subway.persistence.dao;

import subway.persistence.entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {
    Long createLine(final LineEntity lineEntity);

    void deleteById(final Long lineIdRequest);

    List<LineEntity> findAll();

    Optional<LineEntity> findById(final Long lineId);
}
