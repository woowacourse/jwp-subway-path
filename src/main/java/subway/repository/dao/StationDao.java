package subway.repository.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.repository.entity.StationEntity;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private static final RowMapper<StationEntity> rowMapper = (rs, rowNum) -> new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
    );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, station.getName());
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findByName(String name) {
        String sql = "select * from STATION where name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void insertAll(List<StationEntity> stations) {
        String sql = "INSERT INTO station(name) VALUES  (?)";
        jdbcTemplate.batchUpdate(sql, stations, stations.size(), ((ps, station) -> ps.setString(1, station.getName())));
    }

    public void deleteByIds(List<Long> ids) {
        final String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));

        String sql = String.format("delete from STATION where id IN (%s)", inSql);
        jdbcTemplate.update(sql, ids.toArray());
    }

    public List<StationEntity> findAllBySections() {
        String sql = "SELECT DISTINCT sta.id, sta.name FROM station sta "
                + "JOIN section sec ON sta.id = sec.source_station_id OR sta.id = sec.target_station_id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<StationEntity> findByLineId(Long id) {
        String sql = "SELECT DISTINCT sta.id, sta.name FROM station sta "
                + "JOIN section sec ON sta.id = sec.source_station_id OR sta.id = sec.target_station_id"
                + " WHERE sec.line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
