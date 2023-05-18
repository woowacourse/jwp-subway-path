package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.repository.converter.LinePropertyConverter;
import subway.repository.converter.SectionConverter;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;

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
        Sections sections = new Sections(saveSections(line.getLineProperty(), line.getSections()));
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
                Distance.from(section.getDistance()));
    }

}
