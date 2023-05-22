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
import subway.domain.vo.Distance;
import subway.domain.line.Section;
import subway.domain.line.Station;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> sectionMapper;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionMapper =
        (resultSet, rowNum) -> {
            Station upStation = new Station(
                    resultSet.getLong("upstation_id"),
                    resultSet.getString("upstation_name"));
            Station downStation = new Station(
                    resultSet.getLong("downstation_id"),
                    resultSet.getString("downstation_name"));
            return new Section(resultSet.getLong("id"), upStation, downStation, new Distance(resultSet.getInt("distance")));
        };
    }

    public Long insert(Long lineId, Section section) {
        String sql = "INSERT INTO section(line_id, upstation_id, downstation_id, distance) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, lineId);
            ps.setLong(2, section.getUpStation().getId());
            ps.setLong(3, section.getDownStation().getId());
            ps.setDouble(4, section.getDistance().getValue());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<Section> findSectionsByLineId(Long lineId) {
        String sql = "SELECT se.id,"
                + "s1.id AS upstation_id, s1.name AS upstation_name, "
                + "s2.id AS downstation_id, s2.name AS downstation_name, "
                + "se.distance "
                + "FROM section se "
                + "JOIN station s1 ON se.upstation_id = s1.id "
                + "JOIN station s2 ON se.downstation_id = s2.id "
                + "WHERE se.line_id = ? "
                + "ORDER BY se.id";
        return jdbcTemplate.query(sql, sectionMapper, lineId);
    }

    public void deleteAllByLineId(Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertAllByLineId(Long lineId, List<Section> sections) {
        String sql = "INSERT INTO section(line_id, upstation_id, downstation_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Section section = sections.get(i);
                ps.setLong(1, lineId);
                ps.setLong(2, section.getUpStation().getId());
                ps.setLong(3, section.getDownStation().getId());
                ps.setDouble(4, section.getDistance().getValue());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }
}
