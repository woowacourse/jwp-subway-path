package subway.repository;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.exception.LineNotFoundException;

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
        LineEntity lineEntity = lineDao.insert(new LineEntity(null, line.getName(), line.getExtraFare()));
        return new Line(lineEntity.getId(), lineEntity.getName(), new Fare(lineEntity.getExtraFare()),
                new Sections(new LinkedList<>()));
    }

    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new LineNotFoundException("일치하는 노선이 존재하지 않습니다."));

        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
        LinkedList<Section> sections = convertToSections(sectionEntities);

        return new Line(lineEntity.getId(), lineEntity.getName(), new Fare(lineEntity.getExtraFare()),
                new Sections(sections));
    }

    public boolean existsByName(String name) {
        return lineDao.findByName(name).isPresent();
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        List<Line> lines = new ArrayList<>();
        for (LineEntity lineEntity : lineEntities) {
            List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
            LinkedList<Section> sections = convertToSections(sectionEntities);
            lines.add(new Line(lineEntity.getId(), lineEntity.getName(), new Fare(lineEntity.getExtraFare()),
                    new Sections(sections)));
        }
        return lines;
    }

    private LinkedList<Section> convertToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(sectionDto -> new Section(
                        sectionDto.getId(),
                        stationRepository.findById(sectionDto.getLeftStationId()),
                        stationRepository.findById(sectionDto.getRightStationId()),
                        new Distance(sectionDto.getDistance())
                ))
                .collect(toCollection(LinkedList::new));
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }

    public SectionEntity saveSection(SectionEntity sectionEntity) {
        return sectionDao.insert(sectionEntity);
    }

    public void deleteSection(Long sectionId) {
        sectionDao.deleteByStationId(sectionId);
    }
}
