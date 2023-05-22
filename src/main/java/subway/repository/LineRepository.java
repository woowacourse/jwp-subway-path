package subway.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.InvalidInputException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectiondao;
    private final StationDao stationDao;
    private final LineMapper lineMapper;

    public LineRepository(LineDao lineDao, SectionDao sectiondao, StationDao stationDao, LineMapper lineMapper) {
        this.lineDao = lineDao;
        this.sectiondao = sectiondao;
        this.stationDao = stationDao;
        this.lineMapper = lineMapper;
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<SectionEntity> sectionEntities = sectiondao.findAll();
        List<StationEntity> stationEntities = stationDao.findAll();

        Map<Long, List<SectionEntity>> sectionInLine = sectionEntities.stream()
                .collect(groupingBy(SectionEntity::getLineId));

        return lineEntities.stream().map(lineEntity -> lineMapper.toLine(
                        lineEntity,
                        sectionInLine.get(lineEntity.getId()),
                        stationEntities)
                )
                .collect(toList());
    }

    public Station findStationById(long stationId) {
        try {
            StationEntity stationEntity = stationDao.findById(stationId);
            return new Station(stationEntity.getId(), stationEntity.getName());
        } catch (DataAccessException e) {
            throw new InvalidInputException(stationId + "는 존재하지 않는 역 아이디입니다.");
        }
    }

    public Line findById(long lineId) {
        try {
            LineEntity lineEntity = lineDao.findById(lineId);
            List<SectionEntity> sectionEntities = sectiondao.findSectionsBy(lineId);
            List<StationEntity> stationEntities = stationDao.findAll();

            return lineMapper.toLine(
                    lineEntity,
                    sectionEntities,
                    stationEntities);

        } catch (DataAccessException e) {
            Arrays.stream(e.getStackTrace()).forEach(System.out::println);
            System.out.println(e.getMessage());
            throw new InvalidInputException("존재하지 않는 라인입니다.");
        }
    }

    public SectionEntity saveSection(Section section) {
        SectionEntity sectionEntity = lineMapper.toSectionEntity(section);
        return sectiondao.insert(sectionEntity);
    }

    public void removeSectionsByLineId(long lineId) {
        sectiondao.deleteByLineId(lineId);
    }

    public void saveAllSectionsInLine(Line line) {
        line.getSections()
                .forEach(this::saveSection);
    }

    public List<SectionEntity> getSections(long lineId) {
        return sectiondao.findSectionsBy(lineId);
    }

    public void removeSectionById(long id) {
        sectiondao.deleteById(id);
    }

    public Station saveStation(Station station) {
        StationEntity stationEntity = stationDao.insert(station);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public List<Station> findAllStations() {
        List<StationEntity> allStationEntities = stationDao.findAll();
        return allStationEntities.stream()
                .map(entity -> new Station(entity.getId(), entity.getName()))
                .collect(toList());
    }

    public void updateStation(Station station) {
        stationDao.update(station.getName(), station.getId());
    }

    public void removeStationById(Long id) {
        sectiondao.deleteById(id);
    }

    public LineEntity saveLine(Line line) {
        return lineDao.insert(line);
    }

    public Optional<Long> findByName(String name) {
        return lineDao.findByName(name)
                .map(LineEntity::getId);
    }

    public Optional<Long> findStationByName(String name) {
        return stationDao.findByName(name)
                .map(StationEntity::getId);
    }

    public void removeLineById(Long id) {
        lineDao.deleteById(id);
    }

    public LineEntity findOnlyLineBy(Long id) {
        return lineDao.findById(id);
    }

    public List<LineEntity> findOnlyLines() {
        return lineDao.findAll();
    }

    public void updateLine(Line line) {
        lineDao.update(line);
    }
}
