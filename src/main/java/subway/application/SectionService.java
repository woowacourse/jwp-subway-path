package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDAO;
import subway.domain.Section;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@Service
public class SectionService {
    
    private SectionDAO sectionDAO;
    
    public SectionService(final SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
    }
    
    public SectionResponse saveSection(SectionRequest sectionRequest){
        Section section = Section.from(sectionRequest);
        return SectionResponse.of(this.sectionDAO.insert(section));
    }
}
