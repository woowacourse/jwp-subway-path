package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.converter.LinePropertyConverter;
import subway.repository.converter.SectionConverter;
import subway.repository.converter.StationConverter;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line saveLine(Line line) {
        LineProperty lineProperty = saveLineProperty(line.getLineProperty());
        Sections sections = new Sections(saveSections(lineProperty, line.getSections()));
        return new Line(lineProperty, sections);
    }

    public LineProperty saveLineProperty(LineProperty lineProperty) {
        LineEntity lineEntity = LinePropertyConverter.domainToEntity(lineProperty);
        Long id = lineDao.insert(lineEntity);
        return LinePropertyConverter.entityToDomain(id, lineEntity);
    }

    private List<Section> saveSections(LineProperty lineProperty, List<Section> sections) {
        return sections.stream()
                .map(section -> saveSection(lineProperty.getId(), section))
                .collect(Collectors.toList());
    }

    private Section saveSection(Long insert, Section section) {
        SectionEntity sectionEntity = SectionConverter.domainToEntity(insert, section);
        Long id = sectionDao.insert(sectionEntity);

        return new Section(
                id,
                section.getPreviousStation(),
                section.getNextStation(),
                Distance.from(section.getDistance())
        );
    }

    public boolean existsByName(String name) {
        return !lineDao.findByName(name)
                .isEmpty();
    }

    public Line findById(Long id) {
        List<LineEntity> lines = lineDao.findById(id);

        if (lines.isEmpty()) {
            throw new LineNotFoundException("해당 노선은 존재하지 않습니다.");
        }

        return toLine(lines.get(0));
    }

    private Line toLine(LineEntity lineEntity) {
        LineProperty lineProperty = LinePropertyConverter.entityToDomain(lineEntity);
        Sections sectionsByLineId = new Sections(getSectionsByLineId(lineProperty.getId()));
        return new Line(lineProperty, sectionsByLineId);
    }

    private List<Section> getSectionsByLineId(Long id) {
        return sectionDao.findByLineId(id)
                .stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        getStationById(sectionEntity.getPreviousStationId()),
                        getStationById(sectionEntity.getNextStationId()),
                        Distance.from(sectionEntity.getDistance())
                ))
                .collect(Collectors.toList());
    }

    private Station getStationById(Long id) {
        List<StationEntity> stationEntities = stationDao.findById(id);

        if (stationEntities.isEmpty()) {
            throw new StationNotFoundException("해당하는 역이 존재하지 않습니다.");
        }

        StationEntity stationEntity = stationEntities.get(0);
        return StationConverter.entityToDomain(stationEntity);
    }

    public void updateLineProperty(LineProperty lineProperty) {
        LineEntity lineEntity = LinePropertyConverter.domainToEntity(lineProperty);
        int updateCount = lineDao.update(lineEntity);

        if (updateCount == 0) {
            throw new IllegalArgumentException("노선이 업데이트 되지 않았습니다.");
        }
    }

    public void deleteById(Long id) {
        int removeCount = lineDao.deleteById(id);

        if (removeCount == 0) {
            throw new IllegalArgumentException("노선이 삭제되지 않았습니다.");
        }
    }

}
