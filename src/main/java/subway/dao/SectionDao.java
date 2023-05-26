package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance"),
                    rs.getInt("order")
            );

    private final RowMapper<SectionDetailEntity> sectionDetailEntityRowMapper = (rs, rowNum) ->
            new SectionDetailEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getString("line_name"),
                    rs.getString("line_color"),
                    rs.getLong("up_station_id"),
                    rs.getString("up_station_name"),
                    rs.getLong("down_station_id"),
                    rs.getString("down_station_name"),
                    rs.getInt("distance"),
                    rs.getInt("order")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionEntity.getLineId());
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void insertAll(final List<SectionEntity> sectionEntities) {
        final SqlParameterSource[] array = sectionEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[sectionEntities.size()]);

        String sql = "INSERT INTO section (LINE_ID, UP_STATION_ID, DOWN_STATION_ID, DISTANCE, `ORDER`) VALUES(:lineId, :upStationId, :downStationId, :distance, :order)";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.batchUpdate(sql, array);
    }

    public List<SectionEntity> findByLineIdAndStationId(final Long lineId, final Long stationId) {
        String sql = "SELECT * FROM section WHERE line_id = ? AND (up_station_id = ? OR down_station_id = ?)";
        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, stationId, stationId);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public List<SectionDetailEntity> findSectionsWithSort() {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name, line.name as line_name, line.color as line_color FROM section " +
                "LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id " +
                "LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id " +
                "LEFT OUTER JOIN line ON section.line_id = line.id " +
                "ORDER BY `order`";
        return jdbcTemplate.query(sql, sectionDetailEntityRowMapper);
    }

    public List<SectionDetailEntity> findSectionsByLineIdWithSort(final Long lineId) {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name, line.name as line_name, line.color as line_color FROM section " +
                "LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id " +
                "LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id " +
                "LEFT OUTER JOIN line ON section.line_id = line.id " +
                "WHERE line_id = ? ORDER BY `order`";
        return jdbcTemplate.query(sql, sectionDetailEntityRowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, sectionEntityRowMapper);
    }

    public void deleteByLineId(final Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}

