package subway.adapter.out.persistence.dao;

import subway.adapter.out.persistence.entity.LineEntity;

import java.util.List;
import java.util.Optional;

public interface LineDao {
    Long createLine(final LineEntity lineEntity);

    void deleteById(final Long lineIdRequest);

    List<LineEntity> findAll();

    Optional<LineEntity> findLineById(final Long lineId);
    List<LineEntity> findLinesById(final List<Long> lineId);
}
