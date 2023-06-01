package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.domain.subway.Station;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Section> rowMapper = (rs, num) -> {
        Long sectionId = rs.getLong("id");
        int distance = rs.getInt("distance");
        Long lineId = rs.getLong("line_id");

        Long upStationId = rs.getLong("up_station_id");
        String upStationName = rs.getString("up_station_name");
        Station upStation = new Station(upStationId, upStationName);

        Long downStationId = rs.getLong("down_station_id");
        String downStationName = rs.getString("down_station_name");
        Station downStation = new Station(downStationId, downStationName);

        Distance sectionDistance = new Distance(distance);
        return new Section(sectionId, sectionDistance, upStation, downStation, lineId);
    };

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Section section) {
        String sql = "insert into SECTION (distance, up_station_id, down_station_id, line_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, section.getDistance());
            ps.setLong(2, section.getUpStationId());
            ps.setLong(3, section.getDownStationId());
            ps.setLong(4, section.getLineId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Section> findAll() {
        String query = "SELECT se.id AS section_id, se.distance, se.line_id, " +
                "s1.id AS up_station_id, s1.name AS up_station_name, " +
                "s2.id AS down_station_id, s2.name AS down_station_name " +
                "FROM SECTION se " +
                "JOIN STATION s1 ON s1.id = se.up_station_id " +
                "JOIN STATION s2 ON s2.id = se.down_station_id";

        return jdbcTemplate.query(query, rowMapper);

    }

    public List<Section> findAllByLineId(Long lineId) {
        final String sql = "SELECT se.id AS section_id, se.distance, se.line_id, " +
                "s1.id AS up_station_id, s1.name AS up_station_name, " +
                "s2.id AS down_station_id, s2.name AS down_station_name " +
                "FROM SECTION se " +
                "JOIN STATION s1 ON s1.id = se.up_station_id " +
                "JOIN STATION s2 ON s2.id = se.down_station_id " +
                "WHERE se.line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void update(Section updateSection) {
        final String sql = "UPDATE section SET distance = ?, up_station_id = ?, down_station_id = ?";
        jdbcTemplate.update(sql, updateSection.getDistance(), updateSection.getUpStationId(), updateSection.getDownStationId());
    }

    public boolean exists(Long upStationId, Long downStationId, Long lineId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM section WHERE up_station_id = ? AND down_station_id = ? AND line_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, upStationId, downStationId, lineId);
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void saveAll(final List<Section> sections) {
        List<Map<String, ? extends Number>> batchParams = sections.stream()
                .map(section -> Map.of(
                        "distance", section.getDistance(),
                        "up_station_id", section.getUpStationId(),
                        "down_station_id", section.getDownStationId(),
                        "line_id", section.getLineId()))
                .collect(Collectors.toList());

        SqlParameterSource[] batchSqlParams = SqlParameterSourceUtils.createBatch(batchParams.toArray());
        simpleJdbcInsert.executeBatch(batchSqlParams);
    }

    public void deleteByLineId(Long lineId) {
        String sql = "delete from section where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
