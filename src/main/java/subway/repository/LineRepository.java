package subway.repository;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.DuplicatedNameException;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public LineRepository(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public Long save(Line line) {
        if (lineDao.existsByName(line.getName())) {
            throw new DuplicatedNameException(line.getName());
        }
        LineEntity newLineEntity = lineDao.insert(new LineEntity(line.getName()));

        List<StationEntity> stations = StationEntity.of(line);
        stationDao.insertAll(stations);

        for (Section section : line.getSections()) {
            Station source = section.getSource();
            Station target = section.getTarget();
            StationEntity sourceStationEntity = stationDao.findByName(source.getName())
                    .orElseThrow(() -> new NoSuchElementException());
            StationEntity targetStationEntity = stationDao.findByName(target.getName())
                    .orElseThrow(() -> new NoSuchElementException());

            sectionDao.insert(new SectionEntity(sourceStationEntity.getId(), targetStationEntity.getId(),
                    newLineEntity.getId(), section.getDistance()));
        }

        return newLineEntity.getId();
    }
}
