package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static subway.dao.support.SqlHelper.sqlHelper;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public Long insert(final StationEntity stationEntity) {
        return insertAction.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", stationEntity.getName())
        ).longValue();
    }

    public Optional<StationEntity> findByStationId(final Long stationId) {
        final String sql = sqlHelper()
                .select().columns("id, name")
                .from().table("station")
                .where().condition("id = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findInStationIds(final List<Long> stationIds) {
        final String ids = stationIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        final String sql = sqlHelper()
                .select().columns("id, name")
                .from().table("station")
                .where().in("id", ids)
                .toString();

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findByStationName(final String stationName) {
        final String sql = sqlHelper()
                .select().columns("id, name")
                .from().columns("station")
                .where().condition("name = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
