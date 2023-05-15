package subway.line.infrastructure.persistence.dao;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.infrastructure.persistence.entity.SectionEntity;

@Repository
public class SectionDao {

    private static final RowMapper<SectionEntity> sectionRowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            UUID.fromString(rs.getString("up_station_domain_id")),
            UUID.fromString(rs.getString("down_station_domain_id")),
            rs.getInt("distance"),
            UUID.fromString(rs.getString("line_domain_id"))
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
        final String sql = "DELETE FROM SECTIONS WHERE line_domain_id = ("
                + "SELECT domain_id FROM LINE WHERE name = ?"
                + ");";
        template.update(sql, name);
    }

    public List<SectionEntity> findAllByLineName(final String name) {
        final String sql =
                "SELECT * FROM sections JOIN line ON line.domain_id = sections.line_domain_id WHERE line.name = ?";
        return template.query(sql, sectionRowMapper, name);
    }
}
