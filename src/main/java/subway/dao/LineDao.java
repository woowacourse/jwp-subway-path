package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
        new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getLong("head_station")
        );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    // TODO: 2023/05/11 name을 받고 id를 찾아 반환하는 메서드 구현하기 
    public LineEntity insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("head_station", line.getHeadStation().getId());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor(),
            line.getHeadStation().getId());
    }

    public Long findIdByName(String name) {
        String sql = "select id from LINE where name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), name);
    }

    public List<LineEntity> findAll() {
        String sql = "select * from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "select * from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

//    public void update(Line newLine) {
//        String sql = "update LINE set name = ?, color = ? where id = ?";
//        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId());
//    }

    public void updateHeadStation(Line line, Long headStation) {
        String sql = "update LINE set head_station = ? where id = ?";
        jdbcTemplate.update(sql, headStation, line.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
