package subway.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.exception.bad_request.DuplicatedLineNameException;
import subway.persistence.entity.LineEntity;

import javax.sql.DataSource;

@Repository
public class LineDao {

    private final SimpleJdbcInsert insertAction;

    public LineDao(DataSource dataSource) {
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

}
