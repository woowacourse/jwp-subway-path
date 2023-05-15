package subway.repository;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.application.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.dto.LineEntity;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationRepository stationRepository;

    public LineRepository(LineDao lineDao, SectionDao sectionDao, StationRepository stationRepository) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
    }

    public Line save(Line line) {
        LineEntity lineEntity = lineDao.insert(new LineEntity(null, line.getName()));
        return new Line(lineEntity.getId(), lineEntity.getName(), new LinkedList<>());
    }

    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 노선이 존재하지 않습니다."));

        List<SectionDto> sectionDtos = sectionDao.findByLineId(lineEntity.getId());
        LinkedList<Section> sections = convertToSections(sectionDtos);

        return new Line(lineEntity.getId(), lineEntity.getName(), sections);
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        List<Line> lines = new ArrayList<>();
        for (LineEntity lineEntity : lineEntities) {
            List<SectionDto> sectionDtos = sectionDao.findByLineId(lineEntity.getId());
            LinkedList<Section> sections = convertToSections(sectionDtos);
            lines.add(new Line(lineEntity.getId(), lineEntity.getName(), sections));
        }
        return lines;
    }

    private LinkedList<Section> convertToSections(List<SectionDto> sectionDtos) {
        return sectionDtos.stream()
                .map(sectionDto -> new Section(
                        stationRepository.findById(sectionDto.getLeftStationId()),
                        stationRepository.findById(sectionDto.getRightStationId()),
                        new Distance(sectionDto.getDistance())
                ))
                .collect(toCollection(LinkedList::new));
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
