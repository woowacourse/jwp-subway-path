package subway.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.entity.vo.SectionVo;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("sections");
    }


    public void insertSection(final Section section, final long lineId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("up_id", section.getUpStation().getId())
                .addValue("down_id", section.getDownStation().getId())
                .addValue("line_id", lineId)
                .addValue("distance", section.getDistance().getValue());

        simpleJdbcInsert.execute(params);
    }

    public void insertSections(List<Section> sectionEntities, long lineId) {
        String sql = "INSERT INTO sections (up_id, down_id, distance, line_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                Section section = sectionEntities.get(i);
                ps.setLong(1, section.getUpStation().getId());
                ps.setLong(2, section.getDownStation().getId());
                ps.setInt(3, section.getDistance().getValue());
                ps.setLong(4, lineId);
            }

            @Override
            public int getBatchSize() {
                return sectionEntities.size();
            }
        });
    }

    public List<SectionVo> findSectionsByLineId(final long lineId) {
        String sql = "SELECT S1.id, S1.name, S2.id, S2.name, SEC.distance"
                + " FROM SECTIONS AS SEC "
                + " INNER JOIN STATION AS S1 ON SEC.up_id = S1.id "
                + " INNER JOIN STATION AS S2 ON SEC.down_id = S2.id "
                + " WHERE SEC.line_id = ?";

        return jdbcTemplate.query(sql, sectionVoMapper(), lineId);
    }

    private RowMapper<SectionVo> sectionVoMapper() {
        return (rs, rowNum) -> SectionVo.of(
                rs.getLong(1), rs.getString(2),
                rs.getLong(3), rs.getString(4),
                rs.getInt(5));
    }

    public void deleteSections(List<Section> sections, long lineId) {
        String sql = "DELETE sections WHERE up_id = ? AND down_id = ? AND line_id =?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                Section section = sections.get(i);
                ps.setLong(1, section.getUpStation().getId());
                ps.setLong(2, section.getDownStation().getId());
                ps.setLong(3, lineId);
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

}
