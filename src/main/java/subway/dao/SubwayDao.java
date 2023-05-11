package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubwayDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<LineEntity> lineRowMapper = (rs, cn) -> new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color")
    );
    private final RowMapper<SectionEntity> sectionMapper = (rs, cn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getString("up"),
            rs.getString("down"),
            rs.getInt("distance")
    );

    public SubwayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line findById(Integer lineId) {
        String lineEntitySql = "select id, name, color from line where id = ?";
        LineEntity lineEntity = jdbcTemplate.queryForObject(lineEntitySql, lineRowMapper, lineId);

        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";
        List<SectionEntity> sectionEntities = jdbcTemplate.query(sectionSql, sectionMapper, lineId);
        List<Section> sections = sectionEntities.stream()
                .map((SectionEntity::toDomain))
                .collect(Collectors.toList());

        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                sections
        );
    }

    public void save(Line line) {
        String deleteSql = "delete from section where line_id = ?";
        String insertSql = "insert into section (line_id, up, down, distance) values (?, ?, ?, ?)";

        jdbcTemplate.update(deleteSql, line.getId());
        List<SectionEntity> sections = line.getSections()
                        .stream().map(section -> new SectionEntity(
                                line.getId(),
                                section.getLeft().getName(),
                        section.getRight().getName(),
                        section.getDistance().getDistance()
                        )).collect(Collectors.toList());

        jdbcTemplate.batchUpdate(insertSql, sections, 50,
                (ps, entity) -> {
                    ps.setLong(1, entity.getLineId());
                    ps.setString(2, entity.getLeft());
                    ps.setString(3, entity.getRight());
                    ps.setInt(4, entity.getDistance());
                });
    }
}
