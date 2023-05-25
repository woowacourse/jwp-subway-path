package subway.persistence.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.StationEntity;

@Repository
public class StationDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<StationEntity> rowMapper = (resultSet, rowNumber) -> new StationEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getString("name")
    );

    public StationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("station")
                .usingColumns("line_id", "name")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(StationEntity stationEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(stationEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public Optional<StationEntity> findById(long id) {
        String sql = "SELECT id, line_id, name FROM station WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteAllByLineId(Long id) {
        String sql = "DELETE FROM station WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public Optional<StationEntity> findByLineIdAndName(Long lineId, String name) {
        String sql = "SELECT id, line_id, name FROM station WHERE (line_id=:line_id AND name=:name)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("line_id", lineId)
                .addValue("name", name);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
