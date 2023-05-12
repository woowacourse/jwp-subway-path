package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import subway.persistence.dao.entity.PathEntity;

import java.util.List;

@Component
public class PathDao {

    private static final RowMapper mapper = (rs, rowNum) -> new PathEntity(
            rs.getLong("id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getLong("line_id"),
            rs.getInt("distance")
    );

    private final JdbcTemplate jdbcTemplate;

    public PathDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PathEntity> findAllByLineId(final Long lineId) {
        final String sql = "SELECT * FROM paths WHERE line_id = ?";
        return jdbcTemplate.query(sql, mapper, lineId);
    }

    public List<PathEntity> findAll() {
        final String sql = "SELECT * FROM paths";
        return jdbcTemplate.query(sql, mapper);
    }
}
