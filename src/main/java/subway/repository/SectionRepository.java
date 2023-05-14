package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.line.LineDao;
import subway.dao.section.SectionDao;
import subway.dao.station.StationDao;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public Sections findSectionsByLineNumber(final Long lineNumber) {
        LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
        return getSections(lineEntity);
    }

    private Sections getSections(final LineEntity lineEntity) {
        List<SectionEntity> sectionEntitiesByLineId = sectionDao.findSectionsByLineId(lineEntity.getLineId());

        return sectionEntitiesByLineId.stream()
                .map(this::getSection)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Sections::new));
    }

    private Section getSection(final SectionEntity sectionEntity) {
        StationEntity upStationEntity = stationDao.findById(sectionEntity.getUpStationId())
                .orElseThrow(StationNotFoundException::new);
        StationEntity downStationEntity = stationDao.findById(sectionEntity.getDownStationId())
                .orElseThrow(StationNotFoundException::new);

        Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());
        Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());
        return new Section(upStation, downStation, sectionEntity.getDistance());
    }

    public Sections findSectionsByLineName(final String lineName) {
        LineEntity lineEntity = lineDao.findByName(lineName);
        return getSections(lineEntity);
    }
}
