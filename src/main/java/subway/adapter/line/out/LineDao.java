package subway.adapter.line.out;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.application.line.port.in.LineNotFoundException;

class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert lineInsertAction;
    private final InterStationDao interStationDao;
    private final RowMapper<Optional<LineEntity>> optionalRowMapper;

    private final RowMapper<LineEntity> rowMapper;

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        lineInsertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingColumns("name", "color")
                .usingGeneratedKeyColumns("id");
        interStationDao = new InterStationDao(jdbcTemplate);
        optionalRowMapper = getOptionalRowMapper();
        rowMapper = getRowMapper();
    }

    private RowMapper<Optional<LineEntity>> getOptionalRowMapper() {
        return (rs, rowNum) -> {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final String color = rs.getString("color");
            return Optional.of(new LineEntity(id, name, color, interStationDao.findAllByLineId(id)));
        };
    }

    private RowMapper<LineEntity> getRowMapper() {
        return (rs, rowNum) -> {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final String color = rs.getString("color");
            return new LineEntity(id, name, color, interStationDao.findAllByLineId(id));
        };
    }

    public void updateInformation(final LineEntity entity) {
        jdbcTemplate.update("update Line set name = ?, color = ? where id = ?",
                entity.getName(), entity.getColor(), entity.getId());
    }

    public void insertInterStations(final List<InterStationEntity> interStationEntities) {
        interStationDao.insertAll(interStationEntities);
    }

    public void deleteInterStations(final List<InterStationEntity> toInterStationEntities) {
        final List<Long> toInterStationIds = toInterStationEntities.stream()
                .map(InterStationEntity::getId)
                .collect(Collectors.toList());
        final String inClause = toInterStationIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));
        final String sql = String.format("delete from InterStation where id in (%s)", inClause);
        jdbcTemplate.update(sql, toInterStationIds.toArray());
    }

    public List<LineEntity> findAll() {
        return jdbcTemplate.query("select id, name, color from Line", rowMapper);
    }

    public LineEntity insert(final LineEntity lineEntity) {
        final long id = lineInsertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity))
                .longValue();
        final List<InterStationEntity> interStationEntities = lineEntity.getInterStationEntities()
                .stream()
                .map(interStationEntity -> InterStationEntity.of(interStationEntity, id))
                .collect(Collectors.toList());

        interStationDao.insertAll(interStationEntities);
        return findById(id).orElseThrow(LineNotFoundException::new);
    }

    public Optional<LineEntity> findById(final long id) {
        try {
            return jdbcTemplate.queryForObject("select id, name, color from Line where id = ?",
                    optionalRowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(final long id) {
        interStationDao.deleteAllByLineId(id);
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
