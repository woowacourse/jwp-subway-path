package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.dao.dto.SectionStationResultMap;
import subway.domain.subway.Section;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<SectionStationResultMap> rowMapper = (rs, num) -> new SectionStationResultMap(
            rs.getLong("sectionId"),
            rs.getInt("distance"),
            rs.getLong("upStationId"),
            rs.getString("upStationName"),
            rs.getLong("downStationId"),
            rs.getString("downStationName"),
            rs.getLong("lineId")
    );

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

    public List<SectionStationResultMap> findAll() {
        final String sql = "SELECT se.id sectionId, " +
                "se.distance distance, " +
                "se.up_station_id upStationId, " +
                "st1.name upStationName, " +
                "se.down_station_id downStationId, " +
                "st2.name downStationName, " +
                "se.line_id lineId " +
                "FROM section se " +
                "JOIN station st1 ON st1.id = se.up_station_id " +
                "JOIN station st2 ON st2.id = se.down_station_id ";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionStationResultMap> findAllByLineId(Long lineId) {
        final String sql = "SELECT se.id sectionId, " +
                "se.distance distance, " +
                "se.up_station_id upStationId, " +
                "st1.name upStationName, " +
                "se.down_station_id downStationId, " +
                "st2.name downStationName, " +
                "se.line_id lineId " +
                "FROM section se " +
                "JOIN station st1 ON st1.id = se.up_station_id " +
                "JOIN station st2 ON st2.id = se.down_station_id " +
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
