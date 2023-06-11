package subway.domain.section;

import java.util.List;

public interface SectionRepository {

    void save(final Sections sections, final Long lineNumber);

    Sections findByLineNumber(final Long lineNumber);

    Sections findByLineId(final Long lineId);

    List<Sections> findAllByLineIds(final List<Long> lineIds);
}
