package subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
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

    public List<SectionEntity> findAll() {
        String sql = "select * from SECTION";
        return jdbcTemplate.query(sql, rowMapper);
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

    public void deleteAll() {
        String sql = "delete from SECTION";

        jdbcTemplate.update(sql);
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public boolean existStartStationByStationId(Long stationId) {
        String sql = "select exist(select * from SECTION where station_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, stationId));
    }

    public boolean existEndStationByStationId(final Long stationId) {
        String sql = "select exist(select * from SECTION where station_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, stationId));
    }

}
