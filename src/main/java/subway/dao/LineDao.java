package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.domain.Line;
import subway.domain.LineColor;
import subway.domain.LineName;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("lines")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<LineEntity> rowMapper = (resultSet, rowNumber) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color")
    );

    public Long save(final LineEntity lineEntity) {
        return insertAction.executeAndReturnKey(
                    new MapSqlParameterSource("name", lineEntity.getName())
                        .addValue("color", lineEntity.getColor()))
                .longValue();
    }

    public void saveAll(final List<LineEntity> lines) {
        final String sql = "INSERT INTO lines (name, color) VALUES (?, ?) ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                LineEntity line = lines.get(i);
                ps.setString(1, line.getName());
                ps.setString(1, line.getColor());
            }

            @Override
            public int getBatchSize() {
                return lines.size();
            }
        });
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from lines";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "select id, name, color from lines WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String lineName) {
        final String sql = "SELECT id, name, color FROM lines WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, lineName));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        final String sql = "delete from lines where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAll(final List<LineEntity> lines) {
        final String sql = "delete from lines where id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                LineEntity line = lines.get(i);
                ps.setLong(1, line.getId());
            }

            @Override
            public int getBatchSize() {
                return lines.size();
            }
        });
    }
}
