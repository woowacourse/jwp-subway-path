package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.LineName;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.dto.StationSaveResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StationService {

    public static final int SAVED_INITIAL_STATIONS_COUNT = 2;
    public static final int SAVED_INITIAL_SECTION_COUNT = 1;
    public static final int SAVED_NEW_STATION_COUNT = 1;
    public static final int SAVED_MODIFIED_SECTION_COUNT = 2;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationSaveResponse saveStation(StationRequest stationRequest) {
        LineName lineName = new LineName(stationRequest.getLineName());
        Station upStation = new Station(stationRequest.getUpStationName());
        Station downStation = new Station(stationRequest.getDownStationName());
        Section section = new Section(upStation, downStation, stationRequest.getDistance());

        Optional<LineEntity> findLine = lineDao.findByLineName(lineName.getLineName());
        if (findLine.isEmpty()) {
            return saveInitialStations(lineName, section);
        }
        return saveNewStation(findLine.get(), section);
    }

    private StationSaveResponse saveInitialStations(LineName lineName, Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Long insertedLineId = lineDao.insert(new LineEntity(null, lineName.getLineName()));
        Long insertedUpStationId = stationDao.insert(new StationEntity(null, upStation.getName(), insertedLineId));
        Long insertedDownStationId = stationDao.insert(new StationEntity(null, downStation.getName(), insertedLineId));
        Long insertedSectionId = sectionDao.insert(new SectionEntity(null, insertedUpStationId,
                insertedDownStationId, insertedLineId, section.getDistance()));
        return new StationSaveResponse(insertedLineId, List.of(insertedUpStationId, insertedDownStationId), List.of(insertedSectionId));
    }

    private StationSaveResponse saveNewStation(LineEntity lineEntity, Section section) {
        // TODO: 끼워넣었을 때 거리가 음수가 되면 안됨
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        boolean isUpStationEmpty =
                stationDao.findByStationNameAndLineName(upStation.getName(), lineEntity.getName()).isEmpty();
        boolean isDownStationEmpty =
                stationDao.findByStationNameAndLineName(downStation.getName(), lineEntity.getName()).isEmpty();

        validateSavable(isUpStationEmpty, isDownStationEmpty);

        if (isUpStationEmpty) {
            return saveUpStation(lineEntity, section);
        }
        return saveDownStation(lineEntity, section);
    }

    private void validateSavable(boolean isUpStationEmpty, boolean isDownStationEmpty) {
        boolean isAllNotSaved = isUpStationEmpty && isDownStationEmpty;
        boolean isAllAlreadySaved = !isUpStationEmpty && !isDownStationEmpty;

        if (isAllNotSaved || isAllAlreadySaved) {
            throw new IllegalArgumentException("존재하는 노선이면 현재 등록된 역 중에 하나를 포함해야합니다.");
        }
    }

    private StationSaveResponse saveUpStation(LineEntity lineEntity, Section section) {
        Station upStationToSave = section.getUpStation();
        Station currentDownStation = section.getDownStation();
        Long lineId = lineEntity.getId();
        Long savedNewUpStationId = stationDao.insert(new StationEntity(null, upStationToSave.getName(), lineId));
        Optional<StationEntity> findCurrentDownStation = stationDao.findByStationNameAndLineName(currentDownStation.getName(), lineEntity.getName());
        Long currentDownStationId = findCurrentDownStation.get().getId();
        Optional<SectionEntity> findCurrentSection = sectionDao.findByDownStationIdAndLindId(currentDownStationId, lineId);
        sectionDao.deleteBySectionId(findCurrentSection.get().getId());
        Long currentUpStationId = findCurrentSection.get().getUpStationId();
        int currentDistance = findCurrentSection.get().getDistance();
        Long insertedSection1Id = sectionDao.insert(new SectionEntity(null, currentUpStationId, savedNewUpStationId, lineId, currentDistance - section.getDistance()));
        Long insertedSection2Id = sectionDao.insert(new SectionEntity(null, savedNewUpStationId, currentDownStationId, lineId, section.getDistance()));
        return new StationSaveResponse(lineId, List.of(savedNewUpStationId), List.of(insertedSection1Id, insertedSection2Id));
    }

    private StationSaveResponse saveDownStation(LineEntity lineEntity, Section section) {
        Station currentUpStation = section.getUpStation();
        Station downStationToSave = section.getDownStation();
        Long lineId = lineEntity.getId();
        Long savedNewDownStationId = stationDao.insert(new StationEntity(null, downStationToSave.getName(), lineId));
        Optional<StationEntity> findCurrentUpStation = stationDao.findByStationNameAndLineName(currentUpStation.getName(), lineEntity.getName());
        Long currentUpStationId = findCurrentUpStation.get().getId();
        Optional<SectionEntity> findCurrentSection = sectionDao.findByUpStationIdAndLindId(currentUpStationId, lineId);
        sectionDao.deleteBySectionId(findCurrentSection.get().getId());
        Long currentDownStationId = findCurrentSection.get().getDownStationId();
        int currentDistance = findCurrentSection.get().getDistance();
        Long insertedSection1Id = sectionDao.insert(new SectionEntity(null, currentUpStationId, savedNewDownStationId, lineId, section.getDistance()));
        Long insertedSection2Id = sectionDao.insert(new SectionEntity(null, savedNewDownStationId, currentDownStationId, lineId, currentDistance - section.getDistance()));
        return new StationSaveResponse(lineId, List.of(savedNewDownStationId), List.of(insertedSection1Id, insertedSection2Id));
    }

    public StationResponse findStationResponseById(Long id) {
//        return StationResponse.of(stationDao.findById(id));
        return null;
    }

    public List<StationResponse> findAllStationResponses() {
//        List<Station> stations = stationDao.findAll();
//
//        return stations.stream()
//                .map(StationResponse::of)
//                .collect(Collectors.toList());
        return null;
    }

    public void updateStation(Long id, StationRequest stationRequest) {
//        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
//        stationDao.deleteById(id);
    }
}