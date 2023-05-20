package subway.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Station;

@Repository
public class StationDao {
	private static final int UPDATED_COUNT = 1;
	private static final int DELETED_COUNT = 1;
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insert;

	private final RowMapper<Station> stationRowMapper = (rs, rowNum) ->
		new Station(
			rs.getLong("id"),
			rs.getString("name")
		);

	public StationDao(final JdbcTemplate jdbcTemplate) {
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("station")
			.usingGeneratedKeyColumns("id");
		this.jdbcTemplate = jdbcTemplate;
	}

	public long createStation(final Station station) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(station);
		return insert.executeAndReturnKey(params).longValue();
	}

	public List<Station> findAll() {
		String sql = "SELECT * FROM station";
		return jdbcTemplate.query(sql, stationRowMapper);
	}

	public Station findById(final Long stationIdRequest) {
		String sql = "SELECT * FROM station WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, stationRowMapper, stationIdRequest);
	}

	public boolean updateStation(final long stationId, final Station station) {
		final String sql = "UPDATE station SET name = ? WHERE id = ?";
		final int updateCount = jdbcTemplate.update(sql, station.getName(), stationId);

		return updateCount == UPDATED_COUNT;
	}

	public boolean deleteById(final Long stationId) {
		String sql = "DELETE FROM station WHERE id = ?";
		final int deleteCount = jdbcTemplate.update(sql, stationId);

		return deleteCount == DELETED_COUNT;
	}

	public Station findStationWithId(final Station station) {
		String sql = "SELECT * FROM station WHERE name = ?";
		return jdbcTemplate.queryForObject(sql,stationRowMapper, station.getName());
	}
}
