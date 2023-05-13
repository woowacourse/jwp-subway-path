package subway.dao.v2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static subway.dao.support.SqlHelper.sqlHelper;

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

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public Long insert(final StationEntity stationEntity) {
        final SqlParameterSource paramSource = new BeanPropertySqlParameterSource(stationEntity);

        return insertAction.executeAndReturnKey(paramSource).longValue();
    }

    public Optional<StationEntity> findByStationId(final Long stationId) {
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

    public List<StationEntity> findInStationIds(final List<Long> stationIds) {
        final String ids = stationIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        final String sql = sqlHelper()
                .select().columns("id, name")
                .from().table("STATIONS")
                .where().in("id", ids)
                .toString();

        return jdbcTemplate.query(sql, rowMapper);
    }
}
