package subway.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.exception.DuplicatedLineNameException;
import subway.persistence.entity.LineEntity;
import subway.exception.LineNotFoundException;

import javax.sql.DataSource;
import java.util.List;

import static subway.persistence.entity.RowMapperUtil.lineEntityRowMapper;

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

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, lineEntityRowMapper);
    }

    // TODO: 꼭 queryForList로 해야하나? 다른 방법 찾아보자. 옵셔널 말고.
    public LineEntity findByName(final String name) {
        final String sql = "SELECT * FROM line WHERE name = ?";
        final List<LineEntity> result = jdbcTemplate.query(sql, lineEntityRowMapper, name);

        if (result.isEmpty()) {
            throw new LineNotFoundException();
        }

        return result.get(0);
    }

    public LineEntity findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, lineEntityRowMapper, id);
    }

    public void update(LineEntity newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

}
