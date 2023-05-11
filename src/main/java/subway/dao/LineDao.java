package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.LineInfo;

@Repository
public class LineDao {
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;
	private final RowMapper<LineInfo> lineRowMapper = (rs, rowNum) ->
		new LineInfo(
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

	public LineInfo insert(LineInfo line) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", line.getId());
		params.put("name", line.getName());
		params.put("color", line.getColor());

		Long lineId = insertAction.executeAndReturnKey(params).longValue();
		return new LineInfo(lineId, line.getName(), line.getColor());
	}

	public List<LineInfo> findAll() {
		String sql = "select id, name, color from LINE";
		return jdbcTemplate.query(sql, lineRowMapper);
	}

	public LineInfo findById(Long id) {
		String sql = "select id, name, color from LINE WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, lineRowMapper, id);
	}

	public void update(LineInfo newLine) {
		String sql = "update LINE set name = ?, color = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] {newLine.getName(), newLine.getColor(), newLine.getId()});
	}

	public void deleteById(Long id) {
		jdbcTemplate.update("delete from Line where id = ?", id);
	}
}
