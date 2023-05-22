package subway.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.infrastructure.entity.StationRow;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationRow> rowMapper = (rs, rowNum) ->
            new StationRow(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationRow insert(StationRow row) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(row);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationRow(id, row.getName());
    }

    public List<StationRow> selectAll() {
        String sql = "select * from station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationRow selectById(Long id) {
        String sql = "select * from station where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Map<Long, String> selectKeyValueSetWhereIdIn(List<Long> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("select id, name from station where id in (%s)", inSql);

        List<Map.Entry<Long, String>> nameIdKeyValue = jdbcTemplate.query(sql,
                (rs, cn) -> Map.entry(rs.getLong("id"), rs.getString("name")),
                ids.toArray());

        return nameIdKeyValue.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void update(StationRow row) {
        String sql = "update station set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{row.getName(), row.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from station where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
