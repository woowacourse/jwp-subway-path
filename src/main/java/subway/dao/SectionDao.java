package subway.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<SectionEntity> rowMapper = (resultSet, rowNumber) -> {
        return new SectionEntity(
                resultSet.getLong("id"),
                resultSet.getLong("up_station_id"),
                resultSet.getLong("down_station_id"),
                resultSet.getLong("line_id"),
                resultSet.getInt("distance")
        );
    };

    public Long save(final SectionEntity section) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", section.getUpStationId())
                .addValue("down_station_id", section.getDownStationId())
                .addValue("line_id", section.getLineId())
                .addValue("distance", section.getDistance())
        ).longValue();
    }

    public void save(final List<SectionEntity> sections) {
        final String sql = "INSERT INTO section (up_station_id, down_station_id, line_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                SectionEntity section = sections.get(i);
                ps.setLong(1, section.getUpStationId());
                ps.setLong(2, section.getDownStationId());
                ps.setLong(3, section.getLineId());
                ps.setInt(4, section.getDistance());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT id, distance, up_station_id, down_station_id, line_id" +
                " FROM section ";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void delete(final List<SectionEntity> sections) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                SectionEntity section = sections.get(i);
                ps.setLong(1, section.getId());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }
}
