package subway.service;

import org.springframework.stereotype.Service;
import subway.persistence.dao.SectionDao;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }
}

