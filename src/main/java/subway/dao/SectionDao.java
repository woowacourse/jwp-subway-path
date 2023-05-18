package subway.dao;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private RowMapper<SectionEntity> sectionRowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance"));


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("sections")
                .usingGeneratedKeyColumns("id");
    }

    public void batchInsert(final List<SectionEntity> sectionEntities) {
        final SqlParameterSource[] array = sectionEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[sectionEntities.size()]);

        simpleJdbcInsert.executeBatch(array);
    }

    public void deleteAllByLineId(final Long sectionId) {
        String sql = "DELETE FROM SECTIONS WHERE line_id = ?";
        jdbcTemplate.update(sql, sectionId);
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        String sql = "SELECT * FROM SECTIONS WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }
}
