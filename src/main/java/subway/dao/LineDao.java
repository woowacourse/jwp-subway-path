package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {

    private static final String TABLE_NAME = "line";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String EXTRA_FEE = "extra_fee";
    private static final String ALL_COLUMN = String.join(", ", List.of(ID, NAME, COLOR, EXTRA_FEE));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong(ID),
                    rs.getString(NAME),
                    rs.getString(COLOR),
                    rs.getInt(EXTRA_FEE));

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID);
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put(ID, line.getId());
        params.put(NAME, line.getName());
        params.put(COLOR, line.getColor());
        params.put(EXTRA_FEE, line.getExtraFee());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor(), line.getExtraFee());
    }

    public List<Line> findAll() {
        String sql = "select " + ALL_COLUMN + " from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(Long id) {
        String sql = "select " + ALL_COLUMN + " from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ?, extra_fee = ? where id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getExtraFee(), newLine.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
