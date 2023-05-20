package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.entity.vo.SectionVo;

public class SectionRepositoryImpl implements SectionRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepositoryImpl(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Sections findSectionsByLineId(final long lineId) {
        validateLineId(lineId);
        List<SectionVo> sectionVos = sectionDao.findSectionsByLineId(lineId);
        return new Sections(
                sectionVos.stream()
                        .map(sectionVo -> Section.of(
                                new Station(sectionVo.getUpStationEntity().getId(), sectionVo.getUpStationEntity().getName()),
                                new Station(sectionVo.getDownStationEntity().getId(), sectionVo.getDownStationEntity().getName()),
                                sectionVo.getDistance()))
                        .collect(Collectors.toList()));
    }

    public Station addSection(final Section section, final long lineId) {
        validateLineId(lineId);
        StationEntity upStationEntity = stationDao.insert(section.getUpStation());
        StationEntity downStationEntity = stationDao.insert(section.getDownStation());
        Station upStation = new Station(upStationEntity.getId(), upStationEntity.getName());
        Station downStation = new Station(downStationEntity.getId(), downStationEntity.getName());
        Section newSection = Section.of(upStation,
                downStation,
                section.getDistance());
        sectionDao.insertSection(newSection, lineId);
        return downStation;
    }

    public void addSections(final Sections addedSections, final long lineId) {
        validateLineId(lineId);
        sectionDao.insertSections(addedSections.getSections(), lineId);
    }

    public Station addStation(final Station station) {
        validateExistStation(station);
        StationEntity stationEntity = stationDao.insert(station);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Station findStationByName(final Station station) {
        StationEntity stationEntity = stationDao.findByName(station.getName().getName());
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Station findStationById(final long stationId) {
        validateNotExistStationById(stationId);
        StationEntity stationEntity = stationDao.findById(stationId);

        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    private void validateNotExistStationById(final long stationId) {
        if (!stationDao.existsById(stationId)) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }

    public void deleteSections(final Sections deletedSections, long lineId) {
        validateLineId(lineId);
        sectionDao.deleteSections(deletedSections.getSections(), lineId);
    }

    private void validateExistStation(Station station) {
        if (stationDao.existsByName(station.getName().getName())) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }

    private void validateLineId(final Long lineId) {
        if (!lineDao.existsById(lineId)) {
            throw new IllegalArgumentException("존재하지 않는 호선입니다.");
        }
    }

    public boolean existStationByName(Station station) {
        return stationDao.existsByName(station.getName().getName());
    }
}
