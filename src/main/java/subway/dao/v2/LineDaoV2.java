package subway.dao.v2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;

import java.util.List;
import java.util.Optional;

import static subway.dao.support.SqlHelper.sqlHelper;

@Repository
public class LineDaoV2 {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDaoV2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("lines")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public Long insert(final String lineName, final String lineColor) {
        final SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("name", lineName)
                .addValue("color", lineColor);

        return insertAction.executeAndReturnKey(paramSource).longValue();
    }

    public Optional<LineEntity> findByLineId(Long lineId) {
        final String sql = sqlHelper()
                .select().columns("id, name, color")
                .from().table("LINES")
                .where().condition("id = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, lineId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        final String sql = sqlHelper()
                .select().columns("id, name, color")
                .from().table("LINES")
                .toString();

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteByLineId(Long lineId) {
        final String sql = sqlHelper()
                .delete()
                .from().table("LINES")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, lineId);
    }
}
