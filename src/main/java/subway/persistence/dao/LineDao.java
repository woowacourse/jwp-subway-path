package subway.persistence.dao;

import subway.domain.Line;
import subway.persistence.entity.LineEntity;

import java.util.List;

public interface LineDao {
    Long createLine(final LineEntity lineEntity);

    void deleteById(final Long lineIdRequest);

    List<LineEntity> findAll();

    LineEntity findById(final Long lineIdRequest);
}
