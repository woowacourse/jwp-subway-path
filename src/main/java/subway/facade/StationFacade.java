package subway.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.SectionEntity;
import subway.domain.entity.StationEntity;
import subway.global.util.FinalStationFactory;
import subway.presentation.dto.SectionSaveRequest;
import subway.presentation.dto.StationResponse;
import subway.service.SectionService;
import subway.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class StationFacade {

    private final FinalStationFactory finalStationFactory;
    private final StationService stationService;
    private final SectionService sectionService;

    public StationFacade(final StationService stationService, final SectionService sectionService, final FinalStationFactory finalStationFactory) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.finalStationFactory = finalStationFactory;
    }

    @Transactional
    public Long createStation(final String name) {
        return stationService.insert(StationEntity.of(name));
    }

    public List<StationResponse> getAllByLineId(final Long lineId) {
        List<SectionEntity> sections = sectionService.findAll();
        List<StationEntity> stations = stationService.findAll(lineId, sections);
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateById(final Long stationId, final String name) {
        stationService.updateById(stationId, name);
    }

    @Transactional
    public void deleteById(final Long lineId, final Long stationId) {
        StationEntity stationEntity = stationService.findById(stationId);
        if (finalStationFactory.getFinalStation(lineId).isFinalStation(stationEntity.getName())) {
            stationService.deleteById(stationId);
            return;
        }
        final SectionEntity leftSectionEntity = sectionService.findLeftSectionByStationId(stationId);
        final SectionEntity rightSectionEntity = sectionService.findRightSectionByStationId(stationId);
        int newDistance = leftSectionEntity.getDistance() + rightSectionEntity.getDistance();
        SectionEntity sectionEntity = SectionEntity.of(lineId, leftSectionEntity.getUpStationId(), rightSectionEntity.getDownStationId(), newDistance);

        stationService.deleteById(stationId);
        sectionService.saveSection(SectionSaveRequest.of(sectionEntity));
    }

}
