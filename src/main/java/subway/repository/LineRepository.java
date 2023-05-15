package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@Repository
public class LineRepository {

    StationDao stationDao;
    LineDao lineDao;
    SectionDao sectionDao;

    public LineRepository(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public long saveNewLine(Line line) {
        LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        return lineDao.insert(lineEntity);
    }

    public Optional<Line> findLineById(Long lineId) {
        return lineDao.findById(lineId)
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                        sectionDao.findSectionsByLineId(lineId)));
    }

    public Optional<Line> findLineByName(String lineName) {
        return lineDao.findByName(lineName)
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                        sectionDao.findSectionsByLineId(lineEntity.getId())));
    }

    public Optional<Long> findIdByName(String name) {
        Optional<LineEntity> lineEntity = lineDao.findByName(name);
        return lineEntity.map(LineEntity::getId);
    }

    public Optional<Long> findIdByColor(String color) {
        Optional<LineEntity> lineEntity = lineDao.findByName(color);
        return lineEntity.map(LineEntity::getId);
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                        sectionDao.findSectionsByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    public void updateLineInfo(Line line) {
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor()));
    }

    public void delete(Long lineId) {
        lineDao.deleteById(lineId);
    }


    ///station서비스 관련
    public Long addNewStationToLine(String newStationName, Long lineId) {
        return stationDao.insert(new StationEntity(lineId, newStationName));
    }
    public void addNewSectionToLine(Section section, Long lineId) {
        sectionDao.insert(SectionEntity.of(lineId,section));

    }
    public Section findSectionByUpStation(String baseStationName, Long lineId) {
        StationEntity station = stationDao.findByName(baseStationName, lineId)
                .orElseThrow(()-> new IllegalStateException("해당 역이 존재하지 않습니다"));

        SectionEntity sectionEntity = sectionDao.findByUpStationId(station.getId(), lineId).get();
        StationEntity upStation = stationDao.findById(sectionEntity.getUpStationId()).get();
        StationEntity downStation = stationDao.findById(sectionEntity.getDownStationId()).get();
        return new Section(
                new Station(upStation.getId(), upStation.getName()),
                new Station(downStation.getId(), downStation.getName()),
                new Distance(sectionEntity.getDistance()));
    }

    public Section findSectionByDownStation(String baseStationName, Long lineId) {
        StationEntity station = stationDao.findByName(baseStationName, lineId)
                .orElseThrow(()-> new IllegalStateException("해당 역이 존재하지 않습니다"));

        SectionEntity sectionEntity = sectionDao.findByDownStationId(station.getId(), lineId).get();
        StationEntity upStation = stationDao.findById(sectionEntity.getUpStationId()).get();
        StationEntity downStation = stationDao.findById(sectionEntity.getDownStationId()).get();
        return new Section(
                new Station(upStation.getId(), upStation.getName()),
                new Station(downStation.getId(), downStation.getName()),
                new Distance(sectionEntity.getDistance()));

    }

    public void deleteSectionInLine(Section section, Long lineId)  {
        sectionDao.delete(new SectionEntity(
                lineId,
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance().getValue())
        );
    }

    public List<Station> findAllStations() {
        return stationDao.findAll().stream()
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()))
                .collect(Collectors.toList());
    }

    public Station findStationById(Long stationId, Long lineId) {
        StationEntity station = stationDao.findByStationAndLineId(stationId, lineId).get();
        return new Station(station.getId(), station.getName());
    }

    public void deleteStationBy(Long stationId, Long lineId) {
        stationDao.deleteByStationAndLineId(stationId, lineId);
    }

//    public void updateStationBy(Long stationId, Long lineId) {
//        stationDao.update(new StationEntity());
//    }
}
