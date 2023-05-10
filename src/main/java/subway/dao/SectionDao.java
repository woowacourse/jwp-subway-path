package subway.dao;


import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public void delete(final List<Section> sections) {
        final String sql = "delete from SECTION where id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final Section section = sections.get(i);
                ps.setLong(1, section.getId());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

    public void insert(final Long lineId, final List<Section> sections) {
        final MapSqlParameterSource[] sources = sections.stream()
                .map(section -> new MapSqlParameterSource()
                        .addValue("BEFORE_STATION", section.getBeforeStation().getId())
                        .addValue("NEXT_STATION", section.getNextStation().getId())
                        .addValue("DISTANCE", section.getDistance().getValue())
                        .addValue("LINE_ID", lineId)
                )
                .toArray(MapSqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(sources);
    }
}
