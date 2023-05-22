package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<SectionEntity> sectionMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("left_station_id"),
                    rs.getLong("right_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity save(final SectionEntity sectionEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        Long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return SectionEntity.of(savedId, sectionEntity);
    }

    public void saveAll(final List<SectionEntity> sectionEntities) {
        BeanPropertySqlParameterSource[] parameterSources = sectionEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(parameterSources);
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        String sql = "select * from SECTION where line_id = ?";
        return jdbcTemplate.query(sql, sectionMapper, lineId);
    }

    public void deleteAllByLineId(final Long lineId) {
        String sql = "delete from SECTION where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
