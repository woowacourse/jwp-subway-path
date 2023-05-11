package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getString("current_station_name"),
                    rs.getString("next_station_name"),
                    rs.getInt("distance"),
                    rs.getLong("line_id")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public void batchSave(final List<SectionEntity> sectionEntities) {
        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sectionEntities));
    }

    public List<SectionEntity> findSectionsByLineId(final Long lineId) {
        final String sql = "SELECT * FROM SECTION S WHERE S.line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteAll(final Long lineId) {
        final String sql = "DELETE FROM SECTION S WHERE S.line_id = ?";

        jdbcTemplate.update(sql, lineId);
    }
}
