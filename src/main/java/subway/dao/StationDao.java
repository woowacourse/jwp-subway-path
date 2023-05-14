package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public StationEntity insert(Station station, Long lineId) {

//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//            ps.setString(1, product.getName());
//            ps.setString(2, product.getUrl());
//            ps.setInt(3, product.getPrice());
//            return ps;
//        }, keyHolder);

        Map<String, Object> params = new HashMap<>();
        params.put("name", station.getName());
        params.put("next", station.getNext().getId());
        params.put("distance", station.getDistance().getValue());
        params.put("lind_id", lineId);

        //SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, station.getName(), station.getNext().getId()
            , station.getDistance().getValue(), lineId);
    }

//    public List<StationEntity> findAll() {
//        String sql = "select * from STATION";
//        List<StationEntity> query = jdbcTemplate.query(sql, rowMapper);
//
//        return jdbcTemplate.query(sql, rowMapper);
//    }

    public List<StationEntity> findByLineId(Long lineId) {
        String sql = "select * from STATION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

//    public StationEntity findById(Long id) {
//        String sql = "select * from STATION where id = ?";
//        return jdbcTemplate.queryForObject(sql, rowMapper, id);
//    }

    //전 역을 찾는 메서드
    public StationEntity findByNextStationId(Long lineId, Long nextStationId) {
        try {
            String sql = "select * from STATION where line_id = ? AND next_station = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, lineId, nextStationId);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("노선에 존재하지 않는 역이거나 이전 역이 존재하지 않는 역입니다.");
        }
    }

    public StationEntity findByLineIdAndName(Long lineId, String name) {
        try {
            String sql = "select * from STATION where line_id = ? AND name = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, lineId, name);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException(String.format("노선에 %s이 존재하지 않습니다.", name));
        }
    }

    public StationEntity findHeadStationByLineId(Long lineId) {
        String sql = "select * from STATION "
            + "left outer join LINE on STATION.id = LINE.head_station "
            + "where LINE.id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapperWithLine, lineId);
    }

    public boolean isDownEndStation(Long lineId, String name) {
        String sql = "select exists(select * from STATION where line_id = ? and name = ? and next_station = 0) as is_down_end_station;";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            rs.getBoolean("IS_DOWN_END_STATION"), lineId, name));
    }

//    public void update(Station newStation) {
//        String sql = "update STATION set name = ? where id = ?";
//        jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
//    }

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
