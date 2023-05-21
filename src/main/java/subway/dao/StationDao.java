package subway.dao;

import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {

    public static final int MIN_DISTANCE_VALUE = 1;
    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
        new StationEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getLong("next_station"),
            rs.getInt("distance"),
            rs.getLong("line_id")
        );

    private final RowMapper<StationEntity> rowMapperWithLine = (rs, rowNum) ->
        new StationEntity(
            rs.getLong("STATION.id"),
            rs.getString("STATION.name"),
            rs.getLong("STATION.next_station"),
            rs.getInt("STATION.distance"),
            rs.getLong("STATION.line_id")
        );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(StationEntity entity) {
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("name", entity.getName())
            .addValue("next_station", entity.getNext())
            .addValue("distance", entity.getDistance())
            .addValue("line_id", entity.getLineId());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<StationEntity> findByLineId(Long lineId) {
        String sql = "select * from STATION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public StationEntity findByNextStationId(Long lineId, String nextStationName) {
        try {
            String sql = "select * from STATION S1 "
                + "left outer join STATION S2 on S1.next_station = S2.id "
                + "where S1.line_id = ? and S2.name = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, lineId, nextStationName);
        } catch (EmptyResultDataAccessException exception) {
            throw new NoSuchElementException("노선에 존재하지 않는 역이거나 이전 역이 존재하지 않는 역입니다.");
        }
    }

    public StationEntity findByLineIdAndName(Long lineId, String name) {
        try {
            String sql = "select * from STATION where line_id = ? AND name = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, lineId, name);
        } catch (EmptyResultDataAccessException exception) {
            throw new NoSuchElementException(String.format("노선에 %s이 존재하지 않습니다.", name));
        }
    }

    public StationEntity findHeadStationByLineId(Long lineId) {
        String sql = "select * from STATION "
            + "left outer join LINE on STATION.id = LINE.head_station "
            + "where LINE.id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapperWithLine, lineId);
    }

    public Long findTailStationByLineId(Long lineId) {
        String sql = "select id from STATION where line_id = ? and next_station = 0";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), lineId);
    }

    public boolean isDownEndStation(Long lineId, String name) {
        String sql = "select exists(select * from STATION where line_id = ? and name = ? and next_station = 0) as is_down_end_station";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            rs.getBoolean("IS_DOWN_END_STATION"), lineId, name));
    }

    public boolean isExistInLine(Long lineId, String name) {
        String sql = "select exists(select * from STATION where line_id = ? and name = ?) as is_exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            rs.getBoolean("IS_EXIST"), lineId, name));
    }

    public boolean isNotExist(String name) {
        String sql = "select not exists(select * from STATION where name = ?) as is_not_exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            rs.getBoolean("IS_NOT_EXIST"), name));
    }

    public int updateNextStationById(Long id, Long newNextStation) {
        String sql = "update STATION set next_station = ? where id = ?";
        return jdbcTemplate.update(sql, newNextStation, id);
    }

    public int updateDistanceById(Long id, int newDistance) {
        validateDistance(newDistance);
        String sql = "update STATION set distance = ? where id = ?";
        return jdbcTemplate.update(sql, newDistance, id);
    }

    private void validateDistance(int newDistance) {
        if (newDistance < MIN_DISTANCE_VALUE) {
            throw new IllegalArgumentException("거리는 양의 정수여야 합니다");
        }
    }

    public int deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByLineId(Long lineId) {
        String sql = "delete from STATION where line_id = ?";
        return jdbcTemplate.update(sql, lineId);
    }
}
