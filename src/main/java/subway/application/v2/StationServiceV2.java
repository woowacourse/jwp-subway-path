package subway.application.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateSectionRequest;
import subway.application.request.CreateStationRequest;
import subway.application.response.SectionResponse;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.*;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class StationServiceV2 {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public StationServiceV2(final StationRepository stationRepository, final SectionRepository sectionRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long saveStation(final CreateStationRequest request) {
        final StationEntity stationEntity = new StationEntity(request.getStationName());

        return stationRepository.saveStation(stationEntity);
    }

    @Transactional
    public Long saveSection(final CreateSectionRequest request) {
        final LineDomain line = lineRepository.findByLineId(request.getLineId());
        final StationDomain upStation = saveStationWhenNameIsNotExist(request.getUpStationName());
        final StationDomain downStation = saveStationWhenNameIsNotExist(request.getDownStationName());

        final SectionDomain section = new SectionDomain(
                new Distance(request.getDistance()), false, upStation, downStation);
        line.addSection(section);

        final List<SectionEntity> sectionEntities = collectSectionEntitiesBy(line);
        sectionRepository.deleteByLineId(line.getId());
        sectionRepository.saveAll(sectionEntities);

        return line.getId();
    }

    private List<SectionEntity> collectSectionEntitiesBy(final LineDomain line) {
        return line.getSections()
                .stream()
                .map(sectionDomain -> SectionEntity.toEntity(sectionDomain, line.getId()))
                .collect(Collectors.toList());
    }

    private StationDomain saveStationWhenNameIsNotExist(final String stationName) {
        final Optional<StationDomain> maybeStation = stationRepository.findByStationName(stationName);
        if (maybeStation.isPresent()) {
            return maybeStation.get();
        }

        final Long saveStationId = stationRepository.saveStation(new StationEntity(stationName));
        return new StationDomain(saveStationId, stationName);
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

    public StationResponse findByStationId(final Long stationId) {
        final StationDomain station = stationRepository.findByStationId(stationId);

        return StationResponse.from(station);
    }
}
