package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity findById(Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Long save(StationEntity stationEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(stationEntity)).longValue();
    }

    public StationEntity findByName(String name) {
        final String sql = "SELECT * FROM station WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public int deleteByName(String name) {
        final String sql = "DELETE FROM station WHERE name = ?";
        return jdbcTemplate.update(sql, name);
    }

    public boolean isExisted(String name) {
        final String sql = "SELECT EXISTS(SELECT * FROM station WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }
}
