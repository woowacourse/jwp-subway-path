package subway.line.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SectionDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
      new SectionEntity(
          rs.getLong("id"),
          rs.getString("current_station_name"),
          rs.getString("next_station_name"),
          rs.getInt("distance"),
          rs.getLong("line_id")
      );

  public SectionDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("SECTION")
        .usingGeneratedKeyColumns("id");
  }

  public void save(final SectionEntity sectionEntity) {
    simpleJdbcInsert.execute(new BeanPropertySqlParameterSource(sectionEntity));
  }

  public List<SectionEntity> findSectionsByLineId(final Long lineId) {
    final String sql = "SELECT * FROM SECTION S WHERE S.line_id = ?";

    return jdbcTemplate.query(sql, rowMapper, lineId);
  }

  public void deleteAll(final Long lineId) {
    final String sql = "DELETE FROM SECTION S WHERE S.line_id = ?";

    jdbcTemplate.update(sql, lineId);
  }

  public void update(final SectionEntity sectionEntity) {
    final String sql = "UPDATE SECTION S SET S.current_station_name = ?, "
        + "S.next_station_name = ?,"
        + "S.distance = ? "
        + "WHERE S.id = ?";

    jdbcTemplate.update(
        sql,
        sectionEntity.getCurrentStationName(),
        sectionEntity.getNextStationName(),
        sectionEntity.getDistance(),
        sectionEntity.getId()
    );
  }

  public void deleteById(final Long sectionId) {
    final String sql = "DELETE FROM SECTION S WHERE S.id = ?";

    jdbcTemplate.update(sql, sectionId);
  }
}
