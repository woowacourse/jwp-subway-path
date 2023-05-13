package subway.dao.v2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.StationDomain;

import java.util.Optional;

import static subway.dao.support.SqlHelper.*;

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

    private final RowMapper<StationDomain> rowMapper = (rs, rowNum) ->
            new StationDomain(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public Long insert(final String stationName) {
        final SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("name", stationName);

        return insertAction.executeAndReturnKey(paramSource).longValue();
    }

    public Optional<StationDomain> findByStationId(final Long stationId) {
        final String sql = sqlHelper()
                .select()
                        .columns("id, name")
                .from()
                        .table("STATIONS")
                .where()
                        .condition("id = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
