package subway.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
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

    public Line save(final Line line) {
        LineEntity lineEntity = lineDao.save(new LineEntity(line.getId(), line.getName()));
        return new Line(lineEntity.getId(), line.getName(), line.getSections().getSections());
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        List<Line> lines = new ArrayList<>();
        for (final LineEntity lineEntity : lineEntities) {
            LinkedList<Section> sections = getSections(lineEntity.getId());
            lines.add(new Line(lineEntity.getId(), lineEntity.getName(), sections));
        }
        return lines;
    }

    private LinkedList<Section> getSections(final Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);

        LinkedList<Section> sections = new LinkedList<>();
        for (final SectionEntity sectionEntity : sectionEntities) {
            Section section = getSection(sectionEntity);
            sections.add(section);
        }
        return sections;
    }

    private Section getSection(final SectionEntity sectionEntity) {
        Station leftStation = findStationById(sectionEntity.getLeftStationId());
        Station rightStation = findStationById(sectionEntity.getRightStationId());
        Distance distance = new Distance(sectionEntity.getDistance());
        return new Section(leftStation, rightStation, distance);
    }

    private Station findStationById(final Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역을 찾을 수 없습니다."));
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Optional<Line> findById(final Long id) {
        return lineDao.findById(id)
                .map(lineEntity -> new Line(
                        lineEntity.getId(),
                        lineEntity.getName(),
                        getSections(lineEntity.getId())
                ));
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
