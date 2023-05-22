package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.FarePolicyEntity;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FarePolicyDao {

    public static final RowMapper<FarePolicyEntity> farePolicyRowMapper = (rs, rowNum) -> new FarePolicyEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getInt("additional_fare")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public FarePolicyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(FarePolicyEntity farePolicyEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", farePolicyEntity.getLineId());
        params.put("additional_fare", farePolicyEntity.getAdditionalFare());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<FarePolicyEntity> findAll() {
        String sql = "SELECT * FROM fare_policy";
        return jdbcTemplate.query(sql, farePolicyRowMapper);
    }

    public List<FarePolicyEntity> findById(Long id) {
        String sql = "SELECT * FROM fare_policy WHERE line_id = ?";
        return jdbcTemplate.query(sql, farePolicyRowMapper, id);
    }

}
