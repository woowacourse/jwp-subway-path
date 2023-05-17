package subway.adapter.out.persistence.dao;

import subway.adapter.out.persistence.entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {
    Long createLine(final LineEntity lineEntity);

    void deleteById(final Long lineIdRequest);

    List<LineEntity> findAll();

    Optional<LineEntity> findById(final Long lineId);
}
