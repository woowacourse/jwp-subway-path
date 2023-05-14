package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.calculator.AddCalculator;
import subway.domain.calculator.Changes;
import subway.domain.calculator.RemoveCalculator;
import subway.domain.line.Line;
import subway.domain.line.LineStatus;
import subway.domain.section.*;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.dto.StationRequest;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StationService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public StationService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long saveSection(StationRequest stationRequest) {
        Optional<LineEntity> findNullableLineEntity = lineDao.findByLineName(stationRequest.getLineName());
        LineStatus lineStatus = getLineStatus(findNullableLineEntity);
        Line line = findOrCreateLine(findNullableLineEntity, stationRequest.getLineName());
        Station upStation = new Station(null, stationRequest.getUpStationName(), line);
        Station downStation = new Station(null, stationRequest.getDownStationName(), line);
        Distance distance = new Distance(stationRequest.getDistance());
        Section sectionToAdd = new Section(null, upStation, downStation, distance);

        List<Section> containingSections = sectionRepository.findSectionsContaining(sectionToAdd);
        AddCalculator addCalculator = new AddCalculator(new ContainingSections(containingSections));
        Changes changes = addCalculator.addSection(lineStatus, sectionToAdd);
        return applyChanges(changes);
    }

    private LineStatus getLineStatus(Optional<LineEntity> findNullableLineEntity) {
        LineStatus lineStatus = null;
        if (findNullableLineEntity.isEmpty()) {
            lineStatus = LineStatus.INITIAL;
        }
        if (findNullableLineEntity.isPresent()) {
            lineStatus = LineStatus.EXIST;
        }
        return lineStatus;
    }

    private Line findOrCreateLine(Optional<LineEntity> findNullableLineEntity, String lineName) {
        Line line = null;
        if (findNullableLineEntity.isEmpty()) {
            LineEntity insertedLineEntity = lineDao.insert(new LineEntity(null, lineName));
            line = new Line(insertedLineEntity.getId(), insertedLineEntity.getLineName());
        }
        if (findNullableLineEntity.isPresent()) {
            LineEntity findLineEntity = findNullableLineEntity.get();
            line = new Line(findLineEntity.getId(), findLineEntity.getLineName());
        }
        return line;
    }

    private Long applyChanges(Changes changes) {
        for (Line line : changes.getLineToAdd()) {
            lineDao.insert(new LineEntity(null, line.getName()));
        }
        for (Line line : changes.getLineToRemove()) {
            lineDao.deleteById(line.getId());
        }
        for (Station station : changes.getStationsToAdd()) {
            stationDao.insert(new StationEntity(null, station.getName(), station.getLineId()));
        }
        for (Station station : changes.getStationsToRemove()) {
            stationDao.deleteById(station.getId());
        }
        for (Section section : changes.getSectionsToAdd()) {
            sectionDao.insert(new SectionEntity(null, section.getUpStationId(),
                    section.getDownStationId(), section.getDistance(), section.getLineId()));
        }
        for (Section section : changes.getSectionsToRemove()) {
            sectionDao.deleteBySectionId(section.getId());
        }
        return changes.getChangedLineId();
    }

    public Long deleteStationById(Long id) {
        Optional<Station> findNullableStation = stationRepository.findStationById(id);
        if (findNullableStation.isEmpty()) {
            throw new IllegalArgumentException("역 id에 해당하는 역 정보를 찾을 수 없습니다.");
        }
        Station stationToDelete = findNullableStation.get();
        List<Section> sectionsByLineId = sectionRepository.findSectionsByLineId(stationToDelete.getLineId());
        Sections sections = new Sections(sectionsByLineId);
        RemoveCalculator removeCalculator = new RemoveCalculator(sections);
        Changes changes = removeCalculator.calculate(stationToDelete);
        return applyChanges(changes);
    }
}