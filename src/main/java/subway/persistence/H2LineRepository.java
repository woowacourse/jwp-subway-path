package subway.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.application.repository.LineRepository;
import subway.application.domain.Distance;
import subway.application.domain.Line;
import subway.application.domain.LineProperty;
import subway.application.domain.Section;
import subway.application.domain.Station;
import subway.persistence.row.SectionRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class H2LineRepository implements LineRepository {

    private static final int BATCH_SIZE = 50;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<LineProperty> propertyRowMapper = (rs, cn) -> new LineProperty(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color")
    );

    private final RowMapper<SectionRow> sectionMapper = (rs, cn) -> new SectionRow(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getString("up_bound"),
            rs.getString("down_bound"),
            rs.getInt("distance")
    );

    public H2LineRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line findById(Long linePropertyId) {
        String linePropertySql = "select id, name, color from line_property where id = ?";
        String sectionSql = "select id, line_id, up_bound, down_bound, distance from section where line_id = ?";

        List<SectionRow> sectionEntities = jdbcTemplate.query(sectionSql, sectionMapper, linePropertyId);
        LineProperty lineProperty = jdbcTemplate.queryForObject(linePropertySql, propertyRowMapper, linePropertyId);

        Map<String, Long> stationIds = getStationIdsOf(sectionEntities);
        List<Section> sections = sectionEntities.stream()
                .map(sectionRow -> new Section(
                        new Station(stationIds.get(sectionRow.getLeft()), sectionRow.getLeft()),
                        new Station(stationIds.get(sectionRow.getRight()), sectionRow.getRight()),
                        new Distance(sectionRow.getDistance())
                )).collect(Collectors.toList());

        return new Line(lineProperty, sections);
    }


    private Map<String, Long> getStationIdsOf(List<SectionRow> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            return Collections.emptyMap();
        }
        List<String> stationNames = getStationNames(sectionEntities);
        String inSql = String.join(",", Collections.nCopies(stationNames.size(), "?"));
        String sql = String.format("select id, name from station where name in (%s)", inSql);

        List<Map.Entry<String, Long>> nameIdKeyValue = jdbcTemplate.query(sql,
                (rs, cn) -> Map.entry(rs.getString("name"), rs.getLong("id")),
                stationNames.toArray());

        return nameIdKeyValue.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> getStationNames(List<SectionRow> sectionEntities) {
        Set<String> stationNames = new HashSet<>();
        for (SectionRow sectionRow : sectionEntities) {
            stationNames.add(sectionRow.getLeft());
            stationNames.add(sectionRow.getRight());
        }
        return new ArrayList<>(stationNames);
    }

    public void removeSections(Long lineId) {
        String sql = "delete from section where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insert(Line line) {
        String sectionSql = "insert into section (line_id, up_bound, down_bound, distance) values (?, ?, ?, ?)";

        List<SectionRow> sections = line.getSections().stream()
                .map(section -> new SectionRow(
                        line.getId(),
                        section.getUpBound().getName(),
                        section.getDownBound().getName(),
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
