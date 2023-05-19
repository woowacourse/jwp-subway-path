package subway.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;
import subway.exception.DuplicatedLineNameException;

import javax.sql.DataSource;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(final LineEntity lineEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        try {
            final long id = insertAction.executeAndReturnKey(params).longValue();
            return LineEntity.of(id, lineEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedLineNameException();
        }
    }

    public void deleteById(final long id) {
        final String sql = "DELETE FROM line WHERE id = ?";
        final int result = jdbcTemplate.update(sql, id);
        validateUpdateResult(result);
    }

    private void validateUpdateResult(final int result) {
        if (result == 0) {
            throw new RuntimeException("존재하지 않는 line id 입니다.");
        }
    }
}
