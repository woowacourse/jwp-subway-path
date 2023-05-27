package subway.dao.section;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("section_id"),
                    rs.getLong("first_station_id"),
                    rs.getLong("second_station_id"),
                    rs.getInt("distance"),
                    rs.getLong("line_id")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("section_id");
    }

    public SectionEntity insert(final SectionEntity section) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        final Long id = insertAction.executeAndReturnKey(params).longValue();

        return new SectionEntity(
                id,
                section.getFirstStationId(),
                section.getSecondStationId(),
                section.getDistance(),
                section.getLineId()
        );
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public SectionEntity findById(final Long id) {
        final String sql = "SELECT * FROM section WHERE section_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteByStations(final Long firstStation, final Long secondStation) {
        final String sql = "DELETE FROM section WHERE first_station_id = ? AND second_station_id = ?";

        jdbcTemplate.update(con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, firstStation);
            preparedStatement.setLong(2, secondStation);

            return preparedStatement;
        });
    }
}