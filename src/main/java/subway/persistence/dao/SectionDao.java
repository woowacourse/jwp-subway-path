package subway.persistence.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.SectionEntity;

@Repository
public class SectionDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<SectionEntity> rowMapper = (resultSet, rowNumber) -> new SectionEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getString("upward_station"),
            resultSet.getString("downward_station"),
            resultSet.getInt("distance")
    );

    public SectionDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("section")
                .usingColumns("line_id", "upward_station", "downward_station", "distance")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(SectionEntity sectionEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }
}
