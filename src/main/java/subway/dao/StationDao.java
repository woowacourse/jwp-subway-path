package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("STATIONS")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<StationEntity> rowMapper = (resultSet, rowNumber) ->
            new StationEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );

    public void save(final List<StationEntity> stations) {
        final String sql = "INSERT INTO stations (name) VALUES (?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                StationEntity station = stations.get(i);
                ps.setString(1, station.getName());
            }

            @Override
            public int getBatchSize() {
                return stations.size();
            }
        });
    }

    public Long save(final String stationName) {
        final MapSqlParameterSource name = new MapSqlParameterSource().addValue("name", stationName);
        return insertAction.executeAndReturnKeyHolder(name).getKey().longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATIONS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from STATIONS where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(final String upStationName) {
        final String sql = "SELECT id, name FROM STATIONS WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, upStationName));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM stations WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Long save(final StationEntity stationEntity) {
        final MapSqlParameterSource name = new MapSqlParameterSource()
                .addValue("name", stationEntity.getName());
        return insertAction.executeAndReturnKeyHolder(name).getKey().longValue();
    }

    public void delete(final List<StationEntity> stations) {
        final String sql = "DELETE FROM stations WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                StationEntity station = stations.get(i);
                ps.setLong(1, station.getId());
            }

            @Override
            public int getBatchSize() {
                return stations.size();
            }
        });
    }
}
