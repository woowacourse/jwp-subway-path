package subway.dao;

import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
        new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("extra_charge"),
            rs.getLong("head_station")
        );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(LineEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", entity.getName());
        params.put("color", entity.getColor());
        params.put("extra_charge", entity.getExtraCharge());
        params.put("head_station", entity.getHeadStation());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select * from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(Long id) {
        try {
            String sql = "select * from LINE WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NoSuchElementException("존재하지 않는 노선입니다.");
        }
    }

    public Long findHeadIdById(Long lineId) {
        String sql = "select head_station from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("head_station"), lineId);
    }

    public boolean isExist(String name) {
        String sql = "select exists(select * from LINE where name = ?) as is_exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            rs.getBoolean("is_exist"), name));
    }

    public boolean isUpEndStation(Long lineId, String name) {
        String sql = "select exists(select * from LINE "
            + "left outer join STATION on STATION.id = LINE.head_station "
            + "where LINE.id = ? and STATION.name = ?) as is_up_end_station";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            rs.getBoolean("IS_UP_END_STATION"), lineId, name));
    }

    public int updateHeadStation(Long lineId, Long headStation) {
        String sql = "update LINE set head_station = ? where id = ?";
        return jdbcTemplate.update(sql, headStation, lineId);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
