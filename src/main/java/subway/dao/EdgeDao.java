package subway.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.edge.Edge;
import subway.domain.station.Station;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EdgeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public EdgeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("edge")
                .usingGeneratedKeyColumns("id");
    }

    public Edge insert(final Long lineId, final Edge edge) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("line_id", lineId)
                .addValue("upstation_id", edge.getUpStation().getId())
                .addValue("downstation_id", edge.getDownStation().getId())
                .addValue("distance", edge.getDistance());
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Edge(id, edge.getUpStation(), edge.getDownStation(), edge.getDistance());
    }

    public List<Edge> findAllByLineId(final Long lineId) {
        final String sql =
                "SELECT e.id,"
                        + "s1.id AS upstation_id, s1.name AS upstation_name, "
                        + "s2.id AS downstation_id, s2.name AS downstation_name, "
                        + "e.distance "
                        + "FROM edge e "
                        + "JOIN station s1 ON e.upstation_id = s1.id "
                        + "JOIN station s2 ON e.downstation_id = s2.id "
                        + "WHERE e.line_id = ?";

        final RowMapper<Edge> mapper =
                (resultSet, rowNum) -> {
                    final Station upStation = new Station(
                            resultSet.getLong("upstation_id"),
                            resultSet.getString("upstation_name"));
                    final Station downStation = new Station(
                            resultSet.getLong("downstation_id"),
                            resultSet.getString("downstation_name"));
                    return new Edge(resultSet.getLong("id"), upStation, downStation, resultSet.getInt("distance"));
                };

        return jdbcTemplate.query(sql, mapper, lineId);
    }
    public void deleteAllByLineId(final Long lineId) {
        String sql = "DELETE FROM edge WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertAllByLineId(final Long lineId, final List<Edge> edges) {
        final String sql = "INSERT INTO edge(line_id, upstation_id, downstation_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final Edge edge = edges.get(i);
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