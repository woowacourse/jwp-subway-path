package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("from_id"),
                    rs.getLong("to_id"),
                    rs.getInt("distance"),
                    rs.getLong("line_id")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("from_id", sectionEntity.getFromId())
                .addValue("to_id", sectionEntity.getToId())
                .addValue("distance", sectionEntity.getDistance())
                .addValue("line_id", sectionEntity.getLineId());

        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(id, sectionEntity.getFromId(), sectionEntity.getToId(), sectionEntity.getDistance(), sectionEntity.getLineId());
    }

    public List<SectionEntity> findSectionsByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public SectionEntity findById(final Long id) {
        try {
            final String sql = "SELECT * FROM section WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("존재하지 않는 구간입니다.");
        }
    }
}
