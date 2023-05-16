package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("start_station_id"),
                    rs.getLong("end_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertAll(List<SectionEntity> sectionEntities) {
        String sql = "insert into SECTION (line_id, start_station_id, end_station_id, distance) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, sectionEntities, sectionEntities.size(),
                (PreparedStatement preparedStatement, SectionEntity sectionEntity) -> {
                    preparedStatement.setLong(1, sectionEntity.getLineId());
                    preparedStatement.setLong(2, sectionEntity.getStartStationId());
                    preparedStatement.setLong(3, sectionEntity.getEndStationId());
                    preparedStatement.setInt(4, sectionEntity.getDistance());
                });
    }

    public List<SectionEntity> findAll() {
        String sql = "select * from SECTION";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteAllByLineId(Long lineId) {
        String sql = "delete from SECTION where line_id = ?";

        jdbcTemplate.update(sql, lineId);
    }

    public Boolean isExistSectionUsingStation(Long stationId) {
        String sql = "select exists(select * from SECTION where start_station_id = ? OR end_station_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, stationId, stationId);
    }
}
