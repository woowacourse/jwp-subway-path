package subway.domain.section;

public interface SectionRepository {
    Long save(Section section, Long lineId);

    void delete(Section removedSection);
}
