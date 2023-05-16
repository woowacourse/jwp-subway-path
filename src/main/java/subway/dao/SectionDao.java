package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dto.SectionDto;

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

    public Long insert(SectionDto sectionDto) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(sectionDto);

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public void insertAll(List<SectionDto> sectionDtos) {
        SqlParameterSource[] parameterSources = sectionDtos.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(parameterSources);
    }

    public List<SectionDto> findAllByLineId(Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new SectionDto(
                        rs.getLong("line_id"),
                        rs.getLong("station_id"),
                        rs.getLong("next_station_id"),
                        rs.getInt("distance")
                ),
                lineId
        );
    }

    public int deleteAllByLineId(Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";

        return jdbcTemplate.update(sql, lineId);
    }
}
