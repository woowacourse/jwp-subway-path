package subway.application.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.section.dto.SectionCreateResponse;
import subway.dao.SectionDao;
import subway.domain.Section;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public SectionCreateResponse insert(Section section) {
        Section savedSection = sectionDao.insert(section);
        return SectionCreateResponse.of(savedSection);
    }
}
