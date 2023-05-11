package subway.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            Station.of(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private RowMapper<List<Station>> listRowMapper = (rs, rowNum) -> {
        final ArrayList<Station> stations = new ArrayList<>();
        stations.add(Station.of(
                rs.getLong("id"),
                rs.getString("name")
        ));
        return stations;
    };

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("stations")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<Station> findAll() {
        String sql = "select * from STATIONS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(Long id) {
        String sql = "select * from STATIONS where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void updateById(Long id, Station newStation) {
        String sql = "update STATIONS set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStation.getName(), id});
    }

    public void deleteById(Long id) {
        String sql = "delete from STATIONS where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Station> findByName(final String name) {
        String sql = "select * from STATIONS where name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Station getUpTerminalStation(Long lineId) {
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
        return jdbcTemplate.queryForObject(sql, rowMapper, lineId, lineId);
    }

    public Station getDownTerminalStation(Long lineId) {
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
        return jdbcTemplate.queryForObject(sql, rowMapper, lineId, lineId);
    }

}
