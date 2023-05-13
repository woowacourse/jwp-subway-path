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

    public Sections findSectionsByLineNumber(final long lineNumber) {
        LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
        return getSections(lineEntity);
    }

    private Sections getSections(final LineEntity lineEntity) {
        List<SectionEntity> sectionsByLineId = sectionDao.findSectionsByLineId(lineEntity.getLineId());

        List<Section> sections = sectionsByLineId.stream()
                .map(sectionEntity -> {
                    StationEntity upStationEntity = stationDao.findById(sectionEntity.getUpStationId());
                    StationEntity downStationEntity = stationDao.findById(sectionEntity.getDownStationId());
                    Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());
                    Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());

                    return new Section(upStation, downStation, sectionEntity.getDistance());
                })
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    public Sections findSectionsByLineName(final String lineName) {
        LineEntity lineEntity = lineDao.findByName(lineName);
        return getSections(lineEntity);
    }
}
