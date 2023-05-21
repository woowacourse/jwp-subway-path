package subway.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchSave(List<SectionEntity> sectionEntities) {
        final String sql = "INSERT INTO section "
                + "(`up_station_id`, `down_station_id`, `line_id`, `distance`)"
                + " VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, sectionEntities.get(i).getUpStationId());
                ps.setLong(2, sectionEntities.get(i).getDownStationId());
                ps.setLong(3, sectionEntities.get(i).getLineId());
                ps.setInt(4, sectionEntities.get(i).getDistance());
            }

            @Override
            public int getBatchSize() {
                return sectionEntities.size();
            }
        });
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public int deleteByLineId(Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        return jdbcTemplate.update(sql, lineId);
    }
}
