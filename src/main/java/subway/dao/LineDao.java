package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.dto.LineDto;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineDto> rowMapper = (rs, rowNum) ->
            new LineDto(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineDto insert(LineDto lineDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineDto.getId());
        params.put("name", lineDto.getName());
        params.put("color", lineDto.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineDto(lineId, lineDto.getName(), lineDto.getColor());
    }

    public List<LineDto> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineDto findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(LineDto lineDto) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{lineDto.getName(), lineDto.getColor(), lineDto.getId()});
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
