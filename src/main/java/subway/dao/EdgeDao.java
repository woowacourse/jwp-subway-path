package subway.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
        String sql = "SELECT e.id,"
                + "s1.id AS upstation_id, s1.name AS upstation_name, "
                + "s2.id AS downstation_id, s2.name AS downstation_name, "
                + "e.distance "
                + "FROM edge e "
                + "JOIN station s1 ON e.upstation_id = s1.id "
                + "JOIN station s2 ON e.downstation_id = s2.id "
                + "WHERE e.line_id = ? "
                + "ORDER BY e.id";

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

    public void deleteAllByLineId(Long lineId) {
        String sql = "DELETE FROM edge WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertAllByLineId(Long lineId, List<Edge> edges) {
        String sql = "INSERT INTO edge(line_id, upstation_id, downstation_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Edge edge = edges.get(i);
                ps.setLong(1, lineId);
                ps.setLong(2, edge.getUpStation().getId());
                ps.setLong(3, edge.getDownStation().getId());
                ps.setInt(4, edge.getDistance());
            }

            @Override
            public int getBatchSize() {
                return edges.size();
            }
        });
    }
}
