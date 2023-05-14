package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.calculator.AddCalculator;
import subway.domain.calculator.Changes;
import subway.domain.line.Line;
import subway.domain.section.ContainingSections;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.Station;
import subway.dto.StationRequest;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StationService {

    // TODO: StationService 구현
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final SectionRepository sectionRepository;

    public StationService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.sectionRepository = sectionRepository;
    }

    public Long saveSection(StationRequest stationRequest) {
        Line lineFromRequest = new Line(null, stationRequest.getLineName());
        Optional<LineEntity> findNullableLineEntity = lineDao.findByLineName(lineFromRequest.getName());
        Line line = null;
        if (findNullableLineEntity.isEmpty()) {
            LineEntity insertedLineEntity = lineDao.insert(new LineEntity(null, lineFromRequest.getName()));
            line = new Line(insertedLineEntity.getId(), insertedLineEntity.getLineName());
        }
        if (findNullableLineEntity.isPresent()) {
            LineEntity findLineEntity = findNullableLineEntity.get();
            line = new Line(findLineEntity.getId(), findLineEntity.getLineName());
        }
        Station upStation = new Station(null, stationRequest.getUpStationName(), line);
        Station downStation = new Station(null, stationRequest.getDownStationName(), line);
        Distance distance = new Distance(stationRequest.getDistance());
        Section sectionToAdd = new Section(null, upStation, downStation, distance);


        List<Section> containingSections = sectionRepository.findSectionsContaining(sectionToAdd);
        AddCalculator addCalculator = new AddCalculator(new ContainingSections(containingSections));
        Changes changes = addCalculator.addSection(sectionToAdd);
        return applyChanges(changes);
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
        return changes.getChangedLine();
    }

    public void deleteStationById(Long id) {

    }
}