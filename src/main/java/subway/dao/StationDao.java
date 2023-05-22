package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) -> new StationEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getLong("line_id")
    );

    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity station) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", station.getName());
        params.put("line_id", station.getLineId());

        Long stationId = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(stationId, station.getName(), station.getLineId());
    }

    public List<StationEntity> insertInit(List<StationEntity> stationEntities) {
        List<StationEntity> stations = new ArrayList<>();
        for (StationEntity station : stationEntities) {
            Map<String, Object> params = new HashMap<>();

            params.put("name", station.getName());
            params.put("line_id", station.getLineId());

            Long stationId = insertAction.executeAndReturnKey(params).longValue();

            stations.add(new StationEntity(stationId, station.getName(), station.getLineId()));
        }
        return stations;
    }

    public Optional<StationEntity> findById(final Long id) {
        String sql = "SELECT id, name, line_id FROM STATION WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByNameAndLineId(String baseStation, Long lineId) {
        String sql = "SELECT id, name, line_id FROM STATION WHERE name = ? AND line_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, baseStation, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteBothById(List<Long> stationIds) {
        String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, stationIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return stationIds.size();
            }
        });
    }

    public Optional<StationEntity> findByName(final String stationName, final String lineName) {
        String sql = "SELECT s.id, s.name, s.line_id FROM STATION s " +
                "JOIN LINE l on s.line_id = l.id " +
                "WHERE s.name = ? AND l.name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationName, lineName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
