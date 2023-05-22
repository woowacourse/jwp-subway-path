package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionWithStationNameEntity;
import subway.persistence.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PathRepository {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathRepository(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Station findStationById(Long id) {
        Optional<StationEntity> stationEntity = stationDao.findById(id);
        if (stationEntity.isPresent()) {
            return stationEntity.get().toStation();
        }
        throw new IllegalArgumentException("존재하지 않는 역입니다");
    }

    public Sections findAll() {
        List<SectionWithStationNameEntity> sectionEntities = sectionDao.findAll();
        List<Section> sections = new ArrayList<>();
        for (SectionWithStationNameEntity entity : sectionEntities) {
            Line line = findLineById(entity.getLineId());
            sections.add(new Section(line, new Station(entity.getPreStationName()),
                    new Station(entity.getStationName()), new Distance(entity.getDistance())));
        }
        return new Sections(sections);
    }

    private Line findLineById(Long lineId) {
        Optional<LineEntity> lineEntity = lineDao.findById(lineId);
        if (lineEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다");
        }
        return lineEntity.get().toLine();
    }
}
