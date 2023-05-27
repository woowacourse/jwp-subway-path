package subway.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.dao.entity.LineWithSectionEntities;
import subway.dao.entity.LineWithSectionEntityAndStationEntity;
import subway.dao.entity.SectionWithStationNameEntity;
import subway.dao.entity.StationEntity;

@Repository
public class LineDao {
    private static final int EXIST_NAME = 1;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private final RowMapper<LineWithSectionEntityAndStationEntity> sectionAndStationRowMapper = (rs, rowNum) -> {
        StationEntity upStationEntity = new StationEntity(
                rs.getLong("up_station_id"),
                rs.getString("up_station_name")
        );
        StationEntity downStationEntity = new StationEntity(
                rs.getLong("down_station_id"),
                rs.getString("down_station_name")
        );

        return new LineWithSectionEntityAndStationEntity(
                new LineEntity(
                        rs.getLong("line.id"),
                        rs.getString("line_name")
                ),
                new SectionWithStationNameEntity(
                        rs.getLong("section_id"),
                        upStationEntity,
                        downStationEntity,
                        rs.getInt("distance")
                )
        );
    };

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(LineEntity lineEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity)).longValue();
    }

    public LineEntity findByName(final String name) {
        final String sql = "SELECT * FROM line WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT * FROM line WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineWithSectionEntities> findLinesWithSections() {
        final String sql = "SELECT us.id AS up_station_id, "
                + "l.name AS line_name, "
                + "us.name AS up_station_name, "
                + "ds.id AS down_station_id, "
                + "ds.name AS down_station_name, "
                + "l.id AS line_id, "
                + "s.id AS section_id, "
                + "s.distance AS section_distance "
                + "FROM line l "
                + "LEFT JOIN section s ON l.id = s.line_id "
                + "LEFT JOIN station us ON s.id IS NOT NULL AND s.up_station_id = us.id "
                + "LEFT JOIN station ds ON s.id IS NOT NULL AND s.down_station_id = ds.id ";

        try {
            List<LineWithSectionEntityAndStationEntity> sections = jdbcTemplate.query(sql, sectionAndStationRowMapper);
            Map<LineEntity, List<LineWithSectionEntityAndStationEntity>> lineEntities = sections.stream()
                    .collect(Collectors.groupingBy(
                                    LineWithSectionEntityAndStationEntity::getLineEntity,
                                    Collectors.toList()
                            )
                    );

            return lineEntities.entrySet().stream()
                    .map(entry -> LineWithSectionEntities.of(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public int deleteById(final Long lineId) {
        final String sql = "DELETE FROM line WHERE id = ?";
        return jdbcTemplate.update(sql, lineId);
    }

    public boolean exists(final String lineName) {
        final String sql = "SELECT EXISTS(SELECT * FROM line WHERE name = ?)";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, lineName);

        return result == EXIST_NAME;
    }
}
