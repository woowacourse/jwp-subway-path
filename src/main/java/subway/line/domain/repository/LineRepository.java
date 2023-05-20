package subway.line.domain.repository;

import subway.line.domain.Line;

public interface LineRepository {

    Long insert(final Line line);

    Line findAllStationsByLineId(final Long lineId);

    void updateById(final Long id, final Line line);

    void deleteById(final Long id);

    Line findById(Long id);

}
