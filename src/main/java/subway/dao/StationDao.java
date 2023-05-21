package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import java.util.List;

@Repository
public class StationDao {

    private static final String TABLE_NAME = "station";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ALL_COLUMN = String.join(", ", ID, NAME);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong(ID),
                    rs.getString(NAME)
            );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID);
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select " + ALL_COLUMN + " from " + TABLE_NAME;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(Long id) {
        String sql = "select " + ALL_COLUMN + " from " + TABLE_NAME + " where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Station newStation) {
        String sql = "update " + TABLE_NAME + " set name = ? where id = ?";
        jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from " + TABLE_NAME + " where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
