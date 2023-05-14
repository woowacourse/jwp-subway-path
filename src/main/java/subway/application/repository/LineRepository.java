package subway.application.repository;

import subway.application.domain.Line;

public interface LineRepository {

    Line findById(Long linePropertyId);

    void removeSections(Long lineId);

    void save(Line line);
}
