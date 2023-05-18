package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getObject("up_station_id", Long.class),
                    rs.getObject("down_station_id", Long.class),
                    rs.getInt("distance"),
                    rs.getInt("order")
            );

    private final RowMapper<LineStationEntity> lineStationEntityRowMapper = (rs, rowNum) -> {
        StationEntity upStationEntity = new StationEntity(rs.getLong(3), rs.getString(7));
        StationEntity downStationEntity = new StationEntity(rs.getLong(4), rs.getString(8));
        SectionEntity sectionEntity = new SectionEntity(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4), rs.getInt(5), rs.getInt(6));
        return new LineStationEntity(upStationEntity, downStationEntity, sectionEntity);
    };

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
        return jdbcTemplate.query(sql, rowMapper, lineId, stationId, stationId);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<LineStationEntity> findLineStationById() {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name FROM section" +
                " LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id" +
                " LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id";

        return jdbcTemplate.query(sql, lineStationEntityRowMapper);
    }

    public List<LineStationEntity> findLineStationByLineIdWithSort(final Long lineId) {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name FROM section" +
                " LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id" +
                " LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id" +
                " WHERE line_id = ? ORDER BY `order`";

        return jdbcTemplate.query(sql, lineStationEntityRowMapper, lineId);
    }

    public List<LineStationEntity> findLineStationWithSort() {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name FROM section" +
                " LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id" +
                " LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id" +
                " ORDER BY `order`";

        return jdbcTemplate.query(sql, lineStationEntityRowMapper);
    }

    public Optional<Long> findByStationIds(final Long upStationId, final Long downStationId) {
        String sql = "SELECT id FROM section WHERE up_station_id = ? AND down_station_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, upStationId, downStationId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteByLineId(final Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}

