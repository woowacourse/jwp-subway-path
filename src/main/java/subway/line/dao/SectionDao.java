package subway.line.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.line.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity.Builder()
                    .id(rs.getLong("id"))
                    .upstreamId(rs.getLong("upstream_id"))
                    .downstreamId(rs.getLong("downstream_id"))
                    .lineId(rs.getLong("line_id"))
                    .distance(rs.getInt("distance"))
                    .build();

    @Autowired
    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertSections(List<SectionEntity> sectionEntities) {
        String sql = "insert into SECTION (upstream_id, downstream_id, line_id, distance) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, sectionEntities.get(i).getUpstreamId());
                ps.setLong(2, sectionEntities.get(i).getDownstreamId());
                ps.setLong(3, sectionEntities.get(i).getLineId());
                ps.setInt(4, sectionEntities.get(i).getDistance());
            }

            @Override
            public int getBatchSize() {
                return sectionEntities.size();
            }
        });
    }

    public List<SectionEntity> findSectionsByLineId(long lineId) {
        String sql = "select * from SECTION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteSectionsByLineId(long lineId) {
        String sql = "delete from SECTION where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
