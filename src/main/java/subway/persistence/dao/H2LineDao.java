package subway.persistence.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jmx.export.SpringModelMBean;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.persistence.NullChecker;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2LineDao implements LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            Line.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("extra_fare")
            );

    public H2LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        long lineId = insertAction.executeAndReturnKey(params).longValue();
        return Line.of(lineId, line.getName(), line.getColor(), line.getExtraFare());
    }

    @Override
    public List<Line> findAll() {
        String sql = "select id, name, color, extra_fare from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Line> findById(Long id) {
        NullChecker.isNull(id);
        String sql = "select id, name, color, extra_fare from LINE WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Line newLine) {
        NullChecker.isNull(newLine);
        String sql = "update LINE set name = ?, color = ?, extra_fare = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getExtraFare(), newLine.getId()});
    }

    @Override
    public void deleteById(Long id) {
        NullChecker.isNull(id);
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
