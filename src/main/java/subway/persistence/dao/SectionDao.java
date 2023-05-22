package subway.persistence.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getString("upstation"),
                    rs.getString("downstation"),
                    rs.getLong("line_id"),
                    rs.getLong("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(List<SectionEntity> sections) {
        final String sql = "INSERT INTO section (distance, upstation, downstation, line_id) VALUES (?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final SectionEntity section = sections.get(i);
                ps.setLong(1, section.getDistance());
                ps.setString(2, section.getUpStation());
                ps.setString(3, section.getDownStation());
                ps.setLong(4, section.getLineId());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteSections(final List<SectionEntity> sectionEntities) {
        String sql = "DELETE FROM section WHERE upstation = ? and downstation = ? and distance = ? and line_id = ?";
        for (SectionEntity section : sectionEntities) {
            jdbcTemplate.update(sql, section.getUpStation(), section.getDownStation(), section.getDistance(), section.getLineId());
        }
    }
}
