package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineProperty;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class H2LineRepository {

    private static final int BATCH_SIZE = 50;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<LineProperty> propertyRowMapper = (rs, rowNum) -> new LineProperty(
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

    public H2LineRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line findById(Long linePropertyId) {
        String linePropertySql = "select id, name, color from line_property where id = ?";
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";

        List<SectionEntity> sectionEntities = jdbcTemplate.query(sectionSql, sectionMapper, linePropertyId);
        LineProperty lineProperty = jdbcTemplate.queryForObject(linePropertySql, propertyRowMapper, linePropertyId);

        Map<String, Long> stationIds = getStationIdsOf(sectionEntities);
        List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        new Station(stationIds.get(sectionEntity.getLeft()), sectionEntity.getLeft()),
                        new Station(stationIds.get(sectionEntity.getRight()), sectionEntity.getRight()),
                        new Distance(sectionEntity.getDistance())
                )).collect(Collectors.toList());

        return new Line(lineProperty, sections);
    }


    private Map<String, Long> getStationIdsOf(List<SectionEntity> sectionEntities) {
        String sql = "select id from station where name = ?";
        List<String> stationNames = getStationNames(sectionEntities);
        Map<String, Long> result = new HashMap<>();
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

    public void removeSections(Long lineId) {
        String sql = "delete from section where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void save(Line line) {
        String sectionSql = "insert into section (line_id, up, down, distance) values (?, ?, ?, ?)";

        List<SectionEntity> sections = line.getSections().stream()
                .map(section -> new SectionEntity(
                        line.getId(),
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        section.getDistance().value()
                )).collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sectionSql, sections, BATCH_SIZE,
                (ps, entity) -> {
                    ps.setLong(1, entity.getLineId());
                    ps.setString(2, entity.getLeft());
                    ps.setString(3, entity.getRight());
                    ps.setInt(4, entity.getDistance());
                });
    }
}
