package subway.persistance;

import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineRepository;
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

    public LineRepositoryImpl(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public Optional<Line> findById(Long id) {
        try {
            Line line = lineDao.findById(id)
                    .orElseThrow(NoSuchElementException::new);

            List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());

            List<Section> sections = sectionEntities.stream()
                    .sorted(Comparator.comparingInt(SectionEntity::getListOrder))
                    .map(this::entityToSection)
                    .collect(Collectors.toList());

            return Optional.of(new Line(line.getId(), line.getName(), line.getColor(), new Sections(sections)));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private Section entityToSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId()).get();
        Station downStation = stationDao.findById(sectionEntity.getDownStationId()).get();
        return new Section(upStation, downStation, new Distance(sectionEntity.getDistance()));
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(line -> findById(line.getId())
                        .orElseThrow(() -> new RuntimeException("라인을 불러오는 데 실패했습니다.")))
                .collect(Collectors.toList());
    }

    @Override
    public Line save(Line line) {
        delete(line);

        Line insertedLine = lineDao.insert(line);

        List<Section> sections = line.getSections().getSections();

        List<SectionEntity> sectionEntities = IntStream.range(0, sections.size())
                .mapToObj(i -> new SectionEntity(sections.get(i), insertedLine.getId(), i))
                .collect(Collectors.toList());

        sectionEntities.forEach(sectionDao::insertSection);

        return new Line(insertedLine.getId(), line.getName(), line.getColor(), line.getSections());
    }

    @Override
    public void delete(Line line) {
        sectionDao.deleteByLineId(line.getId());
        lineDao.deleteById(line.getId());
    }
}
