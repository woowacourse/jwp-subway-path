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
import subway.exception.StationNotFoundException;

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

	public String insert(final Station station) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(station);
		final long stationId = insertAction.executeAndReturnKey(params).longValue();

		return findById(stationId).orElseThrow(StationNotFoundException::new).getName();
	}

	public List<StationEntity> findAll() {
		String sql = "SELECT stationId, name FROM station";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public Optional<StationEntity> findById(final Long stationId) {
		String sql = "SELECT stationId, name FROM station WHERE stationId = :stationId";
		return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("stationId", stationId), rowMapper).stream()
			.findAny();
	}

	public Optional<StationEntity> findByName(final String findByName) {
		String sql = "SELECT stationId, name FROM station WHERE name = :name";
		return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("name", findByName), rowMapper).stream()
			.findAny();
	}

	public void update(final String stationName, final Station newStation) {
		String sql = "UPDATE station SET name = ? WHERE name = ?";
		jdbcTemplate.update(sql, newStation.getName(), stationName);
	}

	public void updateById(final long id, final Station station) {
		String sql = "UPDATE station SET name = ? WHERE stationId = ?";
		jdbcTemplate.update(sql, station.getName(), id);
	}

	public void deleteByName(final String stationName) {
		String sql = "DELETE FROM station WHERE name = ?";
		jdbcTemplate.update(sql, stationName);
	}
}
