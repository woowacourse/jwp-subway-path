package subway.dao;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    public LineEntity save(final LineEntity entity) {
        return null;
    }

    public Optional<LineEntity> findById(final Long lineId) {
        return Optional.empty();
    }
}
