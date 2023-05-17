package subway.persistence.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("upstation_id"),
                    rs.getLong("downstation_id"),
                    rs.getLong("line_id"),
                    rs.getLong("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(SectionEntity section) {
        Map<String, Object> params = new HashMap<>();
        params.put("distance", section.getDistance());
        params.put("upstation_id", section.getUpStationId());
        params.put("downstation_id", section.getDownStationId());
        params.put("line_id", section.getLineId());

        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(id, section.getUpStationId(), section.getDownStationId(), section.getLineId(), section.getDistance());
    }

    public void insertAll(List<SectionEntity> sections) {
        final String sql = "INSERT INTO section (distance, upstation_id, downstation_id, line_id) VALUES (?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final SectionEntity section = sections.get(i);
                ps.setLong(1, section.getDistance());
                ps.setLong(2, section.getUpStationId());
                ps.setLong(3, section.getDownStationId());
                ps.setLong(4, section.getLineId());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

    public List<SectionEntity> findAllByLineId(Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public SectionEntity findById(Long id) {
        String sql = "SELECT * FROM section WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteAll() {
        String sql = "DELETE FROM section";
        jdbcTemplate.update(sql);
    }
}
