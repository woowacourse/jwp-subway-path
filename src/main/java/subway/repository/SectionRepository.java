package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;
import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;

@Repository
public class SectionRepository {
    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Long save(final Long lineId, final Section section) {
        return sectionDao.save(toEntity(lineId, section));
    }

    private SectionEntity toEntity(final Long lineId, final Section section) {
        return new SectionEntity(
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                lineId,
                section.getDistance()
        );
    }

    public void delete(final Long lineId, final Section deletedSection) {
        int affectedRow = sectionDao.delete(toEntity(lineId, deletedSection));

        if (affectedRow == 0) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND_SECTION);
        }
    }

    public void deleteByLineId(final Long lineId) {
        int affectedRow = sectionDao.deleteByLineId(lineId);

        if (affectedRow == 0) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND_SECTION_BY_LINE_ID);
        }
    }
}
