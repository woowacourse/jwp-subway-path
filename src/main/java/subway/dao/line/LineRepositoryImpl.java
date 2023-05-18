package subway.dao.line;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.section.SectionDao;
import subway.dao.section.SectionEntity;
import subway.dao.station.StationDao;
import subway.dao.station.StationEntity;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;


@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepositoryImpl(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public Line findById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id);
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return new Line(
                lineEntity.getLineId(),
                toSections(sectionEntities),
                new LineName(lineEntity.getName()),
                new LineColor(lineEntity.getColor()));
    }

    @Override
    public List<Line> findAll() {
        final List<LineEntity> allLineEntities = lineDao.findAll();

        return allLineEntities.stream()
                .map(this::toLine) //FIXME: N + 1
                .collect(Collectors.toList());
    }

    private Line toLine(final LineEntity lineEntity) {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getLineId());
        final Sections sections = toSections(sectionEntities);
        return new Line(sections, new LineName(lineEntity.getName()), new LineColor(lineEntity.getColor()));
    }

    private Sections toSections(final List<SectionEntity> sectionEntities) {
        final Map<Long, Station> stationsById = stationDao.findAll().stream()
                .map(StationEntity::toStation)
                .collect(Collectors.toMap(Station::getStationId, station -> station));

        final List<Section> sections = sectionEntities.stream()
                .map(entities -> entities.toSection(stationsById))
                .collect(Collectors.toList());

        return new Sections(sections);
    }
}
