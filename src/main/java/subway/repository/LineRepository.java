package subway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Color;
import subway.domain.line.Line;
import subway.domain.line.Name;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;
import subway.entity.StationEntity;
import subway.exception.common.NotFoundLineException;
import subway.exception.common.NotFoundStationException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line save(Line line) {
        LineEntity insertedLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        return new Line(
            insertedLineEntity.getId(),
            new Name(insertedLineEntity.getName()),
            new Color(insertedLineEntity.getColor()),
            new Sections(List.of())
        );
    }

    public Line findByName(String name) {
        LineEntity lineEntity = lineDao.findByName(name)
            .orElseThrow(NotFoundLineException::new);

        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());
        if (sectionStationEntities.isEmpty()) {
            return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
        }

        List<Section> sections = new ArrayList<>();
        for (SectionStationEntity sectionStationEntity : sectionStationEntities) {
            sections.add(new Section(
                sectionStationEntity.getId(),
                new Station(sectionStationEntity.getLeftStationId(), sectionStationEntity.getLeftStationName()),
                new Station(sectionStationEntity.getRightStationId(), sectionStationEntity.getRightStationName()),
                    sectionStationEntity.getDistance())
            );
        }
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }
}
