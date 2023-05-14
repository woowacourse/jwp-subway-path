package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.SectionEntity;

import java.util.List;

@Component
public class SectionJdbcDao implements SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<SectionEntity> sectionRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getString("up_station_name"),
                    rs.getString("down_station_name"),
                    rs.getLong("distance")
            );

    public SectionJdbcDao(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveSection(final Long lineId, final List<SectionEntity> sectionEntities) {
        jdbcTemplate.update("drop table section where line_id = ?", lineId);

        final BeanPropertySqlParameterSource[] parameterSources = sectionEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        insert.executeBatch(parameterSources);
    }

    @Override
    public List<SectionEntity> findAllByLineId(final Long lineId) {
        String sql = "select * from section where line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }
}
