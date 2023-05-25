package subway.persistence.dao;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.TransferEntity;

@Repository
public class TransferDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<TransferEntity> rowMapper = (resultSet, rowNumber) -> new TransferEntity(
            resultSet.getLong("id"),
            resultSet.getLong("first_station_id"),
            resultSet.getLong("last_station_id")
    );

    public TransferDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("transfer")
                .usingColumns("first_station_id", "last_station_id")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(TransferEntity transferEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(transferEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public List<TransferEntity> findAll() {
        String sql = "SELECT id, first_station_id, last_station_id FROM transfer";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }
}
