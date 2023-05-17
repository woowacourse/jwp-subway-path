package subway.persistance;

import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistance.entity.SectionEntity;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public Optional<Line> findById(final Long id) {
        try {
            final Line line = lineDao.findById(id)
                    .orElseThrow(NoSuchElementException::new);

            final List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());
            final List<Section> sections = mapToSections(sectionEntities);

            return Optional.of(new Line(line.getId(), line.getName(), line.getColor(), new Sections(sections)));
        } catch (final NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private List<Section> mapToSections(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .sorted(Comparator.comparingInt(SectionEntity::getListOrder))
                .map(this::entityToSection)
                .collect(Collectors.toList());
    }

    private Section entityToSection(final SectionEntity sectionEntity) {
        final Station upStation = stationDao.findById(sectionEntity.getUpStationId()).get();
        final Station downStation = stationDao.findById(sectionEntity.getDownStationId()).get();
        return new Section(upStation, downStation, new Distance(sectionEntity.getDistance()));
    }

    @Override
    public Lines findAll() {
        final List<Line> lines = lineDao.findAll().stream()
                .map(line -> findById(line.getId())
                        .orElseThrow(() -> new RuntimeException("라인을 불러오는 데 실패했습니다.")))
                .collect(Collectors.toList());
        return new Lines(lines);
    }

    @Override
    public Line save(final Line line) {
        final Line insertedLine = updateIfExistOrElseInsert(line);

        sectionDao.deleteByLineId(line.getId());

        final List<Section> sections = line.getSections();
        final List<SectionEntity> sectionEntities = mapToSectionEntities(insertedLine, sections);
        sectionEntities.forEach(sectionDao::insertSection);

        return new Line(insertedLine.getId(), line.getName(), line.getColor(), new Sections(sections));
    }

    private Line updateIfExistOrElseInsert(final Line line) {
        if (line.getId() == null) {
            return lineDao.insert(line);
        }
        lineDao.update(line);
        return line;
    }

    private List<SectionEntity> mapToSectionEntities(final Line insertedLine, final List<Section> sections) {
        return IntStream.range(0, sections.size())
                .mapToObj(i -> new SectionEntity(sections.get(i), insertedLine.getId(), i))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(final Line line) {
        sectionDao.deleteByLineId(line.getId());
        lineDao.deleteById(line.getId());
    }
}
