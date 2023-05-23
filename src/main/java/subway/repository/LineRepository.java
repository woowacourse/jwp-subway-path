package subway.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

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

    public Line findLineByName(final String name) {
        final LineEntity lineEntity = findLineEntityByName(name);

        return new Line(lineEntity.getName(), lineEntity.getColor());
    }

    private LineEntity findLineEntityByName(final String name) {
        return lineDao.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("해당 이름을 가진 노선이 존재하지 않습니다."));
    }

    public Long registerLine(final Line line) {
        final Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        if (lineEntity.isPresent()) {
            throw new DuplicateKeyException("해당 이름의 노선이 이미 존재합니다.");
        }
        return lineDao.insert(line.getName(), line.getColor());
    }

    public Line updateLine(final Line line) {
        final LineEntity lineEntity = findLineEntityByName(line.getName());
        sectionDao.deleteByLineId(lineEntity.getId());
        final List<SectionEntity> sectionEntities = registerSections(line.sections(), lineEntity.getId());
        return toLine(lineEntity, sectionEntities);
    }

    private List<SectionEntity> registerSections(final List<Section> sections, final Long lineId) {
        final List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> toSectionEntity(section, lineId))
                .collect(Collectors.toList());
        sectionDao.insertAll(sectionEntities);
        return sectionEntities;
    }

    private SectionEntity toSectionEntity(final Section section, final Long lineId) {
        final Long sourceId = findStationIdByName(section.getSource().getName());
        final Long targetId = findStationIdByName(section.getTarget().getName());
        return new SectionEntity(lineId, sourceId, targetId, section.getDistance());
    }

    private Long findStationIdByName(final String name) {
        final StationEntity stationEntity = stationDao.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("해당 이름을 가진 역이 존재하지 않습니다."));
        return stationEntity.getId();
    }

    public Line findLineById(final Long id) {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("노선 정보가 잘못되었습니다."));
        return toLine(lineEntity, sectionEntities);
    }

    private Line toLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> {
                    final Long sourceStationId = sectionEntity.getSourceStationId();
                    final Long downstreamId = sectionEntity.getTargetStationId();
                    return new Section(
                            toStation(sourceStationId), toStation(downstreamId), sectionEntity.getDistance());
                })
                .collect(Collectors.toList());
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }

    private Station toStation(final Long stationId) {
        final StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new NoSuchElementException("역 정보가 잘못되었습니다."));
        return new Station(stationEntity.getName());
    }
}
