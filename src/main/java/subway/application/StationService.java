package subway.application;

import org.springframework.stereotype.Service;
import subway.application.repository.StationRepository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void saveStation(Long lineId, SectionRequest sectionRequest) {
        final Line line = toLine(stationRepository.findLineById(lineId));

        final Station upStation = new Station(sectionRequest.getUpStation());
        final Station downStation = new Station(sectionRequest.getDownStation());

        final Section newSection = new Section(upStation, downStation, sectionRequest.getDistance());

        final Sections sections = line.getSections();
        final Sections newSections = sections.addSection(newSection);

        saveStationIfDoesntExists(upStation, sectionRequest.getUpStation());
        saveStationIfDoesntExists(downStation, sectionRequest.getDownStation());

        if (stationRepository.findAllStation().size() > 0) {
            stationRepository.deleteAllSection();
        }

        final List<SectionEntity> sectionEntities = newSections.getSections().stream()
                .map(section -> toSectionEntity(lineId, section)
                )
                .collect(Collectors.toList());
        stationRepository.saveAllSection(sectionEntities);
    }

    private void saveStationIfDoesntExists(final Station station, final String stationName) {
        if (!hasStation(station).isPresent()) {
            stationRepository.saveStation(new StationEntity(stationName));
        }
    }

    private Optional<StationEntity> hasStation(final Station station) {
        return stationRepository.findAllStation().stream()
                .filter(stationEntity -> stationEntity.getName().equals(station.getName()))
                .findFirst();
    }

    private SectionEntity toSectionEntity(final Long lineId, final Section section) {
        return new SectionEntity(
                stationRepository.findStationByName(section.getUpStation().getName()).getId(),
                stationRepository.findStationByName(section.getDownStation().getName()).getId(),
                lineId,
                section.getDistance());
    }

    private Line toLine(final LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), toSections(lineEntity));
    }

    private Sections toSections(final LineEntity lineEntity) {
        return new Sections(findSectionEntityById(lineEntity).stream()
                .map(this::toSection)
                .collect(Collectors.toList()));
    }

    private List<SectionEntity> findSectionEntityById(final LineEntity lineEntity) {
        return stationRepository.findAllSectionByLineId(lineEntity.getId());
    }

    private Section toSection(SectionEntity sectionEntity) {
        StationEntity upStationEntity = stationRepository.findStationById(sectionEntity.getUpStationId());
        StationEntity downStationEntity = stationRepository.findStationById(sectionEntity.getDownStationId());
        Station upStation = new Station(upStationEntity.getName());
        Station downStation = new Station(downStationEntity.getName());
        return new Section(upStation, downStation, sectionEntity.getDistance());
    }

    public StationResponse findStationResponseById(Long id) {
        final StationEntity stationEntity = stationRepository.findStationById(id);
        return new StationResponse(stationEntity.getId(), stationEntity.getName());
    }

    public List<StationResponse> findAllStationResponses() {
        return stationRepository.findAllStation().stream()
                .map(stationEntity -> new StationResponse(stationEntity.getId(), stationEntity.getName()))
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationRepository.updateStation(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        if (stationRepository.findAllStation().size() <= 2) {
            stationRepository.deleteAllStation();
        }
        stationRepository.deleteStationById(id);
    }
}