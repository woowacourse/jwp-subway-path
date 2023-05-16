package subway.application.repository;

import subway.application.domain.Line;

public interface LineRepository {

    void insert(Line line);

    Line findById(Long linePropertyId);

    void removeSections(Long lineId);
}
