package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.domain.Section;
import subway.section.domain.entity.SectionEntity;
import subway.section.domain.repository.SectionRepository;
import subway.section.presentation.dto.SectionSaveRequest;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void saveSection(final SectionSaveRequest request) {
        Optional<SectionEntity> sectionOptional = sectionRepository.findOptionalByUpStationId(request.getUpStationId());
        if (sectionOptional.isPresent() && request.getLineId() == sectionOptional.get().getLineId()) {
            SectionEntity sectionEntity = sectionOptional.get();
            Section section = Section.from(sectionEntity);
            Section leftSection = section.getLeftSection(sectionEntity.getLineId(), request.getDownStationId(), request.getDistance());
            Section rightSection = section.getRightSection(sectionEntity.getLineId(), request.getDownStationId(), request.getDistance());
            deleteOriginAndCreateNewSection(section.getLineId(), leftSection, rightSection);
            return;
        }
        sectionRepository.insert(request.toDomain());
    }

    private void deleteOriginAndCreateNewSection(final Long originSectionId, final Section leftSection, final Section rightSection) {
        sectionRepository.deleteById(originSectionId);
        sectionRepository.insert(leftSection);
        sectionRepository.insert(rightSection);
    }

    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Transactional
    public void ifMiddleStationDelete(final Long stationId, final Long lineId) {
        final Section leftSection = sectionRepository.findLeftSectionByStationId(stationId);
        final Section rightSection = sectionRepository.findRightSectionByStationId(stationId);

        int newDistance = leftSection.getDistanceValue() + rightSection.getDistanceValue();
        Section section = Section.of(lineId, leftSection.getUpStation().getId(), rightSection.getDownStation().getId(), newDistance);

        saveSection(SectionSaveRequest.from(section));
    }
}
