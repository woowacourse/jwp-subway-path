package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.SectionSaveRequest;
import subway.facade.SectionFacade;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionFacade sectionFacade;

    public SectionService(final SectionFacade sectionFacade) {
        this.sectionFacade = sectionFacade;
    }

    @Transactional
    public void saveSection(final SectionSaveRequest request) {
        sectionFacade.saveSection(request);
    }

}
