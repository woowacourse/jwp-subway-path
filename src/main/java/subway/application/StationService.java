package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateSectionRequest;
import subway.application.request.CreateStationRequest;
import subway.application.request.DeleteStationRequest;
import subway.application.response.StationResponse;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.vo.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public StationService(final StationRepository stationRepository, final SectionRepository sectionRepository, final LineRepository lineRepository) {
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
        final Line line = lineRepository.findByLineId(request.getLineId());
        final Station upStation = saveStationWhenNameIsNotExist(request.getUpStationName());
        final Station downStation = saveStationWhenNameIsNotExist(request.getDownStationName());

        final Section section = new Section(Distance.from(request.getDistance()), false, upStation, downStation);
        line.addSection(section);

        deleteAllThenSaveAllSections(line);

        return line.getId();
    }

    private void deleteAllThenSaveAllSections(final Line line) {
        final List<SectionEntity> sectionEntities = collectSectionEntitiesBy(line);
        sectionRepository.deleteByLineId(line.getId());
        sectionRepository.saveAll(sectionEntities);
    }

    private List<SectionEntity> collectSectionEntitiesBy(final Line line) {
        return line.getSections()
                .stream()
                .map(sectionDomain -> SectionEntity.toEntity(sectionDomain, line.getId()))
                .collect(Collectors.toList());
    }

    private Station saveStationWhenNameIsNotExist(final String stationName) {
        final Optional<Station> maybeStation = stationRepository.findByStationName(stationName);
        if (maybeStation.isPresent()) {
            return maybeStation.get();
        }

        final Long saveStationId = stationRepository.saveStation(new StationEntity(stationName));
        return new Station(saveStationId, stationName);
    }

    public StationResponse findByStationId(final Long stationId) {
        final Station station = stationRepository.findByStationId(stationId);

        return StationResponse.from(station);
    }

    @Transactional
    public void deleteByStationNameAndLineName(final DeleteStationRequest request) {
        final Station station = stationRepository.findByStationName(request.getStationName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역명입니다."));
        final Line line = lineRepository.findByLineName(request.getLineName());
        line.removeSection(station);

        deleteAllThenSaveAllSections(line);
    }
}
