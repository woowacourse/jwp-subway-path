package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SectionDao {

	private final JdbcTemplate jdbcTemplate;

	public SectionDao(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
		new SectionEntity(
			rs.getLong("sectionId"),
			rs.getLong("lineId"),
			rs.getLong("upStationId"),
			rs.getLong("downStationId"),
			rs.getLong("distance")
		);

	public List<SectionEntity> findSectionsByLineId(final Long lineId) {
		String sql = "SELECT sectionId, lineId, upStationId, downStationId, distance FROM section WHERE lineId = ?";
		return jdbcTemplate.query(sql, rowMapper, lineId);
	}

	public void deleteByLineId(final long lineId) {
		String sql = "DELETE FROM section WHERE lineId = ?";
		jdbcTemplate.update(sql, lineId);
	}

	public void insertBatch(final List<SectionEntity> sectionEntities) {
		String sql = "INSERT INTO section (lineId, upStationId, downStationId, distance) VALUES (?, ?, ?, ?)";
		List<Object[]> batchValues = new ArrayList<>();

		for (SectionEntity entity : sectionEntities) {
			Object[] values = new Object[]{
				entity.getLineId(),
				entity.getUpStationId(),
				entity.getDownStationId(),
				entity.getDistance()
			};
			batchValues.add(values);
		}

		jdbcTemplate.batchUpdate(sql, batchValues);
	}
}
