package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends Entity> {

    Long insert(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void update(T entity);

    void deleteById(Long id);

    default Optional<T> findInOptional(
            final JdbcTemplate jdbcTemplate,
            final String sql,
            final RowMapper<T> rowMapper,
            final Object... args
    ) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, args));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

}
