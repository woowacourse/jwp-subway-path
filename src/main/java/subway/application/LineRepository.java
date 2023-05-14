package subway.application;

import subway.domain.Line;

public interface LineRepository {

    Line findById(Long linePropertyId);

    void removeSections(Long lineId);

    void save(Line line);
}
