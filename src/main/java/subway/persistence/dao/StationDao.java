package subway.persistence.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.dao.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<StationEntity> stationMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public StationEntity insert(StationEntity stationEntity) {
        try {
            SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
            long stationId = insertAction.executeAndReturnKey(params).longValue();
            return new StationEntity(stationId, stationEntity.getName());
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("해당 역 이름이 이미 존재합니다.");
        }
    }

    public List<StationEntity> findStationsById(Set<Long> ids) {
        String sql = "SELECT * FROM station WHERE id IN (:ids)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, stationMapper);
    }

    public StationEntity findById(Long id) {
        try {
            String sql = "select * from STATION where id = ?";
            return jdbcTemplate.queryForObject(sql, stationMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 stationId입니다.");
        }

    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
