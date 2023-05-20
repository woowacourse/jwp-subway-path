package subway.repository;

import java.util.List;
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

    public Subway findSubway() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<Line> lines = lineEntities.stream()
                .map(lineEntity -> toLine(lineEntity, sectionDao.findByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
        return new Subway(lines);
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
}
