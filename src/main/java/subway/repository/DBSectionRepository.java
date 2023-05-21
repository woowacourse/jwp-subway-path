package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class DBSectionRepository implements SectionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StationRepository stationRepository;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    private final RowMapper<Section> sectionRowMapper =
            (rs, rowNum) -> {
                Long sectionId = rs.getLong("section_id");
                Line line = new Line(rs.getLong("line_id"), rs.getString("line_name"));
                Station upStation = new Station(rs.getLong("up_station_id"), rs.getString("up_station_name"), line);
                Station downStation = new Station(rs.getLong("down_station_id"), rs.getString("down_station_name"), line);
                int distance = rs.getInt("distance");
                return new Section(sectionId, upStation, downStation, distance);
            };

    public DBSectionRepository(JdbcTemplate jdbcTemplate, StationRepository stationRepository, StationDao stationDao, SectionDao sectionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.stationRepository = stationRepository;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Section insert(Section sectionToAdd) {
        Optional<StationEntity> findNullableUpStationEntity = stationDao.findByStationNameAndLineId(sectionToAdd.getUpStationName(), sectionToAdd.getLineId());
        Optional<StationEntity> findNullableDownStationEntity = stationDao.findByStationNameAndLineId(sectionToAdd.getDownStationName(), sectionToAdd.getLineId());
        validateIsExistSection(sectionToAdd);
        if (findNullableUpStationEntity.isPresent()) {
            return insertDownStationAndSection(sectionToAdd, findNullableUpStationEntity.get());
        }
        if (findNullableDownStationEntity.isPresent()) {
            return insertUpStationAndSection(sectionToAdd, findNullableDownStationEntity.get());
        }
        return insertAllStationsAndSection(sectionToAdd);
    }

    private void validateIsExistSection(Section sectionToAdd) {
        if (isExistSection(sectionToAdd)) {
            throw new IllegalArgumentException("이미 포함되어 있는 구간입니다.");
        }
    }

    private boolean isExistSection(Section sectionToAdd) {
        Optional<SectionEntity> findSectionEntity = sectionDao.findByStationIdsAndDistance(sectionToAdd.getUpStationId(), sectionToAdd.getDownStationId(), sectionToAdd.getDistanceValue());
        return findSectionEntity.isPresent();
    }

    private Section insertDownStationAndSection(Section sectionToAdd, StationEntity findUpStationEntity) {
        Station downStationToAdd = sectionToAdd.getDownStation();
        Station insertedDownStation = stationRepository.insert(downStationToAdd);
        Station existUpStation = stationRepository.findStationById(findUpStationEntity.getId());
        sectionToAdd = new Section(null, existUpStation, insertedDownStation, sectionToAdd.getDistance());
        return add(sectionToAdd);
    }

    private Section insertUpStationAndSection(Section sectionToAdd, StationEntity findDownStationEntity) {
        Station upStationToAdd = sectionToAdd.getUpStation();
        Station insertedUpStation = stationRepository.insert(upStationToAdd);
        Station existDownStation = stationRepository.findStationById(findDownStationEntity.getId());
        sectionToAdd = new Section(null, insertedUpStation, existDownStation, sectionToAdd.getDistance());
        return add(sectionToAdd);
    }

    private Section insertAllStationsAndSection(Section sectionToAdd) {
        Station upStationToAdd = sectionToAdd.getUpStation();
        Station downStationToAdd = sectionToAdd.getDownStation();
        Station insertedUpStation = stationRepository.insert(upStationToAdd);
        Station insertedDownStation = stationRepository.insert(downStationToAdd);
        sectionToAdd = new Section(null, insertedUpStation, insertedDownStation, sectionToAdd.getDistance());
        return add(sectionToAdd);
    }

    private Section add(Section sectionToAdd) {
        SectionEntity insertedEntity = sectionDao.insert(new SectionEntity(null, sectionToAdd.getUpStationId(), sectionToAdd.getDownStationId(), sectionToAdd.getDistanceValue(), sectionToAdd.getLineId()));
        return new Section(insertedEntity.getId(), sectionToAdd.getUpStation(), sectionToAdd.getDownStation(), sectionToAdd.getDistance());
    }

    @Override
    public List<Section> findSectionsByLineId(Long lineId) {
        String sql = "SELECT " +
                "ss.id AS section_id, " +
                "us.id AS up_station_id, " +
                "us.name AS up_station_name, " +
                "ds.id AS down_station_id, " +
                "ds.name AS down_station_name, " +
                "ss.distance, " +
                "ss.line_id, " +
                "line.name AS line_name " +
                "FROM " +
                "section ss " +
                "INNER JOIN station us ON us.line_id = ss.line_id AND us.id = ss.up_station_id " +
                "INNER JOIN station ds ON ds.line_id = ss.line_id AND ds.id = ss.down_station_id " +
                "INNER JOIN line ON ss.line_id = line.id " +
                "WHERE line.id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    @Override
    public void remove(Section sectionToModify) {
        sectionDao.deleteBySectionId(sectionToModify.getId());
    }
}
