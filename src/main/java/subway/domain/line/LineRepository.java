package subway.domain.line;

import java.util.List;

public interface LineRepository {
    Line findById(Long id);

    List<Line> findAll();
}
