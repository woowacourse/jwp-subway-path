package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.service.dto.LineRegisterRequest;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void registerFirstSection(
            final LineRegisterRequest lineRegisterRequest,
            final Long savedLineId
    ) {

        final SectionEntity sectionEntity =
                new SectionEntity(
                        null,
                        "Y",
                        lineRegisterRequest.getCurrentStation(),
                        lineRegisterRequest.getNextStation(),
                        lineRegisterRequest.getDistance(),
                        savedLineId
                );

        sectionDao.save(sectionEntity);
    }
}
