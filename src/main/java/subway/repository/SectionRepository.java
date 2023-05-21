package subway.repository;

import subway.domain.Sections;

public interface SectionRepository {

    void insertAll(final Long lineId, final Sections sections);

    Sections findByLineId(final Long lineId);

    void deleteAllByLineId(final Long lineId);
}
