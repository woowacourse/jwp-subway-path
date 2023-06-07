package subway.line.db.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.line.db.interstation.InterStationDao;
import subway.line.db.interstation.InterStationEntity;
import subway.line.domain.line.exception.LineNotFoundException;

class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert lineInsertAction;
    private final InterStationDao interStationDao;
    private final RowMapper<Optional<LineEntity>> optionalRowMapper;

    private final RowMapper<LineEntity> rowMapper;

    public LineDao(JdbcTemplate jdbcTemplate) {
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
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String color = rs.getString("color");
            return Optional.of(new LineEntity(id, name, color, interStationDao.findAllByLineId(id)));
        };
    }

    private RowMapper<LineEntity> getRowMapper() {
        return (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String color = rs.getString("color");
            return new LineEntity(id, name, color, interStationDao.findAllByLineId(id));
        };
    }

    public void updateInformation(LineEntity entity) {
        jdbcTemplate.update("update Line set name = ?, color = ? where id = ?",
                entity.getName(), entity.getColor(), entity.getId());
    }

    public void insertInterStations(List<InterStationEntity> interStationEntities) {
        interStationDao.insertAll(interStationEntities);
    }

    public void deleteInterStations(List<InterStationEntity> toInterStationEntities) {
        List<Long> toInterStationIds = toInterStationEntities.stream()
                .map(InterStationEntity::getId)
                .collect(Collectors.toList());
        String inClause = toInterStationIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));
        String sql = String.format("delete from InterStation where id in (%s)", inClause);
        jdbcTemplate.update(sql, toInterStationIds.toArray());
    }

    public List<LineEntity> findAll() {
        return jdbcTemplate.query("select id, name, color from Line", rowMapper);
    }

    public LineEntity insert(LineEntity lineEntity) {
        long id = lineInsertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity))
                .longValue();
        List<InterStationEntity> interStationEntities = lineEntity.getInterStationEntities()
                .stream()
                .map(interStationEntity -> InterStationEntity.of(interStationEntity, id))
                .collect(Collectors.toList());

        interStationDao.insertAll(interStationEntities);
        return findById(id).orElseThrow(LineNotFoundException::new);
    }

    public Optional<LineEntity> findById(long id) {
        try {
            return jdbcTemplate.queryForObject("select id, name, color from Line where id = ?",
                    optionalRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(long id) {
        interStationDao.deleteAllByLineId(id);
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
