package subway.domain.section;

public interface SectionRepository {

    void updateByLineNumber(final Sections sections, final Long lineNumber);

    Sections findByLineNumber(final Long lineNumber);

    Sections findByLineId(final Long lineId);
}
