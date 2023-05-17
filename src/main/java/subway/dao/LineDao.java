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
import subway.dao.entity.SectionEntity;
import subway.repository.LineWithSectionEntity;

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

    private final RowMapper<LineWithSectionEntity> lineWithSectionEntityRowMapper = (rs, rowNum) ->
            new LineWithSectionEntity(
                    new LineEntity(
                            rs.getLong("line.id"),
                            rs.getString("line.name")
                    ),
                    new SectionEntity(
                            rs.getLong("section.id"),
                            rs.getLong("section.up_station_id"),
                            rs.getLong("section.down_station_id"),
                            rs.getLong("section.line_id"),
                            rs.getInt("section.distance")
                    )
            );

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
        final String sql = "SELECT * FROM line "
                + "LEFT JOIN section ON line.id = section.line_id";

        try {
            List<LineWithSectionEntity> lineWithSectionEntities = jdbcTemplate.query(sql,
                    lineWithSectionEntityRowMapper);
            Map<LineEntity, List<LineWithSectionEntity>> lineEntities = lineWithSectionEntities.stream()
                    .collect(Collectors.groupingBy(LineWithSectionEntity::getLineEntity, Collectors.toList()));

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
