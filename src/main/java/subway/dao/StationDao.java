package subway.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.subway.Station;
import subway.entity.StationEntity;

@Repository
public class StationDao {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
		new StationEntity(
			rs.getLong("stationId"),
			rs.getString("name")
		);

	public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertAction = new SimpleJdbcInsert(dataSource)
			.withTableName("station")
			.usingGeneratedKeyColumns("stationId");
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public long insert(final Station station) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(station);
		return insertAction.executeAndReturnKey(params).longValue();
	}

	public List<StationEntity> findAll() {
		String sql = "SELECT stationId, name FROM station";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public Optional<StationEntity> findById(final Long id) {
		String sql = "SELECT stationId, name FROM station WHERE stationId = :stationId";
		return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("stationId", id), rowMapper).stream()
			.findAny();
	}

	public void update(final long id, final Station station) {
		String sql = "UPDATE station SET name = ? WHERE stationId = ?";
		jdbcTemplate.update(sql, station.getName(), id);
	}

	public void deleteById(final Long id) {
		String sql = "DELETE FROM station WHERE stationId = ?";
		jdbcTemplate.update(sql, id);
	}
}
