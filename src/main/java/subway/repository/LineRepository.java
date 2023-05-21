package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Color;
import subway.domain.line.Line;
import subway.domain.line.Name;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionStationEntity;
import subway.exception.common.NotFoundLineException;
import subway.exception.line.AlreadyExistLineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line save(final Line line) {
        Optional<LineEntity> findedLine = lineDao.findByName(line.getName());
        if (findedLine.isPresent()) {
            throw new AlreadyExistLineException();
        }
        LineEntity insertedLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        return new Line(
                insertedLineEntity.getId(),
                new Name(insertedLineEntity.getName()),
                new Color(insertedLineEntity.getColor()),
                new Sections(List.of())
        );
    }

    public Line findByName(final String name) {
        LineEntity lineEntity = lineDao.findByName(name)
                .orElseThrow(NotFoundLineException::new);

        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());
        if (sectionStationEntities.isEmpty()) {
            return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
        }

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Line findByStationId(final Long id) {
        LineEntity lineEntity = lineDao.findByStationId(id).orElseThrow(NotFoundLineException::new);
        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(NotFoundLineException::new);
        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public List<Line> readAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<SectionStationEntity> sectionStationEntities = sectionDao.findAll();
        List<Line> lines = new ArrayList<>();

        for (LineEntity lineEntity : lineEntities) {
            List<SectionStationEntity> lineSections = sectionStationEntities.stream().
                    filter(sectionStationEntity -> sectionStationEntity.getLineId() == lineEntity.getId())
                    .collect(Collectors.toList());
            Line line = new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), getSections(lineSections));
            lines.add(line);
        }
        return lines;
    }

    private List<Section> getSections(List<SectionStationEntity> sectionStationEntities) {
        List<Section> sections = new ArrayList<>();
        for (SectionStationEntity sectionStationEntity : sectionStationEntities) {
            sections.add(new Section(
                    sectionStationEntity.getId(),
                    new Station(sectionStationEntity.getLeftStationId(), sectionStationEntity.getLeftStationName()),
                    new Station(sectionStationEntity.getRightStationId(), sectionStationEntity.getRightStationName()),
                    sectionStationEntity.getDistance())
            );
        }
        return sections;
    }
}
