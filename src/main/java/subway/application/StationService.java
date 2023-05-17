package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;
import subway.persistence.repository.StationRepository;

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
        //저장하려는 노선에 해당된 거리 정보가 포함된 Line 객체를 생성한다
        final Line line = toLine(stationRepository.findLineById(lineId));
        //상행역과 하행역을 도메인으로 변환
        final Station upStation = new Station(sectionRequest.getUpStation());
        final Station downStation = new Station(sectionRequest.getDownStation());
        //경로를 도메인으로 변환
        final Section newSection = new Section(upStation, downStation, sectionRequest.getDistance());
        //전체 도메인 경로에 저장
        final Sections sections = line.getSections();
        final Sections newSections = sections.addSection(newSection);
        //문제가 없으면 역을 db에 저장
        //기존에 존재하지 않는 역만 저장
        saveStationIfDoesntExists(upStation, sectionRequest.getUpStation());
        saveStationIfDoesntExists(downStation, sectionRequest.getDownStation());
        //기존 db 경로 모두 삭제
        if (stationRepository.findAllStation().size() > 0) {
            stationRepository.deleteAllSection();
        }
        //경로 생성해 db 저장
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
                .map(sectionEntity -> toSection(sectionEntity))
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