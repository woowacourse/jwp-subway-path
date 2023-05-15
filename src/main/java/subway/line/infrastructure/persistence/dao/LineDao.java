package subway.line.infrastructure.persistence.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.infrastructure.persistence.entity.LineEntity;

@Repository
public class LineDao {

    private static final RowMapper<LineEntity> lineRowMapper = (rs, rowNum) -> new LineEntity(
            UUID.fromString(rs.getString("domain_id")),
            rs.getString("name"),
            rs.getInt("surcharge")
    );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public LineDao(final JdbcTemplate template) {
        this.template = template;
        this.simpleJdbcInsert = new SimpleJdbcInsert(template)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public void save(final LineEntity entity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        simpleJdbcInsert.execute(source);
    }

    public void delete(final LineEntity entity) {
        template.update("delete from line where line.domain_id = ?", entity.domainId());
    }

    public Optional<LineEntity> findById(final UUID id) {
        try {
            final String sql = "select * from line where line.domain_id = ?";
            return Optional.ofNullable(template.queryForObject(sql, lineRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String name) {
        try {
            final String sql = "select * from line where line.name = ?";
            return Optional.ofNullable(template.queryForObject(sql, lineRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        return template.query("select * from line", lineRowMapper);
    }
}
