package subway.domain.line.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.station.domain.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            StationEntity.of(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private RowMapper<List<StationEntity>> listRowMapper = (rs, rowNum) -> {
        final ArrayList<StationEntity> stationEntities = new ArrayList<>();
        stationEntities.add(StationEntity.of(
                rs.getLong("id"),
                rs.getString("name")
        ));
        return stationEntities;
    };

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("stations")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATIONS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from STATIONS where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateById(Long id, StationEntity newStationEntity) {
        String sql = "update STATIONS set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStationEntity.getName(), id});
    }

    public void deleteById(Long id) {
        String sql = "delete from STATIONS where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<StationEntity> findFinalUpStation(Long lineId) {
        String sql = "SELECT st.* "
                + "FROM STATIONS st "
                + "WHERE ( "
                + "    SELECT COUNT(*) "
                + "    FROM SECTIONS sc "
                + "    WHERE sc.line_id = ? "
                + "      AND sc.up_station_id = st.id "
                + "      AND st.id not in (select down_station_id from sections where line_id = ?)"
                + "    LIMIT 1"
                + ") = 1; ";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, lineId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findFinalDownStation(Long lineId) {
        String sql = "SELECT st.* "
                + "FROM STATIONS st "
                + "WHERE ( "
                + "    SELECT COUNT(*) "
                + "    FROM SECTIONS sc "
                + "    WHERE sc.line_id = ? "
                + "      AND sc.down_station_id = st.id "
                + "      AND st.id not in (select up_station_id from sections where line_id = ?)"
                + "    LIMIT 1"
                + ") = 1; ";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, lineId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

}
