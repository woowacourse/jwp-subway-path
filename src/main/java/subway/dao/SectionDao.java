package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dto.SectionDto;
import subway.entity.SectionEntity;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("line_id"),
                    rs.getLong("upper_station"),
                    rs.getLong("lower_station"),
                    rs.getInt("distance")
            );

    public Long insert(final SectionDto sectionDto) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(sectionDto);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public void insertAll(final List<SectionDto> sectionDtos) {
        SqlParameterSource[] parameterSources = sectionDtos.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(parameterSources);
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT * FROM section";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public int deleteAllByLineId(final Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";

        return jdbcTemplate.update(sql, lineId);
    }
}
