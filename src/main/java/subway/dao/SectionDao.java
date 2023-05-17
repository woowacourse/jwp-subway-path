package subway.dao;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
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
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("sections")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void batchInsert(final List<SectionEntity> sectionEntities) {
        final SqlParameterSource[] array = sectionEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[sectionEntities.size()]);

        simpleJdbcInsert.executeBatch(array);
    }

    public void batchDelete(final List<SectionEntity> sectionEntities) {
        final String sql = "DELETE FROM SECTIONS WHERE id=:id";
        final SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(sectionEntities.toArray());
        final int[] ints = namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
        System.out.println(ints.length);
    }

    public void deleteAllByLineName(final String lineName) {
        String sql = "DELETE FROM SECTIONS WHERE LINE_ID IN (SELECT id FROM LINES WHERE name = ?)";
        jdbcTemplate.update(sql, lineName);
    }

    public List<SectionEntity> findAllByLineName(final String lineName) {
        String sql = "SELECT * FROM SECTIONS JOIN LINES ON LINES.id=SECTIONS.line_id WHERE LINES.name = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineName);
    }
}
