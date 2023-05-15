//package subway.dao;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//import subway.domain.Line;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class LineDao {
//    private final JdbcTemplate jdbcTemplate;
//    private final SimpleJdbcInsert insertAction;
//
//    private RowMapper<Line> rowMapper = (rs, rowNum) ->
//            new Line(
//                    rs.getLong("id"),
//                    rs.getString("name"),
//                    rs.getString("color"),
//                    sections);
//
//    private final RowMapper<Boolean> booleanMapper = (resultSet, rowNum) -> resultSet.getBoolean("isExist");
//
//
//    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.insertAction = new SimpleJdbcInsert(dataSource)
//                .withTableName("line")
//                .usingGeneratedKeyColumns("id");
//    }
//
//    public Line insert(Line line) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", line.getId());
//        params.put("name", line.getNameValue());
//        params.put("color", line.getColorValue());
//
//        Long lineId = insertAction.executeAndReturnKey(params).longValue();
//        return new Line(lineId, line.getNameValue(), line.getColorValue(), sections);
//    }
//
//    public List<Line> findAll() {
//        String sql = "select id, name, color from LINE";
//        return jdbcTemplate.query(sql, rowMapper);
//    }
//
//    public Line findById(Long id) {
//        String sql = "select id, name, color from LINE WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, rowMapper, id);
//    }
//
//    public Boolean hasId(final Long id) {
//        String sql = "select exists(select id, name, color from LINE WHERE id = ?) as isExist";
//        return jdbcTemplate.queryForObject(sql, booleanMapper, id);
//    }
//
//    public void update(Line newLine) {
//        String sql = "update LINE set name = ?, color = ? where id = ?";
//        jdbcTemplate.update(sql, new Object[]{newLine.getNameValue(), newLine.getColorValue(), newLine.getId()});
//    }
//
//    public void deleteById(Long id) {
//        jdbcTemplate.update("delete from Line where id = ?", id);
//    }
//}
