package subway.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

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

    public Long save(final Line line) {
        final LineEntity lineEntity = LineEntity.from(line);
        return lineDao.insert(lineEntity);
    }

    public void updateSections(final Line line) {
        sectionDao.deleteAllByLineId(line.getId());
        final List<SectionEntity> sectionEntities = line.getSections()
                .getSections()
                .stream()
                .map(section -> SectionEntity.of(section, line.getId()))
                .collect(Collectors.toUnmodifiableList());
        sectionDao.batchInsert(sectionEntities);
    }

    public Optional<Line> findByName(final LineName lineName) {
        return lineDao.findByName(lineName.name()).map(
                it -> new Line(
                        it.getId(),
                        new LineName(it.getName()),
                        new LineColor(it.getColor()),
                        findSectionsByLineId(it.getId())
                )
        );
    }

    private Sections findSectionsByLineId(final Long id) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(id);
        final List<Section> sections = new LinkedList<>();

        for (SectionEntity sectionEntity : sectionEntities) {
            final Station upStation = findStationById(sectionEntity.getUpStationId());
            final Station downStation = findStationById(sectionEntity.getDownStationId());
            final Section section = new Section(
                    sectionEntity.getId(),
                    upStation,
                    downStation,
                    new Distance(sectionEntity.getDistance()));
            sections.add(section);
        }

        return new Sections(sections);
    }

    private Station findStationById(final Long id) {
        final StationEntity stationEntity = stationDao.findById(id).orElseThrow(() ->
                new IllegalStateException(id + "를 가진 역을 찾을 수 없습니다."));

        return new Station(new StationName(stationEntity.getName()));
    }

    public List<Line> findAllLine() {
        return lineDao.findAll().stream().map(it -> new Line(
                it.getId(),
                new LineName(it.getName()),
                new LineColor(it.getColor()),
                findSectionsByLineId(it.getId())
        )).collect(Collectors.toUnmodifiableList());
    }

    public void update(final Line line) {
        lineDao.updateById(LineEntity.from(line));
    }

    public void delete(final Line line) {
        lineDao.deleteById(line.getId());
    }
}

