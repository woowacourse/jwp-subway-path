package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.entity.vo.SectionVo;

@Component
public class PathRepositoryImpl implements PathRepository {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathRepositoryImpl(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Sections findAllSections() {
        List<SectionVo> sectionVos = sectionDao.findAll();
        return new Sections(
                sectionVos.stream()
                .map(this::sectionVoToSection)
                .collect(Collectors.toList())
        );
    }

    private Section sectionVoToSection(SectionVo sectionVo) {
        StationEntity upStationEntity = sectionVo.getUpStationEntity();
        StationEntity downStationEntity = sectionVo.getDownStationEntity();
        return Section.of(
                Station.of(upStationEntity.getId(), upStationEntity.getName()),
                Station.of(downStationEntity.getId(), downStationEntity.getName()),
                sectionVo.getDistance());
    }

    public Station findStationById(Long id) {
        validateStationId(id);
        StationEntity stationEntity = stationDao.findById(id);
        return Station.of(stationEntity.getId(), stationEntity.getName());
    }

    private void validateStationId(final Long id) {
        if (!stationDao.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 STATION ID 입니다.");
        }
    }
}
