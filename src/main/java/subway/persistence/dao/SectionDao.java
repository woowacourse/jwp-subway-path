package subway.persistence.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import subway.persistence.entity.SectionEntity;

@Repository
public class SectionDao {

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
		new SectionEntity(
			rs.getLong("id"),
			rs.getLong("line_id"),
			rs.getLong("departure_id"),
			rs.getLong("arrival_id"),
			rs.getInt("distance")
		);

	public SectionDao(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public SectionEntity insert(final SectionEntity section) {

		final String sql = "INSERT INTO sections (line_id, departure_id, arrival_id, distance) VALUES (?, ?, ?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
			ps.setLong(1, section.getLineId());
			ps.setLong(2, section.getDepartureId());
			ps.setLong(3, section.getArrivalId());
			ps.setInt(4, section.getDistance());
			return ps;
		}, keyHolder);

		final long id = (long)Objects.requireNonNull(keyHolder.getKeys()).get("id");

		return new SectionEntity(id, section.getLineId(), section.getDepartureId(), section.getArrivalId(),
			section.getDistance());
	}

	public List<SectionEntity> findAll() {
		final String sql = "SELECT id, line_id, departure_id, arrival_id, distance FROM sections";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public SectionEntity findById(final Long id) {
		final String sql = "SELECT id, line_id, departure_id, arrival_id, distance FROM sections WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, id);
	}

	public void update(final SectionEntity newSection) {
		final String sql = "UPDATE sections SET line_id = ? departure_id = ?, arrival_id = ?, distance =? WHERE id = ?";
		jdbcTemplate.update(sql,
			newSection.getLineId(), newSection.getDepartureId(), newSection.getArrivalId(),
			newSection.getDistance(), newSection.getId());
	}

	public void deleteById(final Long id) {
		final String sql = "DELETE FROM sections WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
}
