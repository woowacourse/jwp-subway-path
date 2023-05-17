package subway.repository;

import static java.util.stream.Collectors.toList;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionEntity;
import subway.domain.Sections;
import java.util.List;

@Repository
public class SectionRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public SectionRepository(final StationDao stationDao, final LineDao lineDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Section save(final Section section) {
        SectionEntity sectionEntity = toEntity(section);

        SectionEntity savedEntity = sectionDao.insert(sectionEntity);

        return toDomain(savedEntity);
    }

    public void saveSections(final List<Section> sections) {
        List<SectionEntity> entities = sections.stream()
                .map(this::toEntity)
                .collect(toList());

        for (final SectionEntity sectionEntity : entities) {
            sectionDao.insert(sectionEntity);
        }
    }

    public Sections findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        return new Sections(sectionEntities.stream()
                .map(this::toDomain)
                .collect(toList()));
    }

    public Sections findByLine(final Line line) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(line.getId());

        return new Sections(sectionEntities.stream()
                .map(this::toDomain)
                .collect(toList()));
    }

    public void delete(final Section section) {
        sectionDao.deleteById(section.getId());
    }

    private Section toDomain(final SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                lineDao.findById(sectionEntity.getLineId()),
                stationDao.findById(sectionEntity.getUpStationId()),
                stationDao.findById(sectionEntity.getDownStationId()),
                new Distance(sectionEntity.getDistance())
        );
    }

    private SectionEntity toEntity(final Section section) {
        return new SectionEntity(
                section.getLine().getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance().getValue()
        );
    }
}
