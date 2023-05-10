package subway.infrastructure.persistence.dao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.infrastructure.persistence.entity.SectionEntity;

@Repository
public class SectionDao {

    private static final RowMapper<SectionEntity> sectionRowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance"),
            rs.getLong("line_id")
    );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate template) {
        this.template = template;
        this.simpleJdbcInsert = new SimpleJdbcInsert(template)
                .withTableName("sections")
                .usingGeneratedKeyColumns("id");
    }

    public void batchSave(final List<SectionEntity> sectionEntities) {
        final SqlParameterSource[] array = sectionEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[sectionEntities.size()]);
        simpleJdbcInsert.executeBatch(array);
    }

    public void deleteAllByLineName(final String name) {
        final String sql = "DELETE FROM SECTIONS WHERE line_id = ("
                + "SELECT id FROM LINE WHERE name = ?"
                + ");";
        template.update(sql, name);
    }

    public List<SectionEntity> findAllByLineName(final String name) {
        final String sql =
                "SELECT * FROM sections JOIN line ON line.id = sections.line_id WHERE line.name = ?";
        return template.query(sql, sectionRowMapper, name);
    }
}
