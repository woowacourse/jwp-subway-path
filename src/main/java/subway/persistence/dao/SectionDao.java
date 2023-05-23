package subway.persistence.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.SectionEntity;

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
        jdbcTemplate.batchUpdate(sql, ids, ids.size(), (ps, argument) -> ps.setLong(1, argument));
    }

    public void insert(final List<SectionEntity> sections) {
        final SqlParameterSource[] sources = SqlParameterSourceUtils.createBatch(sections.toArray());
        simpleJdbcInsert.executeBatch(sources);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "select id, before_station, next_station, distance, line_id " +
                "from section where line_id = ?";
        return jdbcTemplate.query(sql, SECTION_ENTITY_ROW_MAPPER, lineId);
    }

    public List<SectionEntity> findAll() {
        final String sql = "select id, before_station, next_station, distance, line_id from section";
        return jdbcTemplate.query(sql, SECTION_ENTITY_ROW_MAPPER);
    }
}
