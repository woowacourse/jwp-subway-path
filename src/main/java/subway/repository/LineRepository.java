package subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;

@Transactional
@Repository
public interface LineRepository {
    Line insert(final Line line);

    List<Line> findAll();

    Line findById(final Long id);

    void update(final Line line);

    void deleteById(final Long id);
}
