package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.entity.Section;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public Long saveSection(final Section section) {
        return sectionDao.insert(section);
    }

    public Section findByUpStationId(final Long upStationId) {
        return sectionDao.findByUpStationId(upStationId);
    }

    @Transactional
    public void deleteById(final Long id) {
        sectionDao.deleteById(id);
    }

    public Section findByDownStationId(final Long downStationId) {
        return sectionDao.findByDownStationId(downStationId);
    }
}
