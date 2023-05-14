package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.domain.Section;
import subway.application.response.StationResponse;
import subway.repository.SectionRepository;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long saveSection(final CreateSectionRequest request) {
        return null;
    }

    public SectionResponse findBySectionId(final Long sectionId) {
        final Section section = sectionRepository.findBySectionId(sectionId);

        return SectionResponse.from(
                sectionId,
                section.getDistance().getValue(),
                StationResponse.from(section.getUpStation()),
                StationResponse.from(section.getDownStation())
        );
    }
}
