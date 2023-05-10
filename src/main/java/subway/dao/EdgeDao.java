package subway.dao;

import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.Edge;

@Repository
public class EdgeDao {

    private final JdbcTemplate jdbcTemplate;

    public EdgeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Edge insert(Long lineId, Edge edge) {
        String sql = "INSERT INTO edge(line_id, upstation_id, downstation_id, distance) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, lineId);
            ps.setLong(2, edge.getUpStation().getId());
            ps.setLong(3, edge.getDownStation().getId());
            ps.setInt(4, edge.getDistance());
            return ps;
        }, keyHolder);
        long edgeId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Edge(edgeId, edge.getUpStation(), edge.getDownStation(), edge.getDistance());
    }
}
