package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.LineName;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    new LineName(rs.getString("name"))
            );

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(final LineName lineName) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", lineName.getValue());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, lineName);
    }

    public List<Line> findAll() {
        final String sql = "select id, name from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(final Long id) {
        final String sql = "select id, name from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void updateName(final Long id, final LineName lineName) {
        final String sql = "update LINE set name = ? where id = ?";
        jdbcTemplate.update(sql, lineName.getValue(), id);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
