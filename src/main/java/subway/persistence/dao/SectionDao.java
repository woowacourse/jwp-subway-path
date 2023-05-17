package subway.persistence.dao;


import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class SectionDao {

    private static final RowMapper<SectionEntity> SECTION_ENTITY_ROW_MAPPER = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("before_station"),
            rs.getLong("next_station"),
            rs.getInt("distance"),
            rs.getLong("line_id")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public void delete(final List<Long> ids) {
        final String sql = "delete from SECTION where id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    public void insert(final Long lineId, final List<SectionEntity> sections) {
        final MapSqlParameterSource[] sources = sections.stream()
                .map(section -> new MapSqlParameterSource()
                        .addValue("BEFORE_STATION", section.getBeforeStation())
                        .addValue("NEXT_STATION", section.getNextStation())
                        .addValue("DISTANCE", section.getDistance())
                        .addValue("LINE_ID", lineId)
                )
                .toArray(MapSqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(sources);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "select id, before_station, next_station, distance, line_id " +
                "from section where line_id = ?";
        return jdbcTemplate.query(sql, SECTION_ENTITY_ROW_MAPPER, lineId);
    }
}
