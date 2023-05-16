package subway.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(final Long lineId, final Section section) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("line_id", lineId)
                .addValue("upstation_id", section.getUpStation().getId())
                .addValue("downstation_id", section.getDownStation().getId())
                .addValue("distance", section.getDistanceValue());
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Section(id, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public void insertAllByLineId(final Long lineId, final List<Section> sections) {
        final String sql = "INSERT INTO section(line_id, upstation_id, downstation_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final Section section = sections.get(i);
                ps.setLong(1, lineId);
                ps.setLong(2, section.getUpStation().getId());
                ps.setLong(3, section.getDownStation().getId());
                ps.setInt(4, section.getDistanceValue());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

    public List<Section> findAllByLineId(final Long lineId) {
        final String sql =
                "SELECT s.id, "
                        + "s1.id AS upstation_id, s1.name AS upstation_name, "
                        + "s2.id AS downstation_id, s2.name AS downstation_name, "
                        + "s.distance "
                        + "FROM section s "
                        + "JOIN station s1 ON s.upstation_id = s1.id "
                        + "JOIN station s2 ON s.downstation_id = s2.id "
                        + "WHERE s.line_id = ?";

        final RowMapper<Section> mapper =
                (resultSet, rowNum) -> {
                    final Station upStation = new Station(
                            resultSet.getLong("upstation_id"),
                            resultSet.getString("upstation_name"));
                    final Station downStation = new Station(
                            resultSet.getLong("downstation_id"),
                            resultSet.getString("downstation_name"));
                    final Distance distance = new Distance(resultSet.getInt("distance"));

                    return new Section(resultSet.getLong("id"), upStation, downStation, distance);
                };

        return jdbcTemplate.query(sql, mapper, lineId);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";

        jdbcTemplate.update(sql, lineId);
    }
}
