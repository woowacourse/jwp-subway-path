package subway.repository;

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

import java.util.ArrayList;
import java.util.List;

@Repository
public class LineRepository {

    private static final int UPBOUND_STATION_INDEX = 0;
    private static final int DOWNBOUND_STATION_INDEX = 1;

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

    public List<Station> saveInitStations(Section section, Long lineId) {
        List<StationEntity> stationEntities = List.of(
                new StationEntity(section.getLeftStation().getName(), lineId),
                new StationEntity(section.getRightStation().getName(), lineId));
        List<StationEntity> insertedStationEntities = stationDao.insertInit(stationEntities);

        sectionDao.insert(new SectionEntity(insertedStationEntities.get(UPBOUND_STATION_INDEX).getId()
                , insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getId()
                , lineId
                , section.getDistance().getDistance()));

        return List.of(
                new Station(insertedStationEntities.get(UPBOUND_STATION_INDEX).getId(),
                        insertedStationEntities.get(UPBOUND_STATION_INDEX).getName()),
                new Station(insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getId(),
                        insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getName()));
    }
}
