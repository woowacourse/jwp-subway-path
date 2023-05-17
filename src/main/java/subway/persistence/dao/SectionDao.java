package subway.persistence.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.SectionEntity;

import java.util.List;

@Repository
public class SectionDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<SectionEntity> rowMapper = (resultSet, rowNumber) -> new SectionEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getLong("upward_station_id"),
            resultSet.getLong("downward_station_id"),
            resultSet.getInt("distance")
    );

    public SectionDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("section")
                .usingColumns("line_id", "upward_station_id", "downward_station_id", "distance")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(SectionEntity sectionEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public List<SectionEntity> findAllByLineId(Long id) {
        String sql = "SELECT id, line_id, upward_station_id, downward_station_id, distance FROM section WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", id);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, rowMapper);
    }

    public void deleteAllByLineId(Long id) {
        String sql = "DELETE FROM section WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
