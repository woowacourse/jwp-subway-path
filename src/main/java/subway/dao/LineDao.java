package subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.vo.Charge;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Line> lineMapper;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.lineMapper = (resultSet, rowNum) -> {
            return new Line(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    new Charge(resultSet.getDouble("extra_charge")));
        };
    }

    public Long insert(Line line) {
        String sql = "INSERT INTO line(name, extra_charge) VALUES (?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, line.getName());
            ps.setDouble(2, line.getExtraCharge().getValue());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Line> findById(Long lineId) {
        String sql = "SELECT * FROM line WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineMapper, lineId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Line> findByName(String lineName) {
        String sql = "SELECT * FROM line WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineMapper, lineName));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Line> findAll() {
        String sql = "SELECT * FROM line ORDER BY id";
        return jdbcTemplate.query(sql, lineMapper);
    }
}
