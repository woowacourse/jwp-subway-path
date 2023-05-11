package subway.repository;

import subway.domain.Line;

public interface LineRepository {

    Line findById(long lineId);

    void updateSections(Line line);
}
