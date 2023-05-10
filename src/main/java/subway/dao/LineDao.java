package subway.dao;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line insert(Line line) {
        String sql = "INSERT INTO line(name) VALUES (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, line.getName());
            return ps;
        }, keyHolder);
        long lineId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Line(lineId, line.getName());
    }

    public Optional<Line> findById(Long lineId) {
        String sql = "SELECT * FROM line WHERE id = ?";
        BeanPropertyRowMapper<Line> mapper = BeanPropertyRowMapper.newInstance(Line.class);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, lineId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
