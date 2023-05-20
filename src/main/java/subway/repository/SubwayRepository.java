package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.controller.exception.BusinessException;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

@Repository
public class SubwayRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    @Autowired
    public SubwayRepository(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Line findLineByName(final String name) {
        final LineEntity lineEntity = findLineEntityByName(name);

        return new Line(lineEntity.getName(), lineEntity.getColor());
    }

    private LineEntity findLineEntityByName(final String name) {
        return lineDao.findByName(name)
                .orElseThrow(() -> new BusinessException("해당 이름을 가진 노선이 존재하지 않습니다."));
    }

    public Long registerLine(final Line line) {
        final Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        if (lineEntity.isPresent()) {
            throw new BusinessException("해당 이름의 노선이 이미 존재합니다.");
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
                .orElseThrow(() -> new BusinessException("해당 이름을 가진 역이 존재하지 않습니다."));
        return stationEntity.getId();
    }

    public List<Station> findStations() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> new Station(stationEntity.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public void registerStation(final Station station) {
        final Optional<StationEntity> stationEntity = stationDao.findByName(station.getName());
        if (stationEntity.isEmpty()) {
            stationDao.insert(station.getName());
        }
    }

    public Line findLineById(final Long id) {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new BusinessException("노선 정보가 잘못되었습니다."));
        return toLine(lineEntity, sectionEntities);
    }

    private Line toLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> {
                    final Long sourceStationId = sectionEntity.getSourceStationId();
                    final Long downstreamId = sectionEntity.getTargetStationId();
                    return new Section(toStation(sourceStationId), toStation(downstreamId), sectionEntity.getDistance());
                })
                .collect(Collectors.toList());
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }

    private Station toStation(final Long stationId) {
        final StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new BusinessException("역 정보가 잘못되었습니다."));
        return new Station(stationEntity.getName());
    }

    public Station findStationByName(final String stationName) {
        final StationEntity stationEntity = stationDao.findByName(stationName)
                .orElseThrow(() -> new BusinessException("해당 이름을 가진 역이 존재하지 않습니다."));
        return toStation(stationEntity.getId());
    }

    public Subway findSubway() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<Line> lines = lineEntities.stream()
                .map(lineEntity -> toLine(lineEntity, sectionDao.findByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
        return new Subway(lines);
    }
}
