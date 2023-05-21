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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
                .collect(Collectors.toList());
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

    public void saveSection(Section section) {
        SectionEntity sectionEntity = lineMapper.toSectionEntity(section);
        sectiondao.insert(sectionEntity);
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
}
