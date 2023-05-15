package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.SectionEntity;
import subway.domain.entity.StationEntity;
import subway.dto.SectionSaveRequest;
import subway.dto.StationResponse;
import subway.facade.SectionFacade;
import subway.facade.StationFacade;
import subway.util.FinalStationFactory;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final FinalStationFactory finalStationFactory;
    private final StationFacade stationFacade;
    private final SectionFacade sectionFacade;

    public StationService(final StationFacade stationFacade, final SectionFacade sectionService, final FinalStationFactory finalStationFactory) {
        this.stationFacade = stationFacade;
        this.sectionFacade = sectionService;
        this.finalStationFactory = finalStationFactory;
    }

    @Transactional
    public Long createStation(final String name) {
        return stationFacade.insert(StationEntity.of(name));
    }

    public List<StationResponse> getAllStationResponses(final Long lineId) {
        List<StationEntity> stations = stationFacade.findAll(lineId, sectionFacade.findAll());
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final Long lineId, final Long stationId) {
        StationEntity stationEntity = stationFacade.findById(stationId);
        if (finalStationFactory.getFinalStation(lineId).isFinalStation(stationEntity.getName())) {
            stationFacade.deleteById(stationId);
            return;
        }
        final SectionEntity leftSectionEntity = sectionFacade.findLeftSectionByStationId(stationId);
        final SectionEntity rightSectionEntity = sectionFacade.findRightSectionByStationId(stationId);
        int newDistance = leftSectionEntity.getDistance() + rightSectionEntity.getDistance();
        SectionEntity sectionEntity = SectionEntity.of(lineId, leftSectionEntity.getUpStationId(), rightSectionEntity.getDownStationId(), newDistance);

        stationFacade.deleteById(stationId);
        sectionFacade.saveSection(SectionSaveRequest.of(sectionEntity));
    }

}
