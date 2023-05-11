package subway.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        try {
            Long lineId = insertAction.executeAndReturnKey(params).longValue();
            return new Line(lineId, line.getName(), line.getColor());
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("이미 존재하는 라인입니다.");
        }
    }

    public List<Line> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("해당하는 라인이 존재하지 않습니다.");
        }
    }

    public Line findByName(String name) {
        String sql = "select id, name, color from LINE WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, name);
        } catch (DataAccessException e) {
            throw new NotFoundException("해당하는 라인이 존재하지 않습니다.");
        }

    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
