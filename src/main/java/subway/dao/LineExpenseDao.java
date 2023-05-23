package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineExpenseEntity;

import java.util.List;
import java.util.Optional;

import static subway.dao.support.SqlHelper.sqlHelper;

@Repository
public class LineExpenseDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineExpenseDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line_expense")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<LineExpenseEntity> rowMapper = (rs, rowNum) ->
            new LineExpenseEntity(
                    rs.getLong("id"),
                    rs.getString("per_expense"),
                    rs.getInt("per_distance"),
                    rs.getLong("line_id")
            );

    public Long save(final LineExpenseEntity lineExpenseEntity) {
        return insertAction.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("per_expense", lineExpenseEntity.getPerExpense())
                .addValue("per_distance", lineExpenseEntity.getPerDistance())
                .addValue("line_id", lineExpenseEntity.getLineId()))
                .longValue();
    }

    public void update(final LineExpenseEntity lineExpenseEntity) {
        final String sql = sqlHelper()
                .update().table("line_expense")
                .set("per_expense = ?, per_distance = ?")
                .where().condition("line_id = ?")
                .toString();

        jdbcTemplate.update(sql,
                lineExpenseEntity.getPerExpense(),
                lineExpenseEntity.getPerDistance(),
                lineExpenseEntity.getLineId());
    }

    public Optional<LineExpenseEntity> findByLineId(final long lineId) {
        final String sql = sqlHelper()
                .select().columns("id, per_expense, per_distance, line_id")
                .from().table("line_expense")
                .where().condition("line_id = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineExpenseEntity> findAll() {
        final String sql = sqlHelper()
                .select().columns("id, per_expense, per_distance, line_id")
                .from().table("line_expense")
                .toString();

        return jdbcTemplate.query(sql, rowMapper);
    }
}
