package subway.dao.v2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class StationDaoV2 {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDaoV2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("STATIONS")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final String stationName) {
        final SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("name", stationName);

        return insertAction.executeAndReturnKey(paramSource).longValue();
    }
}
