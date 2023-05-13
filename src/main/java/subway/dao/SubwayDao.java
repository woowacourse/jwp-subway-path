package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public Line findById(Long lineId) {
        String lineEntitySql = "select id, name, color from line where id = ?";
        LineEntity lineEntity = jdbcTemplate.queryForObject(lineEntitySql, lineRowMapper, lineId);

        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";
        List<SectionEntity> sectionEntities = jdbcTemplate.query(sectionSql, sectionMapper, lineId);

        Map<String, Long> stationIdsOf = getStationIdsOf(sectionEntities);
        List<Section> sections = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            String leftStationName = sectionEntity.getLeft();
            String rightStationName = sectionEntity.getRight();
            sections.add(new Section(
                    new Station(stationIdsOf.get(leftStationName), leftStationName),
                    new Station(stationIdsOf.get(rightStationName), rightStationName),
                    new Distance(sectionEntity.getDistance())
            ));
        }
        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                sections
        );
    }

    private Map<String, Long> getStationIdsOf(List<SectionEntity> sectionEntities) {
        List<String> stationNames = getStationNames(sectionEntities);
        Map<String, Long> result = new HashMap<>();
        String sql = "select id from station where name = ?";
        for (String stationName : stationNames) {
            Long stationId = jdbcTemplate.queryForObject(sql, Long.class, stationName);
            result.put(stationName, stationId);
        }
        return result;
    }

    private List<String> getStationNames(List<SectionEntity> sectionEntities) {
        Set<String> stationNames = new HashSet<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            stationNames.add(sectionEntity.getLeft());
            stationNames.add(sectionEntity.getRight());
        }
        return new ArrayList<>(stationNames);
    }

    public void save(Line line) {
        String deleteSql = "delete from section where line_id = ?";
        String insertSql = "insert into section (line_id, up, down, distance) values (?, ?, ?, ?)";

        jdbcTemplate.update(deleteSql, line.getId());
        List<SectionEntity> sections = line.getSections()
                .stream().map(section -> new SectionEntity(
                        line.getId(),
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        section.getDistance().value()
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
