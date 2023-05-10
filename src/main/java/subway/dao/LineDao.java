package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.entity.LineEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(LineEntity lineEntity) {
        String sql = "INSERT INTO line (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, lineEntity.getName());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();

    }

    public List<Line> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public Optional<LineEntity> findByLineName(String lineName) {
        String sql = "SELECT id, name FROM line WHERE name = ?";
        List<LineEntity> lineEntities = jdbcTemplate.query(sql,
                lineEntityRowMapper(), lineName
        );
        return lineEntities.stream().findAny();
    }

    private RowMapper<LineEntity> lineEntityRowMapper() {
        return (rs, rowNum) ->
                new LineEntity(
                        rs.getLong("id"),
                        rs.getString("name")
                );
    }
}
