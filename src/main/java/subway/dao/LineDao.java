package subway.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Line;

@Repository
public class LineDao {

	private static final int DELETED_COUNT = 1;
	private static final int UPDATED_COUNT = 1;
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insert;

	private final RowMapper<Line> lineRowMapper = (rs, rowNum) ->
		new Line(
			rs.getLong("id"),
			rs.getString("name")
		);

	public LineDao(final JdbcTemplate jdbcTemplate) {
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("line")
			.usingGeneratedKeyColumns("id");
		this.jdbcTemplate = jdbcTemplate;
	}

	public long createLine(final Line line) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(line);
		return insert.executeAndReturnKey(params).longValue();
	}

	public List<Line> findAll() {
		String sql = "SELECT * FROM line";
		return jdbcTemplate.query(sql, lineRowMapper);
	}

	public Line findById(final long lineId) {
		String sql = "SELECT * FROM line WHERE id = ?";
		final Line line = jdbcTemplate.queryForObject(sql, lineRowMapper, lineId);
		if (line == null) {
			throw new NullPointerException("존재하지 않는 노선입니다");
		}
		return line;
	}

	public boolean updateLine(final long lineId, final Line line) {
		final String sql = "UPDATE line SET name = ? WHERE id = ?";
		final int updateCount = jdbcTemplate.update(sql, line.getName(), lineId);

		return updateCount == UPDATED_COUNT;
	}

	public boolean deleteById(final long lineId) {
		String sql = "DELETE FROM line WHERE id = ?";
		final int deleteCount = jdbcTemplate.update(sql, lineId);

		return deleteCount == DELETED_COUNT;
	}
}
