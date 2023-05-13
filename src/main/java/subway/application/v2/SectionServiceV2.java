package subway.application.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.domain.SectionDomain;
import subway.dto.StationResponse;
import subway.repository.SectionRepository;

@Transactional(readOnly = true)
@Service
public class SectionServiceV2 {

    private final SectionRepository sectionRepository;

    public SectionServiceV2(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long saveSection(final CreateSectionRequest request) {
        return sectionRepository.save(request.getUpStationId(), request.getDownStationId(), false, request.getDistance());
    }

    public SectionResponse findBySectionId(final Long sectionId) {
        final SectionDomain section = sectionRepository.findBySectionId(sectionId);

        return SectionResponse.from(
                sectionId,
                section.getDistance().getValue(),
                StationResponse.from(section.getUpStation()),
                StationResponse.from(section.getDownStation())
        );
    }
}
