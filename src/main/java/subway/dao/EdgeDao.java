package subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.Edge;
import subway.domain.Station;

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

    public List<Edge> findEdgesByLineId(Long lineId) {
        String sql =
                "SELECT e.id,"
                        + "s1.id AS upstation_id, s1.name AS upstation_name, "
                        + "s2.id AS downstation_id, s2.name AS downstation_name, "
                        + "e.distance "
                        + "FROM edge e "
                        + "JOIN station s1 ON e.upstation_id = s1.id "
                        + "JOIN station s2 ON e.downstation_id = s2.id "
                        + "WHERE e.line_id = ?";

        RowMapper<Edge> mapper =
                (resultSet, rowNum) -> {
                    Station upStation = new Station(
                            resultSet.getLong("upstation_id"),
                            resultSet.getString("upstation_name"));
                    Station downStation = new Station(
                            resultSet.getLong("downstation_id"),
                            resultSet.getString("downstation_name"));
                    return new Edge(resultSet.getLong("id"), upStation, downStation, resultSet.getInt("distance"));
                };
        return jdbcTemplate.query(sql, mapper, lineId);
    }
}
